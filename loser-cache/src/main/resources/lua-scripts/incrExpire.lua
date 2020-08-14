-- 如果key已经存在则不重新设置过期时间
if redis.call("EXISTS", KEYS[1]) == 1 then
    return redis.call("INCR",KEYS[1])
else
    -- 说明是第一次incr, 设置其过期时间
    local value = redis.call("INCR",KEYS[1])
    redis.call("EXPIRE", KEYS[1], ARGV[1])
    return value;
end
