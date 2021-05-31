--[[
调用方式：EVAL(script, 0, operate, username, token, expires, userDetails, authorities)

实现功能：单点登录, 指定时间内token自动过期
登录信息由4个map存储
  auth:token:username       field是token,    value是username
  auth:username:token       field是username, value是token 
  auth:token:userdetails    field是token,    value是userdetails
  auth:token:authorities    field是token,    value是authorities
  auth:token:login:info     field是token,    value是额外的登录信息, 如设备号、手机操作系统, IP地址等等。使用场景如app登出提示
  auth:token:ttl            field是token,    value是token过期时间, refresh token的时候取这个值作为新的过期时间
  
1个zset  
  auth:token:ttl:zset       放token和token到期timestamp, zset类型, score是timestamp
]]

local AUTH_USERNAME_TOKEN_HASH = "auth:username:token"
local AUTH_TOKEN_USERNAME_HASH = "auth:token:username"
local AUTH_TOKEN_USERDETAILS_HASH = "auth:token:userdetails"
local AUTH_TOKEN_AUTHORITIES_HASH = "auth:token:authorities"
local AUTH_TOKEN_LOGIN_INFO_HASH = "auth:token:login:info"
local AUTH_TOKEN_TTL_HASH = "auth:token:ttl"
local AUTH_TOKEN_TTL_ZSET = "auth:token:ttl:zset"

--token过期时发布的频道
local AUTH_TOKEN_EXPIRE_CHANNEL = "auth:token:expired"
--用户异地登录时踢掉前一个登录时发布的频道, 即单点登录下线通知
local AUTH_SINGLE_SIGNON_CHANNEL = "auth:single:signon:channel"
--用户退出登录时, 通知的频道
local AUTH_LOGOUT_CHANNEL = "auth:logout:channel"

local OPERATE_LOGIN = "login" -- 登录
local OPERATE_LOGOUT = "logout" -- 登出
local OPERATE_AUTH = "auth" -- 根据token认证
local OPERATE_CLEAR_EXPIRED = "clearExpired" --清除已过期的token
local OPERATE_USERNAME = "username" --根据token获取用户名
local OPERATE_IS_LOGINED = "isLogined" --根据username判断是否已登录

local setExpires = function(token, expires)
    -- 默认一年过期
    -- 当前毫秒数+过期毫秒数
    local milisYear = 31536000000;
    -- 表示没有传过期参数, 那么默认一年过期
    if expires == "-1" then
        redis.call("ZADD", AUTH_TOKEN_TTL_ZSET, redis.call("TIME")[1] * 1000 + milisYear, token)
    else
        redis.call("ZADD", AUTH_TOKEN_TTL_ZSET, redis.call("TIME")[1] * 1000 + expires, token)
    end
end

--刷新token过期时间
local autoRefresh = function(token)
    local expires = redis.call("HGET", AUTH_TOKEN_TTL_HASH, token)
    if expires then
        setExpires(token, expires)
    end
end

--[[
2.登出 返回1表示登出成功 0表示token不存在
- 根据token    从 auth:token:username 获取 username
- 根据username 删 auth:username:token
- 根据token    删 auth:token:userdetails
- 根据token    删 auth:token:authorities
- 根据token    删 auth:token:ttl
- 根据token    删 auth:token:ttl:zset

  doNotNotify 参数表示不需要PUBLIC一条消息通知客户端用户下线
]]
local logout = function(token, doNotNotify)
    local username = redis.call("HGET", AUTH_TOKEN_USERNAME_HASH, token)
    if(not username) then
        return cjson.encode(false)
    end
    redis.call("HDEL", AUTH_USERNAME_TOKEN_HASH, username)
    redis.call("HDEL", AUTH_TOKEN_USERNAME_HASH, token)
    redis.call("HDEL", AUTH_TOKEN_USERDETAILS_HASH, token)
    redis.call("HDEL", AUTH_TOKEN_AUTHORITIES_HASH, token)
    redis.call("HDEL", AUTH_TOKEN_LOGIN_INFO_HASH, token)
    redis.call("HDEL", AUTH_TOKEN_TTL_HASH, token)
    redis.call("ZREM", AUTH_TOKEN_TTL_ZSET, token)
    --publish一条消息, 方便后端程序记录用户的离线状态
    if(not doNotNotify) then
        redis.call("PUBLISH", AUTH_LOGOUT_CHANNEL, username);    
    end
    return cjson.encode(true)
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
]]
local login = function(username, token, expires, userDetails, authorities, loginInfo)
    local loginResult = {}

    if(not username or not token) then
        loginResult["success"] = false
        return loginResult
    else
        loginResult["success"] = true
    end
    local oldToken = redis.call("HGET", AUTH_USERNAME_TOKEN_HASH, username)

    redis.replicate_commands()
    if(oldToken) then
        local lastLoginInfo = redis.call("HGET", AUTH_TOKEN_LOGIN_INFO_HASH, oldToken)
        logout(oldToken, true)
        loginResult["lastLoginInfo"] = lastLoginInfo;

        local offlineInfo = {}
        offlineInfo[oldToken] = lastLoginInfo
        redis.call("PUBLISH", AUTH_SINGLE_SIGNON_CHANNEL, cjson.encode(offlineInfo))
    end

    redis.call("HSET", AUTH_USERNAME_TOKEN_HASH, username, token)
    redis.call("HSET", AUTH_TOKEN_USERNAME_HASH, token, username)
    redis.call("HSET", AUTH_TOKEN_USERDETAILS_HASH, token, userDetails)
    redis.call("HSET", AUTH_TOKEN_AUTHORITIES_HASH, token, authorities)
    redis.call("HSET", AUTH_TOKEN_LOGIN_INFO_HASH, token, loginInfo)
    redis.call("HSET", AUTH_TOKEN_TTL_HASH, token, expires)

    setExpires(token, expires)
    return cjson.encode(loginResult)
end

--[[
   根据auth:token:ttl:zset的score, 与当前timestamp比较, score<timestamp表示过期了, 
   超过有效时间范围执行登出操作清理数据, 返回expiredTokenLoginInfo
   返回一个数组[token, loginInfo, token2, loginInfo2...]
   
 PUBLISH 消息
 ]]
local clearExpired = function()
    local currentTimestamp = redis.call("TIME")[1] -- 得到的是秒: unix time in seconds
    -- 拿score从负无穷大到currentTimestamp之间的元素
    local expiredTokens = redis.call("ZRANGEBYSCORE", AUTH_TOKEN_TTL_ZSET, "-inf", currentTimestamp * 1000)

    local expiredTokenLoginInfo = {}
    -- 没有token过期
    if(#expiredTokens == 0) then
        return cjson.encode(expiredTokenLoginInfo)
    end

    redis.replicate_commands()
    for key, token in ipairs(expiredTokens) do
        -- loginInfo 是json字符串
        local loginInfo = redis.call("HGET", AUTH_TOKEN_LOGIN_INFO_HASH, token)
        logout(token)
        expiredTokenLoginInfo[token] = loginInfo
    end
    -- PUBLISH 只接受数字或者字符串，expiredTokenLoginInfo是table，所以转成json字符串
    local expiredTokenInfos = cjson.encode(expiredTokenLoginInfo);
    redis.call("PUBLISH", AUTH_TOKEN_EXPIRE_CHANNEL, expiredTokenInfos)
    return expiredTokenInfos
end

--[[
3.验证token
- 根据提供的token从auth:token:username中取username，取不到则验证失败
- 返回username
]]
local auth = function(token, refresh)
    clearExpired() --清除过期的token
    -- 如果设置了自动刷新token
    if(refresh == "true") then
        redis.replicate_commands()
        autoRefresh(token)
    end
    return redis.call("HGET", AUTH_TOKEN_USERNAME_HASH, token)
end

--[[
4.根据username检查改用户是否已登录
     根据提供的username从AUTH_USERNAME_TOKEN_HASH从取token，取不到则验证失败
- 返回token
]]
local isLogined = function(username)
    clearExpired() --清除过期的token
    return redis.call("HGET", AUTH_USERNAME_TOKEN_HASH, username)
end

local operate = ARGV[1]
if operate == OPERATE_LOGIN then
    local username = ARGV[2]
    local token = ARGV[3]
    local expires = ARGV[4]
    local userDetails = ARGV[5]
    local authorities = ARGV[6]
    local loginInfo = ARGV[7]
    return login(username, token, expires, userDetails, authorities, loginInfo)
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
    return isLogined(username)
end
