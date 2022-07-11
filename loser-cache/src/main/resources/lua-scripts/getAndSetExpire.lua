local function getAndSetExpire(key, expires) 
    if(redis.call("EXISTS", key ) == 1 ) then
        redis.call("expire", key, expires)
    end
    return redis.call("GET", key)
end

local key = KEYS[1]
local expires = ARGV[1]
return getAndSetExpire(key, expires) 
