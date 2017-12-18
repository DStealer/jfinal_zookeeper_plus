package com.kuark.jfzk.demo.locks;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.stream.Collectors;

/**
 * 本例锁的形式为 lock path(basedir-key)-sequential
 * key = 锁
 * lockDir = 锁所在目录
 * lockPath=lockDir+'/'+key+'-';
 * lockId = lockPath+sequential
 */
public class DistributedLock extends LockSupport implements Lock {
    private static final Logger LOGGER = LoggerFactory.getLogger(DistributedLock.class);

    public DistributedLock(ZooKeeper zookeeper, String key) {
        super(zookeeper, key, null);
    }

    public DistributedLock(ZooKeeper zookeeper, String key, LockListener callback) {
        super(zookeeper, key, callback);
    }

    @Override
    public synchronized void lock() throws Exception {
        if (!lock(getLockDir(), getLockPath())) {
            throw new IllegalStateException("impossible status");
        } else {
            if (this.callback != null) {
                callback.lockAcquired();
            }
        }
    }

    @Override
    public synchronized boolean tryLock() throws Exception {
        if (this.zookeeper.getState().isAlive()) {
            boolean flag = super.retryOperation(() -> {
                try {
                    String lockDir = getLockDir();
                    String lockPath = getLockPath();
                    //查找或者创建锁标识
                    if (this.lockId == null) {
                        List<String> names = this.zookeeper.getChildren(lockDir, false);
                        Optional<String> name = names.stream().map(k -> lockDir + "/" + k)
                                .filter(k -> k.startsWith(lockPath)).findFirst();
                        if (name.isPresent()) {
                            LOGGER.debug("Found lockId:[{}] created last time", name.get());
                            this.lockId = name.get();
                        } else {
                            //创建节点
                            this.lockId = this.zookeeper.create(lockPath, null, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
                            LOGGER.debug("Create lockId:[{}]", this.lockId);
                        }
                    }
                    //到此应该已经存在lockId
                    if (this.lockId != null) {
                        List<String> names = this.zookeeper.getChildren(lockDir, false);
                        SortedSet<String> sortedNames = names.stream()
                                .map(name -> lockDir + "/" + name)
                                .filter(name -> name.startsWith(lockPath))
                                .collect(Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(o -> Long.valueOf(o.substring(lockPath.length()))))));
                        if (sortedNames.isEmpty()) {
                            LOGGER.warn("No children in: {} when we've just created one! Lets recreate it...", lockDir);
                            this.lockId = null;
                            throw new RetryException("Error stats for lockId");
                        } else {
                            SortedSet<String> lessThanMe = sortedNames.headSet(this.lockId);
                            //本节点是最小节点
                            return lessThanMe.isEmpty();
                        }
                    } else {
                        throw new RetryException("Error stats for lockId");
                    }
                } catch (InterruptedException | KeeperException.ConnectionLossException | KeeperException.OperationTimeoutException e) {
                    throw new RetryException(e);
                }
            }, 5);
            if (flag && this.callback != null) {
                this.callback.lockAcquired();
            }
            return flag;
        } else {
            LOGGER.debug("Zookeeper may be dead");
            return false;
        }
    }

    /**
     * 是否拥有者 <=> 是否拥有锁
     *
     * @param lockDir
     * @param lockPath
     * @return
     */
    private boolean lock(final String lockDir, final String lockPath) throws Exception {
        return super.retryOperation(() -> {
            try {
                //查找或者创建锁标识
                if (this.lockId == null) {
                    List<String> names = this.zookeeper.getChildren(lockDir, false);
                    Optional<String> name = names.stream().filter(k -> k.startsWith(lockPath)).findFirst();
                    if (name.isPresent()) {
                        LOGGER.debug("Found lockId:[{}] created last time", name.get());
                        this.lockId = name.get();
                    } else {
                        //创建节点
                        this.lockId = this.zookeeper.create(lockPath, null, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
                        LOGGER.debug("Create lockId:[{}]", this.lockId);
                    }
                }
                //到此应该已经存在lockId
                if (this.lockId != null) {
                    List<String> names = this.zookeeper.getChildren(lockDir, false);

                    SortedSet<String> sortedNames = names.stream()
                            .map(name -> lockDir + "/" + name)
                            .filter(name -> name.startsWith(lockPath))
                            .collect(Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(o -> Long.valueOf(o.substring(lockPath.length()))))));
                    if (sortedNames.isEmpty()) {
                        LOGGER.warn("No children in: {} when we've just created one! Lets recreate it...", lockDir);
                        this.lockId = null;
                        throw new RetryException("Error stats for lockId");
                    } else {
                        SortedSet<String> lessThanMe = sortedNames.headSet(lockId);
                        if (lessThanMe.isEmpty()) {//本节点是最小节点
                            return true; //最终出口
                        } else {   //非最小节点,注册watcher
                            String lastChildId = lessThanMe.last();
                            CountDownLatch latch = new CountDownLatch(1);
                            LOGGER.debug("watching less than me node:{}", lastChildId);
                            //查证前一个节点情况
                            //TODO 此处的通知是否保证一定会成功
                            Stat stat = this.zookeeper.exists(lastChildId, event -> {
                                try {
                                    LOGGER.debug("Zookeeper event path:{} type:{} state:{} ", event.getPath(), event.getType(), event.getState());
                                    switch (event.getState()) {
                                        case SyncConnected:
                                            switch (event.getType()) {
                                                case NodeDeleted: {
                                                    //前一个节点移除
                                                    latch.countDown();
                                                }
                                                break;
                                                default:
                                                    LOGGER.debug("I didn't care the type");
                                            }
                                            break;
                                        default:
                                            LOGGER.debug("I didn't care the state");
                                    }
                                } catch (Exception e) {
                                    LOGGER.error("Caught :{}", e.getMessage(), e);
                                }
                            });
                            //如果仍然存在，则同步等待移除
                            if (stat != null) {
                                latch.await();
                            } else {
                                //如果不存在，移除watcher，重新判断
                                this.zookeeper.removeAllWatches(lastChildId, Watcher.WatcherType.Any, false);
                                LOGGER.warn("Could not find the stats for less than me:{} ", lastChildId);
                            }
                            return lock(lockDir, lockPath);
                        }
                    }
                } else {
                    throw new RetryException("Error stats for lockId");
                }
            } catch (InterruptedException | KeeperException.ConnectionLossException | KeeperException.OperationTimeoutException e) {
                throw new RetryException(e);
            }
        }, Integer.MAX_VALUE);
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
            LOGGER.warn("Zookeeper may be dead");
        }
    }

    @Override
    public synchronized String getLockId() {
        return this.lockId;
    }

    public String getLockDir() {
        return LOCK_DIR;
    }

    public String getLockPath() {
        return LOCK_DIR + "/" + this.key + "-";
    }
}
