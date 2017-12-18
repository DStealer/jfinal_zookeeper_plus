package com.kuark.jfzk.demo.locks;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

public class UniqueLockTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(UniqueLockTest.class);
    private ZooKeeper zooKeeper;

    @Before
    public void setUp() throws Exception {
        zooKeeper = new ZooKeeper("127.0.0.1:2181", 1000, new Watcher() {
            @Override
            public void process(WatchedEvent event) {
                LOGGER.info("Zookeeper event path:{} type:{} state:{} ", event.getPath(), event.getType(), event.getState());
            }
        });
    }

    @Test
    public void tt01() throws Exception {
        UniqueLock lock = new UniqueLock(zooKeeper, "abc");
        lock.callback = new LockListener() {
            @Override
            public void lockAcquired() {
                System.out.println("lock ... acquired");
            }

            @Override
            public void lockReleased() {
                System.out.println("lock ... acquired");
            }
        };
        lock.lock();
        TimeUnit.SECONDS.sleep(30);
        lock.unlock();
    }

    @After
    public void tearDown() throws Exception {
        zooKeeper.close();
    }
}