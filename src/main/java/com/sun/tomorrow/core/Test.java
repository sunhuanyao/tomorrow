package com.sun.tomorrow.core;

import com.sun.tomorrow.core.base.Rectangle;
import com.sun.tomorrow.core.lock.zookeeper.ZkLock;
import com.sun.tomorrow.core.lock.zookeeper.ZkLockBlocklessImpl;
import com.sun.tomorrow.core.lock.zookeeper.ZookeeperClient;
import com.sun.tomorrow.core.service.ExecutorLocalService;
import com.sun.tomorrow.core.tool.base.BaseEntity;
import com.sun.tomorrow.core.tool.base.Heap;
import org.apache.curator.RetryPolicy;
import org.apache.curator.RetrySleeper;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.imps.CuratorFrameworkImpl;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooKeeper;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @Author roger sun
 * @Date 2019/11/7 16:21
 */
public class Test {


    private int n;
    public Test(int n){
        this.n = n;
    }
    private Lock lock = new ReentrantLock();
    private Condition condition  = lock.newCondition();

//    private  boolean ok = false;
    private volatile int ok = 0;
    private volatile int count = 1 ;



    public void task1(){
        while(count < this.n) {
            while(ok != 0) {
//                System.out.print("task1");
                Thread.yield();
            }
            System.out.print(0);
            ok = 1;

        }
    }

    public void task2(){
        while(count <= this.n){
            while(count % 2 == 0 || ok != 1){
//                System.out.print("task2");
                Thread.yield();
            }
            System.out.print(count ++ );
            ok = 0;

        }
    }

    public void task3(){
        while(count <= this.n){
            while(count % 2 != 0 || ok != 2){
//                System.out.print("task3");
                Thread.yield();
            }
            System.out.print(count ++ );
            ok = 0;

        }

    }


    static int op = 1;
    public static void main(String[] args) throws Exception {


//        ExecutorLocalService executorLocalService = new ExecutorLocalService(2);
//
//        executorLocalService.doInvoke(Test.class, "task", int.class, 4);

//        ZkLock zkLock = new ZkLockBlocklessImpl(ZookeeperClient.getInstance().getClient());
        String lockname = "/lock1";
//
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
        Executor executor = new ThreadPoolExecutor(10,
                20, 10, TimeUnit.SECONDS, new ArrayBlockingQueue<>(100));
        CuratorFramework curatorFramework = CuratorFrameworkFactory.newClient("127.0.0.1:2181", retryPolicy);
        curatorFramework.start();
        InterProcessMutex lock = new InterProcessMutex(curatorFramework, lockname);
        CountDownLatch countDownLatch = new CountDownLatch(10);
        for(int i = 0; i < 100; ++ i) {
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    while (true) {
                        try {
                            System.out.println("start.");
                            if(lock.acquire(1, TimeUnit.SECONDS)){
                                System.out.println(op++);
                                lock.release();
                                break;
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        } finally {
                            if(lock.isAcquiredInThisProcess()) {
                                try {
                                    lock.release();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }

                    }
                    countDownLatch.countDown();
                }
            });
        }
        countDownLatch.await();
        System.out.println(op);

    }

}
