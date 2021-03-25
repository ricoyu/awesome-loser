classpath根目录下放jackson.properties, 支持的配置项如下

1. loser.jackson.ignore_case=false

   JSON转POJO时, 是否支持属性名大小写不敏感匹配, 默认false

2. jackson.fail.on.unknown.properties=false

   JSON转POJO时, 某属性JSON中有, POJO中没有时, 是否报错, 默认不报错
   
3. loser.jackson.enum.propertes

   支持基于Enum类型的某个属性反序列化成Enum对象, 属性值是逗号隔开的字符串

4. loser.jackson.epochBased

   日期时间类型是否默认序列化成1970-01-01以来的毫秒数

   