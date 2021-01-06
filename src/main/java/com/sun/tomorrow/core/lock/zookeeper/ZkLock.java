package com.sun.tomorrow.core.lock.zookeeper;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooKeeper;

public abstract class ZkLock {

    private ZooKeeper zooKeeper;

    public ZkLock(ZooKeeper zooKeeper) {
        this.zooKeeper = zooKeeper;
    }

    public ZooKeeper getClient(){
        return this.zooKeeper;
    }

    public abstract boolean lock(String lockName, String client) throws KeeperException, InterruptedException;

    public abstract boolean unlock(String lockName, String client) throws KeeperException, InterruptedException;

    public abstract boolean isLock(String lockName) throws KeeperException, InterruptedException;
}
