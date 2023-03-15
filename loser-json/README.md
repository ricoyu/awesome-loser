classpath根目录下放jackson.properties, 支持的配置项如下

1. loser.jackson.ignore_case=false

   JSON转POJO时, 是否支持属性名大小写不敏感匹配, 默认false

2. jackson.fail.on.unknown.properties=false

   JSON转POJO时, 某属性JSON中有, POJO中没有时, 是否报错, 默认不报错
   
3. loser.jackson.enum.propertes

   支持基于Enum类型的某个属性反序列化成Enum对象, 属性值是逗号隔开的字符串

4. loser.jackson.epochBased

   日期时间类型是否默认序列化成1970-01-01以来的毫秒数

   

# JsonPath示例

```json
{
    "store": {
        "book": [
            {
                "category": "reference",
                "author": "Nigel Rees",
                "title": "Sayings of the Century",
                "price": 8.95
            },
            {
                "category": "fiction",
                "author": "Evelyn Waugh",
                "title": "Sword of Honour",
                "price": 12.99
            },
            {
                "category": "fiction",
                "author": "Herman Melville",
                "title": "Moby Dick",
                "isbn": "0-553-21311-3",
                "price": 8.99
            },
            {
                "category": "fiction",
                "author": "J. R. R. Tolkien",
                "title": "The Lord of the Rings",
                "isbn": "0-395-19395-8",
                "price": 22.99
            }
        ],
        "bicycle": {
            "color": "red",
            "price": 19.95
        }
    },
    "expensive": 10
}
```

| JsonPath (click link to try)                                 | Result                                                       |
| ------------------------------------------------------------ | ------------------------------------------------------------ |
| $.store.book[*].author                                       | The authors of all books                                     |
| [$..author](http://jsonpath.herokuapp.com/?path=$..author)   | All authors                                                  |
| [$.store.*](http://jsonpath.herokuapp.com/?path=$.store.*)   | All things, both books and bicycles                          |
| [$.store..price](http://jsonpath.herokuapp.com/?path=$.store..price) | The price of everything                                      |
| [$..book[2\]](http://jsonpath.herokuapp.com/?path=$..book[2]) | The third book                                               |
| [$..book[-2\]](http://jsonpath.herokuapp.com/?path=$..book[-2]) | The second to last book                                      |
| [$..book[0,1\]](http://jsonpath.herokuapp.com/?path=$..book[0,1]) | The first two books                                          |
| [$..book[:2\]](http://jsonpath.herokuapp.com/?path=$..book[:2]) | All books from index 0 (inclusive) until index 2 (exclusive) |
| [$..book[1:2\]](http://jsonpath.herokuapp.com/?path=$..book[1:2]) | All books from index 1 (inclusive) until index 2 (exclusive) |
| [$..book[-2:\]](http://jsonpath.herokuapp.com/?path=$..book[-2:]) | Last two books                                               |
| [$..book[2:\]](http://jsonpath.herokuapp.com/?path=$..book[2:]) | All books from index 2 (inclusive) to last                   |
| [$..book[?(@.isbn)\]](http://jsonpath.herokuapp.com/?path=$..book[?(@.isbn)]) | All books with an ISBN number                                |
| [$.store.book[?(@.price < 10)\]](http://jsonpath.herokuapp.com/?path=$.store.book[?(@.price < 10)]) | All books in store cheaper than 10                           |
| [$..book[?(@.price <= $['expensive'\])]](http://jsonpath.herokuapp.com/?path=$..book[?(@.price <= $['expensive'])]) | All books in store that are not "expensive"                  |
| [$..book[?(@.author =~ /.*REES/i)\]](http://jsonpath.herokuapp.com/?path=$..book[?(@.author =~ /.*REES/i)]) | All books matching regex (ignore case)                       |
| [$..*](http://jsonpath.herokuapp.com/?path=$..*)             | Give me every thing                                          |
| [$..book.length()](http://jsonpath.herokuapp.com/?path=$..book.length()) | The number of books                                          |