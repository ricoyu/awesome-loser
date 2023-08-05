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

# 二 API Demo

## 2.1 查询API

### 2.1.1 Request Body Search

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

### 2.1.2 match query

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



### 2.1.3 match phrase

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



### 2.1.4 Query String Query

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




### 2.1.5 通过constant score转为Filter

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

### 2.1.6 结果自动封装进POJO

```java
List<Movie> movies = ElasticUtils.Query.query("movies")
				//查询结果纪要包含matrix, 又要包含reload
				.queryBuilder(matchQuery("title", "Matrix reloaded").operator(Operator.AND))
				.type(Movie.class)
				.queryForList();
```

### 2.1.7 Range查询

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

### 2.1.8 exists查询

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

### 2.1.9 bool查询

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





## 2.2 聚合

### 2.2.1 Bucket聚合

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

### 2.2.2 Metric

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

### 2.2.3 Bucket和Metric 混合



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

### 2.2.4 stats子聚合



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

