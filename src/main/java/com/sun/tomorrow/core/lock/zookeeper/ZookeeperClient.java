package com.sun.tomorrow.core.lock.zookeeper;

import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.ho.yaml.Yaml;
import org.ho.yaml.YamlConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

public class ZookeeperClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(ZookeeperClient.class);

    private static final String INFO_NAME = "[ZookeeperClient]";

    private static CountDownLatch countDownLatch = new CountDownLatch(1);

    public static ZookeeperClient getInstance() {
        return Holder.instance;
    }

    private static class Holder {
        public static ZookeeperClient instance = new ZookeeperClient();
    }

    private ZooKeeper zooKeeper;

    private ZookeeperClient() {
        init();
    }

    public void init() {
        Watcher watcher = event -> {
            LOGGER.info(INFO_NAME + "started to watch");
            switch (event.getState()){
                case SyncConnected:
                    LOGGER.info(INFO_NAME + "SyncConnected.");
                    countDownLatch.countDown();
                    break;
                case Closed:
                    LOGGER.info(INFO_NAME + "closed.");
                    break;
                default:
                    LOGGER.info(INFO_NAME + "test.");
            }
            LOGGER.info(INFO_NAME + "end.");
        };
        try {
            LOGGER.info(INFO_NAME + "start to connect zookeeper.");
            this.zooKeeper = new ZooKeeper("127.0.0.1:2181", 2000, watcher);
            LOGGER.info(INFO_NAME + "end to connect zookeeper.");

            countDownLatch.await();
        } catch (IOException | InterruptedException ex) {
            LOGGER.error(INFO_NAME + " happens:", ex);
        }

    }

    public ZooKeeper getClient() {
        return this.zooKeeper;
    }

    public static void main(String[] args) throws Exception {


    }

}
