--[[
 Hash 相关操作API
  
  支持Hash field 级别的细粒度过期时间控制
  
 1.给HASH设置值的时候分两步操作  
  - hset key field value, 给指定的field设置值  redis.call("HSET", key, field, value)
  - zadd jedis_utils__timeout_set:{key} ch currentMilis+timeout field, 设置一个ZSET, element是field, score是field过期的timestamp
    local currentTimestamp = redis.call("TIME")[1] -- 得到当前时间,单位是秒
    redis.call("ZADD", jedis_utils__timeout_set:{key}, currentTimestamp + expires, field)
    eval "return redis.call('zadd', KEYS[1], ARGV[1], ARGV[2])" 1 jedis_utils__timeout_set:usershash 3 xuehua
    eval "return redis.call('zscore', KEYS[1], ARGV[1])" 1 jedis_utils__timeout_set:usershash xuehua
    zadd ch 作用: field 不存在则插入, 同时设置score, 否则更新其score
  
 2.获取hash时的操作
  zscore jedis_utils:__timeout__set:{key} field 获取ZSET中成员field的score
  - 返回nil 表示这个key没有设置过期时间
          直接 hget key field
  - 返回score
    - score >= current_timestamp ?
               - 是(表示HASH的field已经过期)
                 1)zrem jedis_utils:__timeout__set:{key} field  -- 从ZSET中移除field
                 2)hdel key field -- 从HASH中删除field
               - 否
                 hget key field
 3.显式删除 field
   zrem jedis_utils:__timeout__set:{key} field1 field2 先删zset中的member
   hdel key field1 field2...
          返回删除的field数量     
   
注意：ZSET 或者 HASH中如果没有element了, ZSET 或 HASH 本身就会被自动删除了

key     hash的key
zsetKey zset的key
field   hash的field和zset的memeber
value   hash的value
ttl     多少毫秒后过期
返回    1   field插入
    0   field更新   
]]

-- score 以毫秒为单位
local zadd = function(key, score, member)
  local currentTimestamp = redis.call("TIME")[1] -- 得到当前时间,单位是秒
  return redis.call("ZADD", key, currentTimestamp + score, member)
end

--[[
   根据auth:token:ttl:zset的score，与当前timestamp比较，score<timestamp表示过期了, 超过有效时间范围执行登出操作清理数据,返回expiredTokenLoginInfo
   返回一个数组[token, loginInfo, token2, loginInfo2...]
   
 PUBLISH 消息
 ]]
local clearExpired = function(key, zsetKey)
  local currentTimestamp = redis.call("TIME")[1] -- 得到当前时间,单位是秒
  -- 拿score从负无穷大到currentTimestamp之间的元素
  local expiredFields = redis.call("ZRANGEBYSCORE", zsetKey, "-inf", currentTimestamp)

  -- 有token过期
  if(#expiredFields ~= 0) then
    redis.replicate_commands()
    for index, field in ipairs(expiredFields) do
      redis.call("ZREM", zsetKey, field)
      redis.call("HDEL", key, field)
    end
  end
end

local hset = function(key, zsetKey, field, value, ttl)
  clearExpired(key, zsetKey) -- 先清除过期的key
  redis.replicate_commands()
  -- 如果设置了过期时间, 则将field插入zset或者更新其在zset中的ttl
  if ttl ~= "-1" and ttl ~= "0" then -- 表示field设置了过期时间
    zadd(zsetKey, ttl, field)
  else -- 否则从zset中删除field
    redis.call("ZREM", zsetKey, field)
  end
  return redis.call("HSET", key, field, value)
end

--[[
  - 返回nil 表示这个key没有设置过期时间
          直接 hget key field
  - 返回score
    - score >= current_timestamp ?
               - 是(表示HASH的field已经过期)
                 1)zrem jedis_utils:__timeout__set:{key} field  -- 从ZSET中移除field
                 2)hdel key field -- 从HASH中删除field
               - 否
                 hget key field
]]
local hget = function(key, zsetKey, field)
  clearExpired(key, zsetKey) -- 先清除过期的key
  return redis.call("HGET", key, field)
end

--[[
删除hash的field
]]
local hdel = function(key, zsetKey, field)
  redis.call("ZREM", zsetKey, field)
  return redis.call("HDEL", key, field)
end

--[[
删除hash的field并返回该field对应的值
]]
local hdelGet = function(key, zsetKey, field)
  redis.call("ZREM", zsetKey, field)
  local value = redis.call("HGET", key, field)
  redis.call("HDEL", key, field)
  return value
end

--[[
  返回-3 表示HASH key不存在
  返回-2 表示字段不存在 
  返回-1 表示字段存在但是没有过期时间
  否则返回剩余的ttl
]]
local ttl = function(key, zsetKey, field)
  clearExpired(key, zsetKey)
  local keyExists = redis.call("EXISTS", key)
  if keyExists == 0 then -- key不存在
    return -3
  end
  local fieldExists = redis.call("HEXISTS", key, field)
  if fieldExists == 0 then -- field不存在
    return -2
  else
    local milis = redis.call("ZSCORE", zsetKey, field)
    if milis then
      return milis - redis.call("TIME")[1] -- 返回还有多少秒过期
    else
      return -1 -- 没有设置过期时间
    end
  end
end

local expire = function(key, zsetKey, field, ttl)
  clearExpired(key, zsetKey)
  local keyExists = redis.call("EXISTS", key)
  if keyExists == 0 then
    return -1
  end
  local fieldExists = redis.call("HEXISTS", key, field)
  if fieldExists == 0 then
    return 0
  else 
    local timeToLive = redis.call("TIME")[1] + ttl
    redis.replicate_commands()
    redis.call("ZADD", zsetKey, timeToLive, field)
    return 1
  end
end

local expiredFields = function(zsetKey)
  local currentTimestamp = redis.call("TIME")[1] -- 得到当前时间,单位是秒
  -- 拿score从负无穷大到currentTimestamp之间的元素
  local expiredFields = redis.call("ZRANGEBYSCORE", zsetKey, "-inf", currentTimestamp)
  if #expiredFields ~= 0 then
    return cjson.encode(expiredFields)
  end
  return nil;
end

local persist = function(key, zsetKey, field)
  clearExpired(key, zsetKey)
  local fieldExists = redis.call("HEXISTS", key, field)
  if fieldExists == 0 then
    return 0
  else
    redis.replicate_commands()
    redis.call("ZREM", zsetKey, field)
    return 1
  end
end

local operate = ARGV[1]
if operate == "hset" then
  local key = KEYS[1]
  local zsetKey = KEYS[2]
  local field = ARGV[2]
  local value = ARGV[3]
  local ttl = ARGV[4]
  return hset(key, zsetKey, field, value, ttl)
elseif operate == "hget" then
  local key = KEYS[1]
  local zsetKey = KEYS[2]
  local field = ARGV[2]
  return hget(key, zsetKey, field)
elseif operate == "time" then
  return redis.call("TIME")[1] * 1000 
elseif operate == "expiredFields" then
  local zsetKey = KEYS[1]
  return expiredFields(zsetKey)
elseif operate == "ttl" then
  local key = KEYS[1]
  local zsetKey = KEYS[2]
  local field = ARGV[2]
  return ttl(key, zsetKey, field)
elseif operate == "hdel" then
  local key = KEYS[1]
  local zsetKey = KEYS[2]
  local field = ARGV[2]
  return hdel(key, zsetKey, field)
elseif operate == "hdelGet" then
  local key = KEYS[1]
  local zsetKey = KEYS[2]
  local field = ARGV[2]
  return hdelGet(key, zsetKey, field)
elseif operate == "expire" then
  local key = KEYS[1]
  local zsetKey = KEYS[2]
  local field = ARGV[2]
  local ttl = ARGV[3]
  return expire(key, zsetKey, field, ttl)
elseif operate == "persist" then
  local key = KEYS[1]
  local zsetKey = KEYS[2]
  local field = ARGV[2]
  return persist(key, zsetKey, field)
end  
