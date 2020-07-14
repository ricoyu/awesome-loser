redis.call('set', KEYS[1], ARGV[1])
return redis.call('expire', KEYS[1], ARGV[2])