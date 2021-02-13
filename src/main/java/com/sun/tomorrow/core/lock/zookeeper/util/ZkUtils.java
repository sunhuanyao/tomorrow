package com.sun.tomorrow.core.lock.zookeeper.util;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.rmi.runtime.Log;

public class ZkUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(ZkUtils.class);

    public static boolean createPath(ZooKeeper zooKeeper, String path) {
        try {
            if(path.length() == 0) {
                return true;
            }
            if (zooKeeper.exists(path, false) == null) {
                createPath(zooKeeper, path.substring(0, path.lastIndexOf('/')));
                zooKeeper.create(path, "".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
            }
            return true;

        } catch (KeeperException ex) {
            LOGGER.error("ZkUtils keeper error:", ex);
        } catch (InterruptedException ex) {
            LOGGER.error("ZkUtils interrupt error:", ex);
        }
        return false;
    }

}
