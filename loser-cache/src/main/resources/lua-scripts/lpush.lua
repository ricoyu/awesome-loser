-- list的名字
local key = KEYS[1]
-- ARGV的第一个值表示限制list的长度
local limit = tonumber(ARGV[1])
-- 参数个数
local argCount = #ARGV

--list当前的size
local size = redis.call("LLEN", key)
local index = 2;

while size < limit and index <=argCount  do
    local value = ARGV[index]
    if value ~= nil then
        redis.call("LPUSH", key, value)
        size = redis.call("LLEN", key)
    end
    index = index+1
end

--返回列表长度
return size
