# 核心接口

根据接口隔离原则, 结合使用场景划分为如下几个接口

## 1.EntityOperations

JPA的entity对象的一些简单操作API

* `public <T> void persist(T entity);`

  插入一条记录

* `public <T> void persist(List<T> entities);`

  插入一批记录

* `public <T> T save(T entity);`

  插入或者更新记录, 根据主键是否有值判断

* `public <T> List<T> save(List<T> entities);`

* ......

## 2.CriteriaOperations

一些简单的查询可以通过CriteriaOperations来完成, 毕竟写SQL码字数量也挺多的...

**这个接口操作的都是实体类, 不是任意的POJO**

* 根据属性查找

  ```java
  public <T> List<T> findByProperty(Class<T> entityClass, String propertyName, Object value, boolean includeDeleted);
  ```

* 支持排序的版本

  ```java
  public <T> List<T> findByProperty(Class<T> entityClass, String propertyName, Object value, OrderBean... orders)
  ```

* 分页

  ```java
  public <T> List<T> findByProperty(Class<T> entityClass, Predicate predicate, boolean includeDeleted, Page page);
  ```

* 根据属性查找，返回一个对象，如果找到多个，取第一个,可以指定是否包含软删除的记录,找不到则返回null

  ```java
  public <T> T findUniqueByProperty(Class<T> entityClass, String propertyName, Object value, OrderBean... orders);
  ```

* 根据日期区间查找

  ```java
  public <T> List<T> findBetween(Class<T> entityClass, String propertyName, LocalDateTime begin, LocalDateTime end);
  ```

* 根据属性在给定值列表中来获取，可以指定是否包含软删除的对象(IN 操作)

  ```java
  public <T> List<T> findIn(Class<T> entityClass, String propertyName, Collection<?> value);
  ```

* 找到某个属性是null的对象

  ```java
  public <T> List<T> findIsNull(Class<T> entityClass, String propertyName);
  ```

* 检查对应entity是否存在

  ```java
  public <T> boolean ifExists(Class<T> entityClass, String propertyName, Object value);
  ```

* 根据属性查找，返回唯一一个对象，如果找到多个，取第一个，如果找不到则抛出 EntityNotFoundException

  ```java
  public <T> T ensureEntityExists(Class<T> entityClass, String propertyName, Object value) throws EntityNotFoundException;
  ```

* 根据属性删除对象

  ```java
  public <T> int deleteByProperty(Class<T> entityClass, String propertyName, Object propertyValue);
  ```

* 骚操作太多, 不一一列举
  ......

## 3.SQLOperations

**重点总是留到最后**, 在src/main/resources/named-sql目录下添加**XXX.hbm.xml**, 这里面写SQL, 比如:MessageContent.hbm.xml

下面的SQL示例会根据传入参数不同而生成不同的SQL语句

```xml
<?xml version="1.0" encoding="UTF-8"?>  
<!DOCTYPE hibernate-mapping PUBLIC "-//Hibernate/Hibernate Mapping DTD 3.0//EN" "http://www.hibernate.org/dtd/hibernate-mapping-3.0.dtd" >
<hibernate-mapping>
    <sql-query name="queryNeedRetryMsg">
        <![CDATA[
            select * from message_content 
            where 
            #if($timeDiff)
            TIMESTAMPDIFF(SECOND, create_time, SYSDATE()) >:timeDiff and
            #end
            #if($msgStatus)
            msg_status!=:msgStatus and 
            #end
            current_retry<max_retry
    	]]>
    </sql-query>
</hibernate-mapping>
```

Service代码示例

```java
@Service
@Transactional
@Slf4j
public class RetryMsgTask {
  
  @Autowired
  private SQLOperations sqlOperations;
  
  /**
   * 延时5s启动
   * 周期10S一次
   */
  @Scheduled(initialDelay = 10000, fixedDelay = 20000)
  public void retrySend() {
    log.info("-----------------------------");
    //查询五分钟消息状态还没有完结的消息
    Map<String, Object> params = new HashMap<>();
    params.put("timeDiff", MQConstants.TIME_DIFF);
    QueryUtils.enumOrdinalCondition(params, "msgStatus", MsgStatus.CONSUMER_SUCCESS);
    List<MessageContent> messageContents = sqlOperations.namedSqlQuery("queryNeedRetryMsg", params, MessageContent.class);
    ......
  }
}
```

MessageContent可以是任意的POJO, 只要MessageContent的属性与数据库字段**对得上**, 看下面的DDL和Java Bean定义就明白**对得上**的意思了

```mysql
CREATE TABLE `message_content` (
  `msg_id` varchar(50) NOT NULL,
  `order_id` bigint(32) NOT NULL DEFAULT 0,
  `product_id` int(10) NOT NULL DEFAULT 0,
  `msg_status` int(10) NOT NULL DEFAULT 0 COMMENT '0-发送中, 1-mq的broker确认接受到消息, 2-没有对应交换机, 3-没有对应的路由, 4-消费端成功消费消息',
  `exchange` varchar(50) NOT NULL DEFAULT '',
  `routing_key` varchar(50) NOT NULL DEFAULT '',
  `err_cause` varchar(1000) NOT NULL DEFAULT '',
  `max_retry` int(10) NOT NULL DEFAULT 0,
  `current_retry` int(10) NOT NULL DEFAULT 0,
  `create_time` datetime NOT NULL DEFAULT NOW(),
  `update_time` datetime NOT NULL DEFAULT NOW(),
  PRIMARY KEY (`msg_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
```

```java
public class MessageContent {
  private String msgId;
  private Long orderId;
  private Long productId;
  private MsgStatus msgStatus;
  private String exchange;
  private String routingKey;
  private String errCause;
  private Integer maxRetry;
  private Integer currentRetry = 0;
  private LocalDateTime createTime;
  private LocalDateTime updateTime;
}
```

* order_id --> orderId

  你不需要做任何事情, 本框架会自动帮你把数据库字段名和Bean属性名对应上。所以我们在做表设计的时候可以保留数据库字段命名的风格(下划线_分隔), 不需要为了表字段名和Java bean属性名一致而做出妥协。

* msg_status(int) --> msgStatus(enum类型MsgStatus)

  你也不需要做任何事情, 本框会自动帮你把int型转成对应的enum类型。其他常用的数据类型都能识别并在需要时自动转换

**SQLOperations接口常用API示例**

* SQL查询

  ```java
  public <T> List<T> namedSqlQuery(String queryName, Map<String, Object> params, Class<T> clazz);
  ```

  * queryName 是写在XML里面的SQL语句的名字, 全局唯一
  * params 是SQL里面用到的参数
  * clazz 你想要返回的List里面是什么类型的对象, 这个clazz就传什么, clazz可以是任意的POJO

* 支持分页的SQL查询

  ```java
  public <T> List<T> namedSqlQuery(String queryName, Map<String, Object> params, Class<T> clazz, Page page);
  ```

* 跟namedSqlQuery的差别就是结果集不封装到Bean里面

  ```java
  public List<?> namedRawSqlQuery(String queryName);
  ```

* 返回单个值的查询. 比如type是BigDecimal.class，那么这个查询返回的是BigDecimal

  ```java
  public <T> T namedScalarQuery(String queryName, Map<String, Object> params, Class<T> type);
  ```

* 根据给定的查询条件判断是否有符合条件的记录存在

  ```java
  public boolean ifExists(String queryName, Map<String, Object> params);
  ```

* 执行更新

  ```java
  public int executeUpdate(String queryName, Map<String, Object> params);
  ```

* 骚操作太多, 不一一列举

  ......

# 配置

## Maven依赖

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-jpa</artifactId>
</dependency>
<dependency>
    <groupId>com.loserico</groupId>
    <artifactId>commons-lang</artifactId>
</dependency>
<dependency>
    <groupId>com.loserico</groupId>
    <artifactId>loser-orm</artifactId>
</dependency>               
```

## application.yaml配置

配置DataSource和JPA相关属性

```yaml
spring:
  datasource:
    url: jdbc:mysql://192.168.2.101:3306/sexy-mall?useSSL=false&allowPublicKeyRetrieval=true&useLegacyDatetimeCode=false&useCompression=true&useUnicode=true&autoReconnect=true&autoReconnectForPools=true&failOverReadOnly=false
    username: rico
    password: 123456
    driver-class-name: com.mysql.cj.jdbc.Driver
    hikari:
      connection-timeout: 5000
      idle-timeout: 30000
      maximum-pool-size: 15
      minimum-idle: 5
      poolName: HikariAccountServicePool
      data-source-properties:
        cachePrepStmts: true
        cacheResultSetMetadata: true
        elideSetAutoCommits: true
        maintainTimeStats: false
        prepStmtCacheSize: 250
        prepStmtCacheSqlLimit: 2048
        rewriteBatchedStatements: true
        useLocalSessionState: true
        useServerPrepStmts: true
  jpa:
    database-platform: org.hibernate.dialect.MySQL57Dialect
    hibernate:
      naming:
        implicit-strategy: org.hibernate.boot.model.naming.ImplicitNamingStrategyLegacyJpaImpl
        physical-strategy: org.hibernate.boot.model.naming.PhysicalNamingStrategyStandardImpl
    properties:
      hibernate:
        ddl-auto: none
        format_sql: true
    show-sql: true
```

## Java Config

loser-orm只需要配一个bean, 就是这么简单。

```java
@Configuration
@Slf4j
public class AppConfig {
  
    @Bean
    public JpaDao jpaDao() {
      return new JpaDao();
    }
}
```

**解释一下:**

* 通过`@PersistenceContext`自动将系统中自动配置的EntityManager注入到了JpaDao里面

  ```java
  @PersistenceContext
  protected EntityManager entityManager;
  ```


# 示例

## Service层注入JpaDao

在Service层根据需要注入JpaDao的实例到这几个核心接口类型上

```java
@Autowired
private EntityOperations entityOperations;

@Autowired
private CriteriaOperations criteriaOperations;

@Autowired
private SQLOperations sqlOperations;
```

业务方法里面执行查询

```java
public List<PurchaseOrderListsVO> searchPurchaseOrder(PurchaseOrderSearchVO purchaseOrderSearchVO) {
    Map<String, Object> params = new HashMap<>();
    params.put("beginDate", purchaseOrderSearchVO.getBeginDate());
    params.put("endDate", purchaseOrderSearchVO.getEndDate());
    params.put("supplierId", purchaseOrderSearchVO.getSupplierId());
    params.put("status", QueryUtils.innerMatch(purchaseOrderSearchVO.getStatus()));
    params.put("auditStatus", purchaseOrderSearchVO.getAuditStatus());
    params.put("skus", QueryUtils.innerMatch(purchaseOrderSearchVO.getSkus()));
    params.put("purchaseContractNo", QueryUtils.innerMatch(purchaseOrderSearchVO.getPurchaseContractNo()));
    List<PurchaseOrderListsVO> purchaseOrderListsVOS = sqlOperations.namedSqlQuery("searchPurchaseOrder", params, PurchaseOrderListsVO.class, purchaseOrderSearchVO.getPage());
	......
}
```

PurchaseOrderListsVO是普通的POJO或者Entity都可以, 查询结果的字段名(如PURCHASE_CONTRACT_NO)自动转成POJO的属性名purchaseContractNo

有数据类型不一致的也自动转换

```java
@Data
public class PurchaseOrderListsVO {
    private Long id;
    private String orderNo;
    private String purchaseContractNo;//采购单合同号
    private Long supplierId;//供应商ID
	......
}
```

其中`searchPurchaseOrder`是定义在XML中的SQL的名字, 看下面

## src/main/resources/named-sql

这个目录下放SQL语句, 如PurchaseOrder.hbm.xml

```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE hibernate-mapping PUBLIC "-//Hibernate/Hibernate Mapping DTD 3.0//EN"
        "http://www.hibernate.org/dtd/hibernate-mapping-3.0.dtd" >
<hibernate-mapping>
    <sql-query name="searchPurchaseOrder">
        <![CDATA[
            SELECT
			-- SQL分页支持, 会另外生成一条SQL查总记录数
             #count()
                po.*
             #end
             FROM purchase_order po
             WHERE po.SUPPLIER_ID = :supplierId
             -- 表示Java代码里面传了blocParentId这个参数的话, 生成的SQL里面就会包含 AND uo.BLOC_PARENT_ID= :blocParentId 这一段
             #if($blocParentId)
             AND po.BLOC_PARENT_ID= :blocParentId
             -- 支持elseif, else语句
             #elseif($count > 0)
             .........
             #else
              .........
             #end
			-- 如果传了beginDate和endDate, 则生成SQL: AND CLOSE_MONTH BETWEEN :beginDate AND :endDate
			-- 只传   beginDate, 那么生成SQL: AND CLOSE_MONTH >= :beginDate
			-- 只传   endDate,   那么生成SQL: AND CLOSE_MONTH <= :endDate
             -- 都不传的话什么都不生成
			#between("CLOSE_MONTH", $beginDate, $endDate)
		]]>
    </sql-query>
</hibernate-mapping>
```

