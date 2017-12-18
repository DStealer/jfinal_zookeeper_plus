package com.kuark.jfzk.demo.config;

import com.google.common.collect.Lists;
import com.jfinal.core.JFinal;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.IPlugin;
import com.kuark.jfzk.demo.kit.ZKPKit;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Id;
import org.apache.zookeeper.server.auth.DigestAuthenticationProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.InvalidParameterException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

/**
 * zookeeper 插件
 */
public class ZooKeeperPlugin implements IPlugin {
    private static final Logger LOGGER = LoggerFactory.getLogger(ZooKeeperPlugin.class);
    private String connectString;
    private String nodeGroup;
    private String nodeId;
    private ZooKeeper zooKeeper;
    private String auth;

    public ZooKeeperPlugin(String zkUrls, String nodeName, String nodeVersion, String url) {
        if (StrKit.isBlank(zkUrls) || StrKit.isBlank(nodeName) || StrKit.isBlank(nodeVersion) || StrKit.isBlank(url)) {
            throw new InvalidParameterException("Connect string, node name, node version or node url can't be empty");
        }
        this.connectString = zkUrls;
        this.nodeGroup = ZKPKit.genNodeGroup(nodeName, nodeVersion);
        url = url + JFinal.me().getContextPath() + "/";
        this.nodeId = this.nodeGroup + "/" + ZKPKit.urlEncode(url);
        this.auth = this.nodeId + ":" + this.nodeGroup;
    }

    /**
     * 插件启动时调用
     *
     * @return
     */
    public boolean start() {
        try {
            this.zooKeeper = new ZooKeeper(connectString, 30000,
                    event -> LOGGER.info("Zookeeper event path:{} type:{} state:{} ", event.getPath(), event.getType(), event.getState()));
            //添加验证
            this.zooKeeper.addAuthInfo("digest", this.auth.getBytes());
            //创建节点组
            this.createNodeGroup(this.nodeGroup, null);
            //创建节点
            this.createNodeId(this.nodeId, null);
            //提供外部引用
            ZKPKit.setZkKit(this.zooKeeper, this.nodeGroup, this.nodeId);
            LOGGER.info("Register zookeeper node:[" + this.nodeId + "] success");
            return true;
        } catch (Exception e) {
            LOGGER.error("Register zookeeper node error", e);
            return false;
        }
    }

    /**
     * 创建节点组
     *
     * @param groupPath
     * @param data
     */
    private String createNodeGroup(String groupPath, byte[] data) throws KeeperException, InterruptedException {
        try {
            List<ACL> aclList = Lists.newArrayList(new ACL(ZooDefs.Perms.READ | ZooDefs.Perms.CREATE, ZooDefs.Ids.ANYONE_ID_UNSAFE));
            return this.zooKeeper.create(groupPath, data, aclList, CreateMode.CONTAINER);
        } catch (KeeperException.NodeExistsException e) {
            return groupPath;
        }
    }

    /**
     * 创建节点标识
     *
     * @param serverPath
     * @param data
     */
    private String createNodeId(String serverPath, byte[] data) throws KeeperException, InterruptedException, NoSuchAlgorithmException {
        List<ACL> aclList = Lists.newArrayList(new ACL(ZooDefs.Perms.READ, ZooDefs.Ids.ANYONE_ID_UNSAFE),
                new ACL(ZooDefs.Perms.ALL, new Id("digest", DigestAuthenticationProvider.generateDigest(this.auth))));
        return this.zooKeeper.create(serverPath, data, aclList, CreateMode.EPHEMERAL);
    }

    /**
     * 节点关闭时调用
     *
     * @return
     */
    public boolean stop() {
        if (this.zooKeeper != null) {
            try {
                this.zooKeeper.delete(this.nodeId, -1);
                this.zooKeeper.close();
            } catch (InterruptedException | KeeperException e) {
                LOGGER.warn("Unload zookeeper node error", e);
            }
        }
        return true;
    }
}
