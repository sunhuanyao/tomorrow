package com.sun.tomorrow.core.lock.zookeeper.lock;

import com.sun.tomorrow.core.lock.zookeeper.ZookeeperClient;
import com.sun.tomorrow.core.lock.zookeeper.util.ZkUtils;
import com.sun.tomorrow.core.util.exception.BusinessException;
import org.apache.kafka.common.protocol.types.Field;
import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class SimpleDistributeImpl implements DistributeLock {

    private ZooKeeper zooKeeper;

    private static final String PRIFIX = "[SimpleDistributeImpl] ";

    private String base_path;
    private String current;
    private volatile boolean islock = false;

    private static final Logger LOG = LoggerFactory.getLogger(SimpleDistributeImpl.class);

    public SimpleDistributeImpl(String lockName) throws KeeperException, InterruptedException {
        zooKeeper = ZookeeperClient.getInstance().getClient();
        base_path = "/lock/" + lockName;
        ZkUtils.createPath(zooKeeper, base_path);
    }

    @Override
    public boolean acquire(int timeout) {
        try {
            long startTime = System.currentTimeMillis();
            current = zooKeeper.create(base_path + "/node", "lock".getBytes(),
                        ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
                // 获取排序的节点
            List<String> nodes = getSortedNodes(base_path);
            System.out.println(nodes);
            String nodeNow = current.substring(current.lastIndexOf('/') + 1);
            int index = nodes.indexOf(nodeNow);
            if (index == 0) {
                return true;
            }
            if (index < 0) {
                throw new BusinessException("zk lock failed.");
            }
            CountDownLatch count = new CountDownLatch(1);
            String preNode = nodes.get(index - 1);
//            System.out.println(current + " -- pre:" + preNode);
            zooKeeper.exists(base_path + "/" + preNode, new Watcher() {
                @Override
                public void process(WatchedEvent event) {
                    System.out.println("-----" + event);
                    switch (event.getType()) {
                        case NodeDeleted:
//                            System.out.println("deleted.");
                            count.countDown();
                            break;
                        default:
                            break;
                    }
                }
            });

            count.await();

        } catch (KeeperException ex) {
            LOG.error("create path error");
        } catch (InterruptedException ex) {
            LOG.error("" + ex);
        }
        return false;
    }

    @Override
    public boolean release() {
        try {
            Stat stat = zooKeeper.exists(current, false);
            if(stat!=null) {
                zooKeeper.delete(current, stat.getVersion());
                return true;
            }
            return false;
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (KeeperException e) {
            e.printStackTrace();
        }
        return false;
    }

    private List<String> getSortedNodes(String nameSpace) {
        List<String> nodes = new ArrayList<>();
        try {
            nodes = zooKeeper.getChildren(nameSpace, false);
            Collections.sort(nodes);

        }catch (KeeperException ex) {
            LOG.error("{} get sorted node exception: {}", PRIFIX, ex);
        }catch (InterruptedException e) {
            LOG.error("{} intteruprted error:{}", PRIFIX, e);
        }
        return nodes;
    }

}
