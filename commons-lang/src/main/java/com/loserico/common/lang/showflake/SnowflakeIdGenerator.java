package com.loserico.common.lang.showflake;

/**
 * 雪花算法（Snowflake Algorithm）是 Twitter 开源的一种分布式ID生成算法，它可以生成全局唯一且有序的ID。一个典型的雪花算法生成的ID是一个64位的整数，结构如下：
 * <pre> {@code
 * | 1 bit | 41 bits | 10 bits | 12 bits |
 * |-------|---------|---------|---------|
 * | sign  |  timestamp | machine ID | sequence |
 * }</pre>
 * <p/>
 * 1 bit: 符号位，固定为0，表示正数。<br/>
 * 41 bits: 时间戳，表示从某个起始时间到当前时间的毫秒数。<br/>
 * 10 bits: 机器ID，表示生成ID的机器或节点。<br/>
 * 12 bits: 序列号，表示同一毫秒内生成的多个ID。<br/>
 * <p/>
 * Copyright: Copyright (c) 2025-01-24 15:26
 * <p/>
 * Company: Sexy Uncle Inc.
 * <p/>

 * @author Rico Yu  ricoyu520@gmail.com
 * @version 1.0
 */
public class SnowflakeIdGenerator {

	// 起始的时间戳（可以设置为系统开始使用的时间）
	private final static long START_TIMESTAMP = 1609459200000L; // 2021-01-01 00:00:00

	// 每一部分占用的位数
	private final static long SEQUENCE_BIT = 12; // 序列号占用的位数
	private final static long WORKER_BIT = 5;    // 机器ID占用的位数
	private final static long DATACENTER_BIT = 5; // 数据中心ID占用的位数

	// 每一部分的最大值
	private final static long MAX_SEQUENCE = ~(-1L << SEQUENCE_BIT);
	private final static long MAX_WORKER_NUM = ~(-1L << WORKER_BIT);
	private final static long MAX_DATACENTER_NUM = ~(-1L << DATACENTER_BIT);

	// 每一部分向左的位移
	private final static long WORKER_LEFT = SEQUENCE_BIT;
	private final static long DATACENTER_LEFT = SEQUENCE_BIT + WORKER_BIT;
	private final static long TIMESTAMP_LEFT = SEQUENCE_BIT + WORKER_BIT + DATACENTER_BIT;

	private long workerId;      // 机器ID
	private long datacenterId;  // 数据中心ID
	private long sequence = 0L; // 序列号
	private long lastTimestamp = -1L; // 上一次时间戳

	public SnowflakeIdGenerator(long datacenterId, long workerId) {
		if (datacenterId > MAX_DATACENTER_NUM || datacenterId < 0) {
			throw new IllegalArgumentException("Datacenter ID can't be greater than " + MAX_DATACENTER_NUM + " or less" +
                    " than 0");
		}
		if (workerId > MAX_WORKER_NUM || workerId < 0) {
			throw new IllegalArgumentException("Worker ID can't be greater than " + MAX_WORKER_NUM + " or less than " +
                    "0");
		}
		this.datacenterId = datacenterId;
		this.workerId = workerId;
	}

	/**
	 * 生成下一个ID
	 */
	public synchronized long nextId() {
		long currentTimestamp = getCurrentTimestamp();

		if (currentTimestamp < lastTimestamp) {
			throw new RuntimeException("Clock moved backwards. Refusing to generate id for " + (lastTimestamp - currentTimestamp) + " milliseconds");
		}

		if (currentTimestamp == lastTimestamp) {
			// 同一毫秒内，序列号自增
			sequence = (sequence + 1) & MAX_SEQUENCE;
			if (sequence == 0) {
				// 同一毫秒内序列号已经用完，等待下一毫秒
				currentTimestamp = getNextTimestamp(lastTimestamp);
			}
		} else {
			// 不同毫秒内，序列号重置
			sequence = 0L;
		}

		lastTimestamp = currentTimestamp;

		// 时间戳部分 | 数据中心ID部分 | 机器ID部分 | 序列号部分
		return ((currentTimestamp - START_TIMESTAMP) << TIMESTAMP_LEFT)
				| (datacenterId << DATACENTER_LEFT)
				| (workerId << WORKER_LEFT)
				| sequence;
	}

	/**
	 * 获取当前时间戳（毫秒）
	 */
	private long getCurrentTimestamp() {
		return System.currentTimeMillis();
	}

	/**
	 * 获取下一毫秒的时间戳
	 */
	private long getNextTimestamp(long lastTimestamp) {
		long currentTimestamp = getCurrentTimestamp();
		while (currentTimestamp <= lastTimestamp) {
			currentTimestamp = getCurrentTimestamp();
		}
		return currentTimestamp;
	}

    public static void main(String[] args) {
        SnowflakeIdGenerator idGenerator = new SnowflakeIdGenerator(1, 1); // 数据中心ID为1，机器ID为1

        long previousId = -1L; // 用于保存前一个ID
        int totalIds = 10000;  // 生成ID的总数
        long begin = System.currentTimeMillis();
        for (int i = 0; i < totalIds; i++) {
            long id = idGenerator.nextId();
            System.out.println("Generated ID: " + id);

            // 验证ID是否递增
            if (previousId >= id) {
                throw new RuntimeException("ID is not monotonically increasing! Previous ID: " + previousId + ", Current ID: " + id);
            }
            previousId = id;
        }
        long end = System.currentTimeMillis();
        System.out.print("Generated " + totalIds + " IDs in " + (end - begin) + " milliseconds.");

        System.out.println("All " + totalIds + " IDs are generated and verified to be monotonically increasing.");
    }
}