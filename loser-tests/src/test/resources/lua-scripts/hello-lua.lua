local set = redis.call("setnx", "k1", "v1")
return type(set)
