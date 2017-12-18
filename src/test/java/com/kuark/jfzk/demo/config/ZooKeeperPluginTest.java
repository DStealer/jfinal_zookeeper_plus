package com.kuark.jfzk.demo.config;

import org.apache.zookeeper.*;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.concurrent.TimeUnit;

public class ZooKeeperPluginTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(ZooKeeperPluginTest.class);

    @Test
    public void tt1() throws Exception {
        ZooKeeper keeper = new ZooKeeper("127.0.0.1:2181", 1000, new Watcher() {
            public void process(WatchedEvent event) {
                System.out.println("======" + event);
            }
        });
        String targetPath = String.format("/%s@%s", "test", "0.0.1");
        System.out.println(keeper.create(targetPath, null, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.CONTAINER));
        TimeUnit.MINUTES.sleep(1);
        keeper.close();
    }

    @Test
    public void tt2() throws Exception {
        ZooKeeper keeper = new ZooKeeper("127.0.0.1:2181", 1000, new Watcher() {
            public void process(WatchedEvent event) {
                System.out.println("======" + event);
            }
        });
        keeper.getChildren("/usr@0.0.1", false).stream().map(c -> {
            try {
                return URLDecoder.decode(c, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                return c;
            }
        }).forEach(System.out::println);

        keeper.close();
    }

    @Test
    public void tt3() throws Exception {
        ZooKeeper keeper = new ZooKeeper("127.0.0.1:2181", 1000, new Watcher() {
            public void process(WatchedEvent event) {
                LOGGER.debug("Default watcher:{} zookeeper event path:{} type:{} state:{} ", this, event.getPath(), event.getType(), event.getState());
            }
        });
        Watcher watcher = new Watcher() {
            public void process(WatchedEvent event) {
                LOGGER.debug("Watcher:{} zookeeper event path:{} type:{} state:{} ", this, event.getPath(), event.getType(), event.getState());
            }
        };
        keeper.getChildren("/abc", watcher);
        keeper.getChildren("/abc", watcher);
        keeper.getChildren("/abc", watcher);
        keeper.getChildren("/abc", watcher);
        TimeUnit.MINUTES.sleep(10L);

    }
}