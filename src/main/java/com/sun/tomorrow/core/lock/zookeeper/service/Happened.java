package com.sun.tomorrow.core.lock.zookeeper.service;

import com.sun.tomorrow.core.lock.zookeeper.ZookeeperClient;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

public class Happened implements Runnable {

    public volatile boolean running;

    public String name;

    public static final String basePath = "/node/service-";

    public ZooKeeper zooKeeper;

    public Happened(String name, ZooKeeper zooKeeper) {
        this.running = true;
        this.name = name;
        this.zooKeeper = zooKeeper;
    }

    @Override
    public void run() {
        while(true) {
            if(!running) {
                try {
                    deleteService(basePath + name);

                } catch (InterruptedException | KeeperException e) {
//                    e.printStackTrace();
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                continue;
            }
            try {
                Stat stat =zooKeeper.exists(basePath + name, false);
                if(stat == null) {
                    String path = zooKeeper.create(basePath + name, name.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
                    System.out.println(name + " 注册成功：" + path);
                }

            } catch (KeeperException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
//                e.printStackTrace();
            } finally {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void deleteService(String path) throws KeeperException, InterruptedException {
        zooKeeper.delete(path, -1);
    }
}
