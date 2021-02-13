package com.sun.tomorrow.core.lock.zookeeper.masterTest;

import com.sun.tomorrow.core.lock.zookeeper.ZookeeperClient;
import lombok.ToString;
import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

public class WorkServer implements Runnable {

    public ZooKeeper zooKeeper ;
    public static final String masterNodePath = "/master";
    public volatile boolean running;


    @ToString
    public class Node {
        int id;
        String name;

        public Node(int i, String s) {
            this.id = i;
            this.name = s;
        }
    }
    public Node name;



    public Watcher watcher;

    public WorkServer() {
    }

    public WorkServer(Node name) {
        this.zooKeeper = ZookeeperClient.getInstance().getClient();
        this.running = true;
        this.name = name;
        this.watcher = event -> {
            switch (event.getType()) {
                case None:
                    System.out.println("no changed.");
                    break;
                case NodeCreated:
                    System.out.println("the node is created.");
                    break;
                case NodeDataChanged:
                    System.out.println("the data is changed");
                    break;
                case NodeDeleted:
                    System.out.println("data is deleted.");
                    break;
                default:
                    System.out.println("other");
                    break;
            }
        };
    }

    @Override
    public void run() {
        while(true) {
            if (!running) {
                try {
                    relaseMaster(zooKeeper, name, watcher);
                } catch (KeeperException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
//                System.out.println(name + " is not running.");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                continue;
            }
            try {
                Stat stat = zooKeeper.exists(masterNodePath, true);
                if(stat == null) {
                    master(zooKeeper, name);
                }else{
                    byte[] bytes = zooKeeper.getData(masterNodePath, true, null);
                    if(name.toString().equals(new String(bytes))) {
//                        System.out.println("当前节点 is master:" + name);
                    }
                    Thread.sleep(1000);
                }
            } catch (InterruptedException | KeeperException e) {
                e.printStackTrace();
            }
        }
    }

    public static void master(ZooKeeper zk, Node data) throws InterruptedException {
        byte[] bytes = data.toString().getBytes();
        try {
            String res = zk.create(masterNodePath, bytes, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
//            System.out.println("抢master成功 :" +  res + " data:" + data.toString());
        } catch (KeeperException | InterruptedException ex){
            Thread.sleep(1000);
//            System.out.println("抢master 失败" + data);
            //ex.printStackTrace();
        } catch (Exception e) {
//            System.out.println("create path failure: " + data);
            Thread.sleep(1000);
        }
    }

    public static void relaseMaster(ZooKeeper zk, Node name, Watcher watcher) throws KeeperException, InterruptedException {

        Stat stat = zk.exists(masterNodePath, true);
        byte[] bytes = zk.getData(masterNodePath, true, null);
        if(!(new String(bytes).equals(name.toString()))){
//            System.out.println(name + " 不是主节点，没法释放");
            return ;
        }
//        System.out.println(name + " 释放主节点了");
        zk.delete(masterNodePath, stat.getVersion());

    }
}
