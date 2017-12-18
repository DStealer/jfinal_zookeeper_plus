package com.kuark.jfzk.demo.kit;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.jfinal.kit.LogKit;
import com.jfinal.kit.StrKit;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.jboss.netty.util.internal.ThreadLocalRandom;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * 获取节点信息
 */
public abstract class ZkKit {
    private static Logger LOGGER = LoggerFactory.getLogger(ZKPKit.class);
    private static ZooKeeper ZOO_KEEPER;
    private static LoadingCache<String, List<String>> GROUP_CACHE;
    private static String NODE_GROUP_PATH;
    private static String NODE_ID_PATH;

    /**
     * 初始化工具类
     * TODO :如果在加载的时候出异常该如何处理
     *
     * @param zooKeeper
     * @param nodeGroupPath
     * @param nodeIdPath
     */
    static void init(ZooKeeper zooKeeper, String nodeGroupPath, String nodeIdPath) {
        ZOO_KEEPER = zooKeeper;
        NODE_GROUP_PATH = nodeGroupPath;
        NODE_ID_PATH = nodeIdPath;

        Watcher nodeSyncWatcher = event -> {
            LOGGER.debug("Zookeeper event path:{} type:{} state:{} ", event.getPath(), event.getType(), event.getState());
            if (StrKit.notBlank(event.getPath()) && Watcher.Event.KeeperState.SyncConnected.equals(event.getState())) {
                switch (event.getType()) {
                    case NodeChildrenChanged:
                        GROUP_CACHE.refresh(event.getPath());
                        return;
                    default:
                        //do nothing
                }
            }
        };

        GROUP_CACHE = CacheBuilder.newBuilder()
                .maximumSize(100)
                .build(new CacheLoader<String, List<String>>() {
                    @Override
                    public List<String> load(String key) throws Exception {
                        return ZOO_KEEPER.getChildren(key, nodeSyncWatcher);
                    }
                });
    }

    /**
     * 获取实例
     *
     * @return
     */
    public static ZooKeeper get() {
        return ZOO_KEEPER;
    }


    /**
     * 获取服务实例
     *
     * @param ngName
     * @param ngVersion
     * @return
     */
    public static List<String> getGroupNode(String ngName, String ngVersion) {
        try {
            return GROUP_CACHE.get(ZKPKit.genNodeGroup(ngName, ngVersion));
        } catch (ExecutionException e) {
            LogKit.warn("Unexpected error", e);
            return Collections.emptyList();
        }
    }

    /**
     * 获取服务实例,如果失败则重试一次,使用随机算法
     * TODO 有线程全部睡眠的风险
     *
     * @param ngName
     * @param ngVersion
     * @return
     */
    public static String getNode(String ngName, String ngVersion) {
        List<String> list = getGroupNode(ngName, ngVersion);
        if (list.isEmpty()) {
            try {
                TimeUnit.SECONDS.sleep(5L);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            list = getGroupNode(ngName, ngVersion);
        }
        return ZKPKit.urlDecode(list.get(ThreadLocalRandom.current().nextInt(list.size())));
    }

    /**
     * 发现问题，必须刷新节点
     * TODO 此方法为的是刷新单个节点,避免刷新整个节点，是否正确 有点验证 比如线程是否安全
     *
     * @param ngName
     * @param ngVersion
     * @param nodeId
     */
    public static void refreshNode(String ngName, String ngVersion, String nodeId) {
        String nodeGroup = ZKPKit.genNodeGroup(ngName, ngVersion);
        List<String> nodeGroupList;
        try {
            nodeGroupList = GROUP_CACHE.get(nodeGroup);
            nodeGroupList.remove(nodeId);
        } catch (ExecutionException e) {
            nodeGroupList = Collections.EMPTY_LIST;
        }
        GROUP_CACHE.put(nodeGroup, nodeGroupList);
        ZOO_KEEPER.sync(nodeGroup, (rc, path, ctx) -> {
            if (KeeperException.Code.OK.intValue() == rc) {
                GROUP_CACHE.refresh(String.valueOf(ctx));
            }
        }, nodeGroup);
    }
}
