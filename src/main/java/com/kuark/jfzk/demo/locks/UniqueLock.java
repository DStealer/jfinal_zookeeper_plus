package com.kuark.jfzk.demo.locks;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 全局唯一锁
 * 本例锁的形式为 lock path(base dir + key)
 */
public class UniqueLock extends LockSupport implements Lock {
    private static final Logger LOGGER = LoggerFactory.getLogger(UniqueLock.class);

    public UniqueLock(ZooKeeper zookeeper, String key) {
        super(zookeeper, key, null);
    }

    public UniqueLock(ZooKeeper zookeeper, String key, LockListener callback) {
        super(zookeeper, key, callback);
    }

    @Override
    public synchronized void lock() throws Exception {
        if (this.zookeeper.getState().isAlive()) {
            this.lockId = super.retryOperation(() -> {
                String lockPath = getLockPath();
                try {
                    //创建节点，尝试锁定
                    return zookeeper.create(lockPath, null, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
                } catch (KeeperException.NodeExistsException e) { //如果节点存在
                    Stat stat = zookeeper.exists(lockPath, false);
                    if (stat != null) {
                        if (zookeeper.getSessionId() == stat.getEphemeralOwner()) {
                            return lockPath;
                        } else {
                            throw new RetryException("Owner:" + stat.getEphemeralOwner() + " is not me");
                        }
                    } else {
                        throw new RetryException("Invalid stat");
                    }
                } catch (InterruptedException | KeeperException.ConnectionLossException | KeeperException.OperationTimeoutException e) {
                    throw new RetryException(e);
                }
            }, Integer.MAX_VALUE);
            if (this.callback != null) {
                this.callback.lockAcquired();
            }
        } else {
            throw new IllegalStateException("Zookeeper may be dead");
        }
    }

    @Override
    public synchronized boolean tryLock() throws Exception {
        if (this.zookeeper.getState().isAlive()) {
            try {
                this.lockId = super.retryOperation(() -> {
                    String lockPath = getLockPath();
                    try {
                        //创建节点，尝试锁定
                        return zookeeper.create(lockPath, null, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
                    } catch (KeeperException.NodeExistsException e) {//如果节点存在
                        Stat stat = zookeeper.exists(lockPath, false);
                        if (stat != null) {
                            if (zookeeper.getSessionId() == stat.getEphemeralOwner()) {
                                return lockPath;
                            } else {
                                throw new RetryException("Owner:" + zookeeper.getSessionId() + " is not me");
                            }
                        } else {
                            throw new RetryException("Invalid stat");
                        }
                    } catch (InterruptedException | KeeperException.ConnectionLossException | KeeperException.OperationTimeoutException e) {
                        throw new RetryException(e);
                    }
                }, 5);
                if (this.callback != null) {
                    callback.lockAcquired();
                }
                return true;
            } catch (Exception e) {
                LOGGER.error("Caught:{}", e.getMessage(), e);
                return false;
            }
        } else {
            LOGGER.debug("Zookeeper may be dead");
            return false;
        }
    }

    @Override
    public synchronized void unlock() {
        if (this.zookeeper.getState().isAlive()) {
            try {
                super.retryOperation((ZkOp<Void>) () -> {
                    try {
                        zookeeper.delete(getLockId(), -1);
                    } catch (KeeperException.NodeExistsException e) {
                        //do nothing
                    } catch (KeeperException.ConnectionLossException | KeeperException.OperationTimeoutException e) {
                        throw new RetryException(e);
                    }
                    return null;
                }, 5);
                if (this.callback != null) {
                    this.callback.lockReleased();
                }
            } catch (Exception e) {
                LOGGER.error("Caught:{}", e.getMessage(), e);
            }
        } else {
            LOGGER.debug("Zookeeper may be dead");
        }
    }

    @Override
    public synchronized String getLockId() {
        return this.lockId;
    }

    public String getLockPath() {
        return LOCK_DIR + "/" + this.key;
    }
}
