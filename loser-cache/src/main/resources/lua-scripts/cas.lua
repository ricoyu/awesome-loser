--操作的key
local key = KEYS[1]
--要设置的新值
local value = tonumber(ARGV[1])
--[[
  mode < 0 表示value小于原来的值才set新值
  mode > 0 表示value大于原来的值才set新值
  等于这里不处理 -;)
]]--
local mode = tonumber(ARGV[2])

--key不存在则直接set
local exists = redis.call("EXISTS", key)
if(exists == 0) then
    redis.call("SET", key, value)
    return 1
end

local oldValue = tonumber(redis.call("GET", key))
if(mode < 0) then
    if(value < oldValue) then
        redis.call("SET", key, value)
        return 1
    end
    return 0
end

if(mode > 0) then
    if(value > oldValue) then
        redis.call("SET", key, value)
        return 1
    end
    return 0
end

return 0
