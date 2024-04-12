package com.loserico.jvm.deadlock;

public class DeadlockDemo {
    // 定义两个资源
    private static final Object resource1 = new Object();
    private static final Object resource2 = new Object();
    
    public static void main(String[] args) {
        // 线程1尝试先锁定resource1，然后锁定resource2
        Thread thread1 = new Thread(() -> {
            synchronized (resource1) {
                System.out.println("Thread 1: Locked resource 1");
                
                try {
                    // 为了使死锁的效果更明显，这里使线程睡眠一段时间
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                
                synchronized (resource2) {
                    System.out.println("Thread 1: Locked resource 2");
                }
            }
        });
        
        // 线程2尝试先锁定resource2，然后锁定resource1
        Thread thread2 = new Thread(() -> {
            synchronized (resource2) {
                System.out.println("Thread 2: Locked resource 2");
                
                try {
                    // 为了使死锁的效果更明显，这里使线程睡眠一段时间
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                
                synchronized (resource1) {
                    System.out.println("Thread 2: Locked resource 1");
                }
            }
        });
        
        // 启动两个线程
        thread1.start();
        thread2.start();
    }
}
