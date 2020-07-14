local set = redis.call("setnx", KEYS[1], ARGV[1])

if set == 1 then
  redis.call('expire', KEYS[1], ARGV[2])
end

return set  
  