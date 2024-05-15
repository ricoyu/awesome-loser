Transport Client 在ES 8.0开始就不能用了

# 一 配置

working dir 或者 workingDir/conf目录下添加 elastic.properties

```properties
#必填, 要跟你在Elasticsearch.yml中配置的集群名字一样(cluster.name: es7-application), 否则会连不上Elasticsearch
cluster.name=es7-application

elastic.hosts=192.168.100.101:9300,192.168.100.102:29300,192.168.100.103:29300
elastic.rest.hosts=192.168.100.101:9200,192.168.100.102,192.168.100.103:9200
elastic.username=elastic
elastic.password=123456
#elasticsearch服务端使用的证书
keystore.path=D:\\elastic-certificates.p12
```

# 二 集群相关

## 2.1 集群的健康值检查

* Green

  健康, 所有的主分片(Primary)和副本分片(Replica)都可用

* Yellow

  亚健康, 所有的主分片可用, 部分副本分片不可用

* Red

  不健康状态, 部分主分片不可用

1. GET _cluster/health

2. 对应ElasticUtils API

   ```java
   ElasticUtils.Cluster.health()
   ```

   

# 三 索引相关

# 四 文档相关

1. 创建一个文档, 指定ID

   ```java
   ElasticUtils.index("rico", "{\"key\": \"三少爷\"}", "1");
   ```

   或者

   ```java
   ElasticUtils.index("rico").doc("{\"key\": \"三少爷\"}").id(1).execute();
   ```

2. 将一个POJO索引文档

   ```java
   @Data
   @NoArgsConstructor
   @AllArgsConstructor
   public static class Person {
     
     @DocId
     private Integer id;
     private String user;
     private String comment;
   }
   ```

   ```java
   Person person = new Person();
   person.setUser("三少爷");
   person.setComment("牛仔");
   String id = ElasticUtils.index("rico", person);
   System.out.println(id);
   ```

3. 批量创建文档

   ```java
   String[] docs = new String[]{"{\"name\": \"三少爷\"}", "{\"name\": \"二少爷\"}", "{\"name\": \"大少爷\"}"};
   BulkResult bulkResult = ElasticUtils.bulkIndex("rico", docs);
   System.out.println(toJson(bulkResult));
   ```

   ```java
   List<Person> persons = new ArrayList<>();
   persons.add(new Person(1, "Json", "this is jason born"));
   persons.add(new Person(2, "Icon Man", "this is Stark"));
   persons.add(new Person(3, "Sea King", "this is 海王"));
   
   BulkResult bulkResult = ElasticUtils.bulkIndex("rico", persons);
   System.out.println(toJson(bulkResult));
   ```

   ```java
   List<Product> products = asList(new Product("1", "XHDK-A-1293-#fJ3", "iPhone"),
       new Product("2", "KDKE-B-9947-#kL5", "iPad"),
       new Product("3", "JODL-X-1937-#pV7", "MBP"));
   BulkResult bulkResult = ElasticUtils.bulkIndex("products", products);
   System.out.println(toJson(bulkResult));
   ```

   

## 2.2 查询API

### 2.2.1 Request Body Search

1. Query DSL

   ```json
   POST movies/_search?ignore_unavailable=true
   {
     "profile": "true",
     "query": {
       "match_all": {}
     }
   }
   ```

   对应ElasticUtils API

   ```java
   List<Object> movies = ElasticUtils.Query
       .matchAllQuery("movies", "404index")
       .queryForList();
   log.info("查询到{}条记录", movies.size());
   ```

2. _source 过滤

   如果文档很大, 或者你不需要获取到所有的字段, 那么可以对source进行过滤, 值获取到想要的字段

   ```json
   POST kibana_sample_data_ecommerce/_search
   {
     "sort": [
       {
         "order_date": "desc"
       }
     ]
     , "_source": ["order_date"]
   }
   ```

   对应ElasticUtils API

   ```java
   List<Object> ecommerces = ElasticUtils.Query
       .matchAllQuery("kibana_sample_data_ecommerce")
       .sort("order_date:desc")
       .includeSources("order_date")
       .queryForList();
   ```

### 2.2.2 match query

默认是king OR george这样一个查询条件

```json
POST movies/_search
{
  "query": {
    "match": {
      "title": "King George"
    }
  }
}
```

可以设置operator, 设为king AND george这样一个查询条件

```json
POST movies/_search
{
  "query": {
    "match": {
      "title": {
        "query":"King George", 
        "operator": "AND"
      }
    }
  }
}
```

对应ElasticUtils API

```java
List<Object> movies = ElasticUtils.Query.matchQuery("movies")
  .query("title", "King George")
  .operator(Operator.AND)
  .queryForList();
```



### 2.2.3 match phrase

在query里面的查询词必须是按照顺序出现的, slop 1表示one love之间可以插入一个其他的单词

```http
POST movies/_search
{
  "query": {
    "match_phrase": {
       "title": {
         "query": "one love",
         "slop": 1
       }
    }
  }
}
```

对应ElasticUtils API:

```java
List<Object> movies = ElasticUtils.Query
    .matchPhraseQuery("movies")
    .query("title", "one love")
    .slop(1)
    .queryForList();
```



### 2.2.4 Query String Query

* 类似URI Querys

* AND OR 要大写

* 支持通配符, 支持查询多个字段

* default_field 要查询的默认字段

* fields 查询多个字段

  ```http
  POST users/_search
  {
    "query": {
      "query_string": {
        "default_field": "name",
        "query": "Rruan AND Yiming"
      }
    }
  }
  ```

  对接ElasticUtils API:

  ```java
  @Test
  public void testQueryStringQuery() {
    List<Object> objects = ElasticUtils.Query.query("users")
        .queryBuilder(queryStringQuery("Ruan Yiming").field("name"))
        .queryForList();
    objects.forEach(System.out::println);
  }
  ```




### 2.2.5 通过constant score转为Filter

可以避免算分带来的性能开销, 即可以提高查询性能

```json
POST products/_search
{
  "explain": true,
  "query": {
    "constant_score": {
      "filter": {
        "term": {
          "productID.keyword": "XHDK-A-1293-#fJ3"
        }
      }
    }
  }
}
```

对应ElasticUtils API

```java
List<Object> iphones = ElasticUtils.Query.termQuery("products")
    .query("productID.keyword", "XHDK-A-1293-#fJ3")
    .constantScore(true)
    .includeSources("desc")
    .queryForList();
```

### 2.2.6 结果自动封装进POJO

```java
List<Movie> movies = ElasticUtils.Query.query("movies")
				//查询结果纪要包含matrix, 又要包含reload
				.queryBuilder(matchQuery("title", "Matrix reloaded").operator(Operator.AND))
				.type(Movie.class)
				.queryForList();
```

### 2.2.7 Range查询

now-1y 表示一年以前

```json
POST products/_search
{
  "query": {
    "range": {
      "date": {
        "gte": "now-5y"
      }
    }
  }
}
```

**注意:**now-5y 中间不要有空格

对应ElasticUtils API

```java
List<Object> products = ElasticUtils.Query
        .range("products")
        .field("date")
        .gt("now-5y")
        .queryForList();
```

### 2.2.8 exists查询

查询包含date字段的products

```json
POST products/_search
{
  "query": {
    "exists": {
      "field": "date"
    }
  }
}
```

对应ElasticUtils API

```java
List<Object> products = ElasticUtils.Query.exists("products")
        .field("date")
        .queryForList();
```

### 2.2.9 bool查询

```json
POST newmovies/_search
{
  "query": {
    "bool": {
      "must": [
        {"term": {
          "genre.keyword": {
            "value": "Comedy"
          }
        }},
        {
          "term": {
            "genre_count": {
              "value": "1"
            }
          }
        }
      ]
    }
  }
}
```

对应ElasticUtils API

```java
List<Object> movies = ElasticUtils.Query
				.bool("newmovies")
				.term("genre.keyword", "Comedy").must()
				.term("genre_count", 1).must()
				.queryForList();
```



### 2.2.10 Disjunction Max Query

分离最大化查询(Disjunction Max Query)指的是: 将任何与任一查询匹配的文档作为结果返回, 但只将最佳匹配的评分作为查询的评分结果返回



```json
POST blogs/_search
{
  "query": {
    "dis_max": {
      "queries": [
        {"match": {"title": "Quick fox"}}, 
        {"match": {"body": "Quick fox"}}  
      ]
    }
  }
}
```

对应ElasticUtils API

```java
DisMaxQueryBuilder queryBuilder = disMaxQuery()
				.add(matchQuery("title", "Brown fox"))
				.add(matchQuery("body", "Brown fox"));
		queryBuilder.tieBreaker(0f);
		List<String> blogs = ElasticUtils.Query.query("blogs")
				.queryBuilder(queryBuilder)
				.queryForList();
```



### 2.2.11 Tie Breaker 最佳字段查询调优

Tie Breaker是一个介于0-1之间的浮点数, 0代表使用最佳匹配; 1代表所有语句同等重要

```
POST blogs/_search
{
  "query": {
    "dis_max": {
      "tie_breaker": 0.2,
      "queries": [
        {"match": {"title": "Quick pets"}},
        {"match": {"body": "Quick pets"}}
      ]
    }
  }
}
```

对应ElasticUtils API

```java
DisMaxQueryBuilder queryBuilder = disMaxQuery()
    .tieBreaker(0.2f)
    .add(matchQuery("title", "Quick pets"))
    .add(matchQuery("body", "Quick pets"));
List<Object> blogs = ElasticUtils.Query
    .query("blogs")
    .queryBuilder(queryBuilder)
    .queryForList();
```

### 2.2.12 Multi Match Query

1. 最佳字段(Best Fields)

   当字段之间相互竞争, 有相互关联, 例如title和body这样的字段, 评分来自最匹配字段

2. 多数字段(Most Fields)

   处理英文内容时: 一种常见的手段是是, 在主字段(English Analyzer), 抽取词干, 加入同义词, 以匹配更多的文档. 相同的文本, 加入子字段(Standard Analyzer), 以提供更精确的匹配. 其他字段作为匹配文档提高相关度的信号. 匹配字段越多则越好

3. 混合字段(Cross Field)

   对于某些实体, 例如人名, 地址, 图书信息. 需要在多个字段中确定信息, 单个字段只能作为整体的一部分. 希望在任何这些列出的字段中找到尽可能多的词

#### 1) best_fields 

```json
POST blogs/_search
{
  "query": {
    "multi_match": {
      "query": "Quick pets",
      "type": "best_fields", 
      "fields": ["title", "body"], 
      "tie_breaker": 0.2, 
      "minimum_should_match": "20%"
    }
  }
}
```

* multi_match 声明这是一个Multi Match Query

* query       提供一个查询的语句

* fields      查询语句要匹配到哪些字段上

* type

  * best_fields 默认类型, 最佳字段, 可以不指定

    表示会在fields指定的字段中取一个评分最高的作为一个返回结果

  * most_fields 多数字段

  * cross_fields 跨字段

对应ElasticUtils API

```java
List<Object> movies = ElasticUtils.Query
        .multiMatch("blogs")
        .query("Quick pets")
        .size(1)
        .includeSources("title", "overview")
        .queryForList();
```

#### 2) Most_fields

对多个字段上的算分进行累加

1. 英文分词器, 导致精确度降低, 时态信息丢失

   ```json
   DELETE titles
   PUT titles
   {
     "mappings": {
       "properties": {
         "title":{
           "type": "text",
           "analyzer": "english"
         }
       }
     }
   }
   
   PUT titles/_bulk
   {"index":{"_id":1}}
   {"title":"My Dog barks"}
   {"index":{"_id":2}}
   {"title":"I see a lot of barking dogs  on the road"}
   ```

   下面这个查询文档1排在前面, 但实际应该文档2更匹配, 应该排在前面

   ```json
   POST titles/_search
   {
     "query": {
       "match": {
         "title": "barking dogs"
       }
     }
   }
   ```

   对应ElasticUtils API

   ```java
   MultiMatchQueryBuilder queryBuilder = multiMatchQuery("barking dogs", "title")
       .type(MOST_FIELDS);
   List<Object> titles = ElasticUtils.Query
       .query("titles")
       .queryBuilder(queryBuilder)
       .queryForList();
   ```




#### 3 跨字段搜索 cross_fields

```json
POST address/_doc/1
{
 "street": "5 Poland Street", 
 "city": "Lodon", 
 "country": "United Kingdom", 
 "postcode": "W1V 3DG"
}
POST address/_doc/2
{
 "street": "5 Poland Street", 
 "city": "Berminhan", 
 "country": "United Kingdom", 
 "postcode": "W2V 3DG"
}
```

```java
MultiMatchQueryBuilder queryBuilder = multiMatchQuery("Poland Street W1V", "street", "city", "country", "postcode")
    .type(MOST_FIELDS);
List<Object> addresses = ElasticUtils.Query
    .query("address")
    .queryBuilder(queryBuilder)
    .queryForList();
```





## 2.3 聚合

### 2.3.1 Bucket聚合

```json
POST kibana_sample_data_flights/_search
{
  "size": 0,
  "aggs": {
    "flight_dest": {
      "terms": {
        "field": "DestCountry"
      }
    }
  }
}
```

对应ElasticUtils API

```java
List<Map<String, Object>> aggResults1 = Aggs.terms("kibana_sample_data_flights")
      .of("dest-country", "DestCountry")
      .sort("count")
      .get();
```

这里Map的value是一个Long类型

### 2.3.2 Metric

```json
POST kibana_sample_data_flights/_search
{
  "size": 0,
  "aggs": {
    "flight_dest": {
      "terms": {
        "field": "DestCountry",
        "size": 3
      },
      "aggs": {
        "average_price": {
          "avg": {
            "field": "AvgTicketPrice"
          }
        },
        "max_price":{
          "max": {
            "field": "AvgTicketPrice"
          }
        },
        "min_price":{
          "min": {
            "field": "AvgTicketPrice"
          }
        }
      }
    }
  }
}
```

对应ElasticUtils API

```java
TermsAggregationBuilder termsAggregationBuilder = terms("flight_dest").field("DestCountry")
    .subAggregation(AggregationBuilders.avg("avg_price").field("AvgTicketPrice"))
    .subAggregation(max("max_price").field("AvgTicketPrice"))
    .subAggregation(min("min_price").field("AvgTicketPrice"));

SearchResponse response = ElasticUtils.CLIENT.prepareSearch("kibana_sample_data_flights")
    .setSize(0)
    .addAggregation(termsAggregationBuilder)
    .get();

Aggregations aggregations = response.getAggregations();
for (Aggregation aggregation : aggregations) {
  System.out.println("Aggregation: " + aggregation.getName());
  List<Bucket> buckets = ((StringTerms) aggregation).getBuckets();
  for (Bucket bucket : buckets) {
    String key = bucket.getKeyAsString();
    long docCount = bucket.getDocCount();
    System.out.println("Key: " + key + ", Doc Count: " + docCount);
    
    Aggregations subAggs = bucket.getAggregations();
    if (subAggs != null) {
      for (Aggregation subAgg : subAggs) {
        System.out.println(toJson(subAgg));
        String name = subAgg.getName(); //max_price
        String writeableName = ((NamedWriteable) subAgg).getWriteableName(); //max min 等聚合的类型
        Object value = ReflectionUtils.getFieldValue(writeableName, subAgg);
        if (writeableName.equals("avg")) {
          value = ReflectionUtils.invokeMethod(subAgg, "getValue");
        }
        System.out.println(writeableName + ":" + value);
      }
    }
  }
}
```

### 2.3.3 Bucket和Metric 混合



```json
POST kibana_sample_data_flights/_search
{
  "size": 0,
  "aggs": {
    "flight_dest": {
      "terms": {
        "field": "DestCountry",
        "size": 2
      },
      "aggs": {
        "average_price": {
          "avg": {
            "field": "AvgTicketPrice"
          }
        },
        "weather": {
          "terms": {
            "field": "DestWeather"
          }
        }
      }
    }
  }
```

对应ElasticUtils API

```java
List<Map<String, Object>> resultMap = Aggs.terms("kibana_sample_data_flights")
        .of("flight_dest", "DestCountry").size(2)
        .subAggregation(avg("average_price", "AvgTicketPrice"))
        .subAggregation(SubAggregations.terms("weather", "DestWeather"))
        .get();
```

### 2.3.4 stats子聚合



```json
POST kibana_sample_data_flights/_search
{
  "size": 0,
  "aggs": {
    "flight_dest": {
      "terms": {
        "field": "DestCountry",
        "size": 2
      },
      "aggs": {
        "state_price": {
          "stats": {
            "field": "AvgTicketPrice"
          }
        },
        "weather": {
          "terms": {
            "field": "DestWeather",
            "size": 5
          }
        }
      }
    }
  }
}
```

对应ElasticUtils API

```java
List<Map<String, Object>> resultMap = Aggs.terms("kibana_sample_data_flights")
        .of("flight_dest", "DestCountry").size(2)
        .subAggregation(SubAggregations.stats("state_price", "AvgTicketPrice"))
        .subAggregation(SubAggregations.terms("weather", "DestWeather").size(5))
        .get();
```

