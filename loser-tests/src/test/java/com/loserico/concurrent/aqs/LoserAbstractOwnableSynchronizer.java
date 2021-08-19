package com.loserico.concurrent.aqs;

public abstract class LoserAbstractOwnableSynchronizer implements java.io.Serializable {
    private static final long serialVersionUID = 3737899427754241961L;

    protected LoserAbstractOwnableSynchronizer() { }

    /**
     * 记录当前持有锁的线程
     * 在unlock时候会检查一下想要释放锁的 线程 == exclusiveOwnerThread
     * 如果不等则抛异常, 因为线程只能释放自己加的锁, 其他线程加锁的是能释放
     */
    private transient Thread exclusiveOwnerThread;
    
    /**
     * 设置独占模式下当前持有锁的线程
     * @param thread
     */
    protected final void setExclusiveOwnerThread(Thread thread) {
        exclusiveOwnerThread = thread;
    }

    protected final Thread getExclusiveOwnerThread() {
        return exclusiveOwnerThread;
    }
}
