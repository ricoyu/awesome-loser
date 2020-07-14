package com.loserico.cache.concurrent;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

/**
 * 支持设置过期时间及TTL操作
 * <p>
 * Copyright: Copyright (c) 2018-05-19 21:18
 * <p>
 * Company: DataSense
 * <p>
 * @author Rico Yu	ricoyu520@gmail.com
 * @version 1.0
 * @on
 */
public interface Expirable {

    /**
     * Set a timeout for object. After the timeout has expired,
     * the key will automatically be deleted.
     *
     * @param timeToLive - timeout before object will be deleted
     * @param timeUnit - timeout time unit
     * @return <code>true</code> if the timeout was set and <code>false</code> if not
     */
    boolean expire(long timeToLive, TimeUnit timeUnit);

    /**
     * Set an expire date for object. When expire date comes
     * the key will automatically be deleted.
     *
     * @param timestamp - expire date in milliseconds (Unix timestamp)
     * @return <code>true</code> if the timeout was set and <code>false</code> if not
     */
    boolean expireAt(long timestamp);

    /**
     * Set an expire date for object. When expire date comes
     * the key will automatically be deleted.
     *
     * @param localDateTime - expire date
     * @return <code>true</code> if the timeout was set and <code>false</code> if not
     */
    boolean expireAt(LocalDateTime localDateTime);

    /**
     * Clear an expire timeout or expire date for object.
     *
     * @return <code>true</code> if timeout was removed
     *         <code>false</code> if object does not exist or does not have an associated timeout
     */
    boolean clearExpire();

    /**
     * Remaining time to live of Redisson object that has a timeout 
     *
     * @return time in milliseconds
     *          -2 if the key does not exist.
     *          -1 if the key exists but has no associated expire.
     */
    long remainTimeToLive();

}