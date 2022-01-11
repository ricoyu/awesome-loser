--[[
滑动时间窗口限流算法, 基于zset实现, memeber是一个随机数, score是请求到来的时间点, 毫秒数
每次请求, 传5个参数
1. name       一个能够标识业务意义的名字
2. member     一个随机数
3. score      客户端传入的当前的毫秒数, 代表请求的时间点
4. windowSize 时间窗口大小  也是毫秒数, 比如1000, 即从当前时间往前推1000毫秒, 这个时间窗口内有多少个member在
5. limitCount 限流值, 即在指定时间跨度内, 可以通过多少个请求

返回:
true  放行, 允许调用API
false 决绝, 不允许调用

每次放行, 都要把 -inf ~ (score - timeRange) 范围内的member给清除掉, 不然zset会不断膨胀
--]]

local KEY_PREFIX = "slading_window:zset:"

--[[
key        zset key值
minScore   
--]]
local clearOverRanged = function(key, minScore) 
    redis.call("ZREMRANGEBYSCORE", key, "-inf", minScore)
end

--[[
    name        给这个zset起一个有业务意义的名字, 最终的key值是 KEY_PREFIX+key
    member     随机数, 没有什么意义, 就是要唯一
    score      客户端请求的时间戳
    windowSize 时间窗口大小
    limitCount 时间窗口内允许通过多少个请求
--]]
local shouldPass = function(name, member, score, windowSize, limitCount) 
    local key = KEY_PREFIX .. name
    local minScore = score - windowSize
    local members = redis.call("ZRANGE", key, minScore, score, "BYSCORE")
    
    local len=#members
    -- 请求数已经达到限流阈值, 不予放行 
    if #members >= limitCount then
        return false
    else
        -- 请求数还没有达到限流阈值, 先添加新member, 再清理数据, 最后放行
        redis.call("ZADD", key, score, member)
        clearOverRanged(key, minScore)
        return true
    end
end

local name = KEYS[1]
local member = ARGV[1]
local score = tonumber(ARGV[2])
local windowSize = tonumber(ARGV[3])
local limitCount = tonumber(ARGV[4])
return shouldPass(name, member, score, windowSize, limitCount)
