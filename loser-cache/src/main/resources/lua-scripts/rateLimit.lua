-- 返回0表示超出限制
local times = redis.call('incr', KEYS[1])

if times == 1 then
  -- KEYS[1] 刚刚创建，所以为其设置生存空间
  redis.call('expire', KEYS[1], ARGV[1])
end

if times > tonumber(ARGV[2]) then
  return 0
end

return 1