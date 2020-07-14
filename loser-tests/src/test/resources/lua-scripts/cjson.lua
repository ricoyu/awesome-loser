--[[
The Redis Lua interpreter loads seven libraries: 
  base, table, string, math, debug, cjson, and cmsgpack. 
  
The first several are standard libraries that allow you to do the basic operations you’d expect from any language. 
The last two let Redis understand JSON and MessagePack.

执行：
  redis-cli set apple '{ "color": "red", "type": "fruit" }'
  # OK
  
  redis-cli --eval json-get.lua apple , type
  # "fruit"
]]--
if redis.call("EXISTS", KEYS[1]) == 1 then
  local payload = redis.call("GET", KEYS[1])
  --return cjson.decode(payload)
  return cjson.decode(payload)[ARGV[1]]
else
  return nil
end