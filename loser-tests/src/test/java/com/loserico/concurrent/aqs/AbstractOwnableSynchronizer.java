package com.loserico.concurrent.aqs;

public abstract class AbstractOwnableSynchronizer implements java.io.Serializable {
    private static final long serialVersionUID = 3737899427754241961L;

    protected AbstractOwnableSynchronizer() { }

    /**
     * 独占模式同步器的当前持有线程. (即独占模式下记录了当前是哪个线程持有了锁)
     * transient关键字表示属性不参与序列化
     */
    private transient Thread exclusiveOwnerThread;

    protected final void setExclusiveOwnerThread(Thread thread) {
        exclusiveOwnerThread = thread;
    }

    protected final Thread getExclusiveOwnerThread() {
        return exclusiveOwnerThread;
    }
}
