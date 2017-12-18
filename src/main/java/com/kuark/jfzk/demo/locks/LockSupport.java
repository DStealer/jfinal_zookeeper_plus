package com.kuark.jfzk.demo.locks;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooKeeper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

public abstract class LockSupport {
    private static final Logger LOGGER = LoggerFactory.getLogger(LockSupport.class);
    protected static String LOCK_DIR = "";
    protected ZooKeeper zookeeper;
    protected String key;
    protected LockListener callback;
    protected String lockId;

    public LockSupport(ZooKeeper zookeeper, String key, LockListener callback) {
        if (zookeeper == null || !zookeeper.getState().isAlive()) {
            throw new IllegalStateException("Zookeeper status is illegal");
        }
        if (key == null || key.isEmpty()) {
            throw new IllegalStateException("Key is illegal");
        }
        this.zookeeper = zookeeper;
        this.key = key;
        this.callback = callback;
    }

    /**
     * 重试操作
     *
     * @param op
     * @param <T>
     * @param retryCount if Integer.MAX_VALUE never stop
     * @return
     * @throws Exception
     */
    protected <T> T retryOperation(ZkOp<T> op, int retryCount) throws Exception {
        if (retryCount != Integer.MAX_VALUE) {
            for (int i = 0; i < retryCount; i++) {
                try {
                    return op.execute();
                } catch (RetryException | InterruptedException e) {
                    LOGGER.debug("Attempt {} failed with connection loss so attempting to reconnect", i, e.getCause());
                    TimeUnit.SECONDS.sleep(ThreadLocalRandom.current().nextInt(10));
                } catch (Exception e) {
                    LOGGER.debug("Caught:{}", e.getMessage(), e);
                    throw e;
                }
            }
            throw new RetryException("Attempt out...");
        } else {
            while (true) {
                try {
                    return op.execute();
                } catch (RetryException | InterruptedException e) {
                    LOGGER.debug("Attempt failed with connection loss so attempting to reconnect", e);
                    TimeUnit.SECONDS.sleep(ThreadLocalRandom.current().nextInt(10));
                } catch (Exception e) {
                    LOGGER.debug("Caught:{}", e.getMessage(), e);
                    throw e;
                }
            }
        }
    }

    /**
     * zookeeper操作 默认有重试次数
     */
    protected interface ZkOp<T> {
        T execute() throws RetryException, KeeperException, InterruptedException, Exception;
    }

    /**
     * 可以重新操作的异常
     */
    protected class RetryException extends Exception {
        public RetryException(String message) {
            super(message);
        }

        public RetryException(Exception cause) {
            super(cause);
        }
    }
}
