package com.sun.tomorrow.core.lock.zookeeper.masterTest;

import com.sun.tomorrow.core.lock.zookeeper.ZookeeperClient;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * 模拟选举实战：
 * 3个节点，分别注册zkClient -> 服务发现；
 */
public class DoMaster {

    public static void main(String[] args) throws KeeperException, InterruptedException {
        Executor executor = new ThreadPoolExecutor(5, 10, 50, TimeUnit.SECONDS,  new ArrayBlockingQueue<>(10));
        List<WorkServer> workServers = new ArrayList<>();
        for(int i = 0 ; i < 3; ++ i) {
            workServers.add(new WorkServer(new WorkServer().new Node(i, "node-" + i)));
        }

        for(int i = 0 ; i< 3; ++ i) {
            executor.execute(workServers.get(i));
        }

        for(int i = 0 ; i < 1; ++ i) {
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    int op = 0;
                    while(true) {
                        workServers.get(op).running = false;

                        System.out.println(op + "节点断电了");

                        try {
                            Thread.sleep(5000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        workServers.get(op).running = true;
                        System.out.println(op + "节点恢复了");
                        op ++;

                        op %= 3;


                    }
                }
            });
        }
        executor.execute(new Runnable() {
            @Override
            public void run() {
                while(true) {
                    ZooKeeper zooKeeper = ZookeeperClient.getInstance().getClient();
                    try {
                        Thread.sleep(1000);
                        byte[] res = zooKeeper.getData(WorkServer.masterNodePath, true, null);
                        System.out.println("当前主节点是：" + new String(res));

                    } catch (KeeperException e) {
                        System.out.println("当前没有主节点！");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            }
        });

//        ZooKeeper zooKeeper = ZookeeperClient.getInstance().getClient();
//        Stat stat = zooKeeper.exists(WorkServer.masterNodePath, true);
//        System.out.println(stat);

    }




}
