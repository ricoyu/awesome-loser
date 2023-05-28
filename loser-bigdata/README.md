# loser-bigdata
大数据相关API

classpath root下放hdfs.properties, 里面配置所有的NameNode管理地址, 启动时回去检查哪一个NameNode是Active的状态, 然后去连Active的那个

```properties
hdfs.hosts=192.168.2.101:9870,192.168.2.102:9870,192.168.2.103:9870
```

