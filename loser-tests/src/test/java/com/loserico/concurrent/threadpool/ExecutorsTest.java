package com.loserico.concurrent.threadpool;

import org.junit.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 线程池测试
 */
public class ExecutorsTest {
    
    @Test
    public void testFixedThreadPool() {
        ExecutorService executorService = Executors.newFixedThreadPool(5);
        for (int i = 0; i < 5; i++) {
            executorService.execute(new RunTask());
        }
        executorService.shutdownNow(); //线程池进入STOP状态
        executorService.execute(new RunTask());
    }

    @Test
    public void testScheduledThreadPool() {
        ScheduledExecutorService es = Executors.newScheduledThreadPool(1);
    
        //延迟三秒执行
        es.schedule(new Runnable() {
            public void run() {
                System.out.println("我在跑......");
            }
        },3, TimeUnit.SECONDS);
    
        es.shutdown();
    }

}
