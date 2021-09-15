--[[
调用方式：EVAL(script, 0, operate, username, token, expires, userDetails, authorities, loginInfo, singleSign)

实现功能：支持多点/单点登录, 指定时间内token自动过期
登录信息由5个map, 1个SET 1个ZSET 存储
  auth:token:username       field是token,    value是username
  auth:token:userdetails    field是token,    value是userdetails
  auth:token:authorities    field是token,    value是authorities
  auth:token:login:info     field是token,    value是额外的登录信息, 如设备号、手机操作系统, IP地址等等。使用场景如app登出提示
  auth:token:ttl            field是token,    value是token过期时间, refresh token的时候取这个值作为新的过期时间
  
每个用户1个SET 
  auth:admin:token      存admin用户对应的token, 如果可以多地登录, 那么SET里面就会有多个token
  auth:operator:token   存operator用户对应的token
  
1个zset  
  auth:token:ttl:zset       放token和token到期timestamp, zset类型, score是timestamp
]]

local AUTH_TOKEN_USERNAME_HASH = "auth:token:username"
local AUTH_TOKEN_USERDETAILS_HASH = "auth:token:userdetails"
local AUTH_TOKEN_AUTHORITIES_HASH = "auth:token:authorities"
local AUTH_TOKEN_LOGIN_INFO_HASH = "auth:token:login:info"
local AUTH_TOKEN_TTL_HASH = "auth:token:ttl"
local AUTH_TOKEN_TTL_ZSET = "auth:token:ttl:zset"

--token过期时发布的频道
local AUTH_TOKEN_EXPIRE_CHANNEL = "auth:token:expired:channel"
--用户异地登录时踢掉前一个登录时发布的频道, 即单点登录下线通知
local AUTH_SINGLE_SIGNON_CHANNEL = "auth:single:signon:channel"
--用户退出登录时发布的频道
local AUTH_LOGOUT_CHANNEL = "auth:logout:channel"
--用户成功登录后发布的频道
local AUTH_LOGIN_CHANNEL = "auth:user:login:channel"

local OPERATE_LOGIN = "login" -- 登录
local OPERATE_LOGOUT = "logout" -- 登出
local OPERATE_AUTH = "auth" -- 根据token认证
local OPERATE_CLEAR_EXPIRED = "clearExpired" --清除已过期的token
local OPERATE_IS_LOGINED = "isLogined" --根据username判断是否已登录

--[[
  返回系统当前的时间戳, 毫秒
]]
local currentTimestamp = function() 
    return redis.call("TIME")[1] * 1000
end

--[[
  把token添加进auth:token:ttl:zset这个ZSET中, 同时设置其score 如果token在这个ZSET中已经存在, 那么更新其score
  score是过期时间点, 比如token ttl 30*60*1000 毫秒, 那么ZSET score 为系统当前时间戳timestmap+ttl
]]
local setExpires = function(token, expires)
    -- 默认一年过期
    -- 当前毫秒数+过期毫秒数
    local milisYear = 31536000000
    -- 表示没有传过期参数, 那么默认一年过期
    if expires == "-1" then
        redis.call("ZADD", AUTH_TOKEN_TTL_ZSET, currentTimestamp() + milisYear, token)
    else
        redis.call("ZADD", AUTH_TOKEN_TTL_ZSET, currentTimestamp() + expires, token)
    end
end

local usernameTokenSet = function(username)
    return "auth:" .. username .. ":token"
end

--[[
    刷新token过期时间, 即重新用 timestmap+expires 更新ZSET的score
    达到的效果就是token过期时间往后延了expires毫秒
]]
local autoRefresh = function(token)
    local expires = redis.call("HGET", AUTH_TOKEN_TTL_HASH, token)
    -- 表示这个token设置了过期时间, expires是过期时间, 如30000
    if expires then
        setExpires(token, expires)
    end
end

--[[
专门负责清理
SET  auth:xxxuser:token
HASH auth:token:username
HASH auth:token:userdetails
HASH auth:token:authorities
HASH auth:token:login:info
HASH auth:token:ttl
ZSET auth:token:ttl:zset
]]
local doLogout = function(token)
    -- 先根据token拿username
    local username = redis.call("HGET", AUTH_TOKEN_USERNAME_HASH, token)

    -- 清理 SET auth:xxxuser:token
    redis.call("SREM", usernameTokenSet(username), token)

    -- 清理 auth:token:username
    redis.call("HDEL", AUTH_TOKEN_USERNAME_HASH, token)

    -- 清理 HASH auth:token:userdetails
    redis.call("HDEL", AUTH_TOKEN_USERDETAILS_HASH, token)

    -- 清理 HASH auth:token:authorities
    redis.call("HDEL", AUTH_TOKEN_AUTHORITIES_HASH, token)

    -- 清理 HASH auth:token:login:info
    redis.call("HDEL", AUTH_TOKEN_LOGIN_INFO_HASH, token)

    -- 清理 HASH auth:token:ttl
    redis.call("HDEL", AUTH_TOKEN_TTL_HASH, token)

    -- 清理 ZSET auth:token:ttl:zset
    redis.call("ZREM", AUTH_TOKEN_TTL_ZSET, token)
end

--[[
2.登出 返回true表示登出成功 false表示token不存在
- 根据token    从 auth:token:username 获取 username
- 根据username 删 auth:username:token
- 根据token    删 auth:token:userdetails
- 根据token    删 auth:token:authorities
- 根据token    删 auth:token:ttl
- 根据token    删 auth:token:ttl:zset

  通知用户登出成功
]]
local logout = function(token)
    local username = redis.call("HGET", AUTH_TOKEN_USERNAME_HASH, token)
    -- 表示这个token不存在
    if (not username) then
        return false
    end
    local loginInfo = redis.call("HGET", AUTH_TOKEN_LOGIN_INFO_HASH, token)
    -- 执行登出清理操作
    doLogout(token)

    local message = {}
    message["username"] = username
    message["token"] = token
    message["timestamp"] = currentTimestamp()
    message["loginInfo"] = loginInfo
    --publish一条消息, 方便后端程序记录用户的离线状态
    redis.call("PUBLISH", AUTH_LOGOUT_CHANNEL, cjson.encode(message))

    return true
end

--[[
    如果用户已经登录, 踢下线
]]
local kickOff = function(token)
    local username = redis.call("HGET", AUTH_TOKEN_USERNAME_HASH, token)
    -- 没有找到对应的username, 那么这个token应该已经过期被清理掉了
    if (not username) then
        return
    end

    doLogout(token)

    local message = {}
    message["username"] = username
    message["token"] = token
    message["timestamp"] = currentTimestamp()
    --publish一条消息, 方便后端程序记录用户的离线状态
    redis.call("PUBLISH", AUTH_SINGLE_SIGNON_CHANNEL, cjson.encode(message))
end

--[[
- 设置 auth:token:username
- 设置 auth:username:token
- 设置 auth:token:userdetails
- 设置 auth:token:authorities
- 设置 auth:token:ttl
- 塞入 auth:token:ttl:zset, score为token过期时间
]]
local doLogin = function(username, token, expires, userDetails, authorities, loginInfo)
    -- 设置SET, 将token丢到对应用户的SET中
    redis.call("SADD", usernameTokenSet(username), token)

    -- 设置HASH, 关联token和username
    redis.call("HSET", AUTH_TOKEN_USERNAME_HASH, token, username)

    -- 设置HASH, 关联token和userdetails
    redis.call("HSET", AUTH_TOKEN_USERDETAILS_HASH, token, userDetails)

    -- 设置HASH, 关联token和authorities
    redis.call("HSET", AUTH_TOKEN_AUTHORITIES_HASH, token, authorities)

    -- 设置HASH, token对应的loginInfo
    redis.call("HSET", AUTH_TOKEN_LOGIN_INFO_HASH, token, loginInfo)

    -- 设置HASH, 关联token和token的过期时间
    redis.call("HSET", AUTH_TOKEN_TTL_HASH, token, expires)

    -- 设置ZSET, 将当前的token和ttl丢进去
    -- 默认一年过期, score值是当前毫秒数+过期毫秒数
    setExpires(token, expires)
end

--[[
1.登录成功后, 客户端生成了新的token
- 根据   username 从   auth:username:token    获取之前用过的token
- 根据旧 token    删除  auth:token:username    中相应的field
- 根据旧 token    删除  auth:token:userdetails 中相应的field
- 根据旧 token    删除  auth:token:authorities 中相应的field
- 根据旧 token    删除  auth:token:ttl         中相应的field
- 根据旧 token    从    auth:token:ttl:zset    中删除
清理完毕

- 用新 token 设置 auth:token:username
- 用新 token 设置 auth:username:token
- 用新 token 设置 auth:token:userdetails
- 用新 token 设置 auth:token:authorities
- 用新 token 设置 auth:token:ttl
- 将新 token 塞入 auth:token:ttl:zset, score为token过期时间

expires 是过期的毫秒数
singleSign true  表示允许同一个账号多次登录
           false 表示同一个账号只能登录一次, 后续登录的会把前面登录的给踢掉
]]
local login = function(username, token, expires, userDetails, authorities, loginInfo, singleSignOn)
    -- 没有传username或者token参数?
    if (not username or not token) then
        return false
    end

    -- 单点登录的情况, 先取出这个username已经登录的token, 然后挨个踢下线
    if (singleSignOn == 'true') then
        local tokens = redis.call("SMEMBERS", usernameTokenSet(username))
        --循环tokens, 调用kickOff踢用户下线
        for i = 1, #tokens do
            kickOff(tokens[i])
        end
    end

    doLogin(username, token, expires, userDetails, authorities, loginInfo)

    --publish一条消息, 方便后端程序记录用户的登录行为
    local message = {}
    message["username"] = username
    message["token"] = token
    message["timestamp"] = currentTimestamp()
    redis.call("PUBLISH", AUTH_LOGIN_CHANNEL, cjson.encode(message))

    return true
end

--[[
   根据auth:token:ttl:zset的score, 与当前timestamp比较, score<timestamp表示过期了, 
   超过有效时间范围执行登出操作清理数据, 返回true(有token过期), 返回false(没有token过期)
   
 PUBLISH 消息
 ]]
local clearExpired = function()
    local timestamp = currentTimestamp()
    -- 拿score从负无穷大到currentTimestamp之间的元素
    local expiredTokens = redis.call("ZRANGEBYSCORE", AUTH_TOKEN_TTL_ZSET, "-inf", timestamp)
    -- 没有token过期
    if (#expiredTokens == 0) then
        return false
    end

--    redis.replicate_commands()
    for key, token in ipairs(expiredTokens) do
        local username = redis.call("HGET", AUTH_TOKEN_USERNAME_HASH, token)
        logout(token)
    
        local message = {}
        message["username"] = username
        message["token"] = token
        message["timestamp"] = timestamp
    
        redis.call("PUBLISH", AUTH_TOKEN_EXPIRE_CHANNEL, cjson.encode(message))
    end
    return true
end

--[[
3.验证token
- 根据提供的token从auth:token:username中取username，取不到则验证失败
- 返回username
]]
local auth = function(token, refresh)
    clearExpired() --清除过期的token
    
    -- 如果设置了自动刷新token
    if (refresh == "true") then
        redis.replicate_commands()
        autoRefresh(token)
    end
    
    return redis.call("HGET", AUTH_TOKEN_USERNAME_HASH, token)
end

--[[
  根据username检查该用户是否已登录
  根据提供的username从AUTH_USERNAME_TOKEN_HASH从取token，取不到则验证失败
- 返回true/false
]]
local isLogined = function(username, refresh)
    clearExpired() --清除过期的token
    
    -- 这个username当前登录的所有的token
    local tokens = redis.call("SMEMBERS", usernameTokenSet(username))

    -- 如果设置了自动刷新token
    if (refresh == "true") then
        redis.replicate_commands()
        for i=1, #tokens do
            autoRefresh(tokens[i])
        end
    end
    
    return #tokens ~= 0
end

local operate = ARGV[1]
if operate == OPERATE_LOGIN then
    local username = ARGV[2]
    local token = ARGV[3]
    local expires = ARGV[4]
    local userDetails = ARGV[5]
    local authorities = ARGV[6]
    local loginInfo = ARGV[7]
    local singleSignOn = ARGV[8]
    return login(username, token, expires, userDetails, authorities, loginInfo, singleSignOn)
elseif operate == OPERATE_LOGOUT then
    local token = ARGV[2]
    return logout(token)
elseif operate == OPERATE_AUTH then
    local token = ARGV[2]
    local refresh = ARGV[3]
    return auth(token, refresh)
elseif operate == OPERATE_CLEAR_EXPIRED then
    return clearExpired()
elseif operate == OPERATE_IS_LOGINED then
    local username = ARGV[2]
    local refresh = ARGV[3]
    return isLogined(username, refresh)
end
