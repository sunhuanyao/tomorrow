package com.sun.tomorrow.core.lock.zookeeper;


import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

/**
 * 非阻塞 分布式 zk 锁
 */
public class ZkLockBlocklessImpl extends ZkLock{

    public ZkLockBlocklessImpl(ZooKeeper zooKeeper) {
        super(zooKeeper);
    }

    @Override
    public boolean lock(String lockName, String client) throws KeeperException, InterruptedException {
        Stat result = getClient().exists(lockName, false);
        if (result == null) {
            getClient().create(lockName, client.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
            byte[] data = getClient().getData(lockName, false, null);
            return data != null && client.equals(new String(data));
        }
        return false;
    }

    @Override
    public boolean unlock(String lockName, String client) throws KeeperException, InterruptedException {
        Stat stat = new Stat();
        byte[] bytes = getClient().getData(lockName, false, stat);
        if( client!= null && client.equals(new String(bytes))) {
            getClient().delete(lockName, stat.getVersion());
            return true;
        }
        return false;
    }

    @Override
    public boolean isLock(String lockName) throws KeeperException, InterruptedException {
        Stat stat = getClient().exists(lockName, false);
        return stat != null;
    }
}
