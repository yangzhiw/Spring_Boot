package com.juzi.service.util;

import org.apache.log4j.Logger;

/**
 * 订单号生成-工具类
 * Package: com.riskmirror.channeldriver.utils Author : Eric Create : 15/9/22
 * 17:06 Email : mataojs@lianlian.com
 */

public class IdWorker {

    private final static long twepoch = 1288834974657L;
    private final static long workerIdBits = 4L;
    public final static long maxWorkerId = -1L ^ -1L << workerIdBits;
    private final static long sequenceBits = 10L;
    public final static long sequenceMask = -1L ^ -1L << sequenceBits;
    private final static long workerIdShift = sequenceBits;
    private final static long timestampLeftShift = sequenceBits + workerIdBits;
    public static Logger logger = Logger.getLogger(IdWorker.class);
    private static Long workerId = 1l;
    private static IdWorker idWorker = null;
    private long sequence = 0L;
    private long lastTimestamp = -1L;

    private IdWorker(final long workerId) {
        super();
        if (workerId > this.maxWorkerId || workerId < 0) {
            throw new IllegalArgumentException(String.format(
                "worker Id can't be greater than %d or less than 0",
                this.maxWorkerId));
        }
        this.workerId = workerId;
    }

    /**
     * 单实例初始化
     */
    public static IdWorker getInstance() {
        if (idWorker == null) {
            synchronized (IdWorker.class) {
                if (idWorker == null) {
                    int localWorkId = 3;
                    idWorker = new IdWorker(localWorkId);
                    logger.info("idWorker instance success, workId:" + localWorkId);
                }
            }
        }
        return idWorker;
    }

    public synchronized static IdWorker getInstance1() {
        if (idWorker == null) {
            int localWorkId = 3;
            idWorker = new IdWorker(localWorkId);
            logger.info("idWorker instance success, workId:" + localWorkId);
        }
        return idWorker;
    }

    @SuppressWarnings("static-access")
    public synchronized long nextId() {
        long timestamp = this.timeGen();
        if (this.lastTimestamp == timestamp) {
            this.sequence = (this.sequence + 1) & this.sequenceMask;
            if (this.sequence == 0) {
                logger.info("###########" + sequenceMask);
                timestamp = this.tilNextMillis(this.lastTimestamp);
            }
        } else {
            this.sequence = 0;
        }
        if (timestamp < this.lastTimestamp) {
            try {
                throw new Exception(
                    String.format(
                        "Clock moved backwards.  Refusing to generate id for %d milliseconds",
                        this.lastTimestamp - timestamp));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        this.lastTimestamp = timestamp;
        //logger.info("create serialnos----timestamp:" + timestamp
//				+ ",timestampLeftShift:" + timestampLeftShift + ",workerId:"
//				+ workerId + ",workerIdShift:" + workerIdShift + ",sequence:"
//				+ sequence);
        long nextId = ((timestamp - twepoch << timestampLeftShift))
            | (this.workerId << this.workerIdShift) | (this.sequence);
        return nextId;
    }

    private long tilNextMillis(final long lastTimestamp) {
        long timestamp = this.timeGen();
        while (timestamp <= lastTimestamp) {
            timestamp = this.timeGen();
        }
        return timestamp;
    }

    private long timeGen() {
        return System.currentTimeMillis();
    }

}
