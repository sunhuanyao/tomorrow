package com.sun.tomorrow.core.lock.zookeeper.service;

import com.sun.tomorrow.core.lock.zookeeper.ZookeeperClient;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class Do {
    public static void main(String[] args) throws KeeperException, InterruptedException {
        List<Happened> happenedList = new ArrayList<>();
        //ZookeeperClient.getInstance().getClient().create("/node", "node".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        for(int i = 0; i < 3; ++ i) {
            happenedList.add(new Happened("node"+i, ZookeeperClient.getInstance().getClient()));
        }
        Executor executor = new ThreadPoolExecutor(5,5,10, TimeUnit.SECONDS, new ArrayBlockingQueue<>(10));

        for(int i = 0; i < 3; ++ i) {
            executor.execute(happenedList.get(i));
        }
        executor.execute(() -> {
            int op = 0;
            while(true){
                System.out.println(op + "开始掉线");
                happenedList.get(op).running = false;
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                happenedList.get(op).running = true;
                System.out.println(op + "开始回复");
                op++;
            }
        });
        executor.execute(()->{
            while(true) {
                try {
                    List<String> list = ZookeeperClient.getInstance().getClient().getChildren("/node", false);
                    System.out.println("--：" + list);

                } catch (KeeperException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
