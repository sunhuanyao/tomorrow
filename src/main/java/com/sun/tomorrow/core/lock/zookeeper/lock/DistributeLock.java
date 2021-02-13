package com.sun.tomorrow.core.lock.zookeeper.lock;

import java.util.concurrent.TimeUnit;

public interface DistributeLock {

    boolean acquire(int timeout);

    boolean release();
}
