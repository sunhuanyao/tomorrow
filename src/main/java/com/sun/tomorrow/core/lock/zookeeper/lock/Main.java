package com.sun.tomorrow.core.lock.zookeeper.lock;

import org.apache.zookeeper.KeeperException;

import java.util.concurrent.*;

public class Main {

    private static int count = 0;


    public static void main(String[] args) throws KeeperException, InterruptedException {
        String lockname = "test";
        Executor executor = new ThreadPoolExecutor(10, 200, 100, TimeUnit.SECONDS, new ArrayBlockingQueue<>(10));
        CountDownLatch countDownLatch = new CountDownLatch(100);
        for(int i = 0 ; i < 100; ++ i) {
            final int op = i;
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        DistributeLock lock = new SimpleDistributeImpl(lockname);
                        System.out.println("in:" + op);
                        lock.acquire(1);
                        count ++;
                        lock.release();
                    } catch (KeeperException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    countDownLatch.countDown();
                }
            });

        }
        countDownLatch.await();
        System.out.println("count:" + count);

    }
}
