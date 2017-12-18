package com.kuark.jfzk.demo.kit;

import org.apache.zookeeper.ZooKeeper;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

/**
 * zookeeper plugin 工具类
 */
public class ZKPKit {
    private static final String UTF_8 = "UTF-8";

    /**
     * 生成节点组标识
     *
     * @param nodeName
     * @param nodeVersion
     * @return
     */
    public static String genNodeGroup(String nodeName, String nodeVersion) {
        return String.format("/%s@%s", nodeName, nodeVersion);
    }

    /**
     * 编码节点服务地址,默认使用URL encode
     *
     * @param url
     * @return
     */
    public static String urlEncode(String url) {
        try {
            return URLEncoder.encode(url, UTF_8);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 生成节点服务地址,默认使用URL encode
     *
     * @param url
     * @return
     */
    public static String urlDecode(String url) {
        try {
            return URLDecoder.decode(url, UTF_8);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 初始化zkkit的实例
     *
     * @param zooKeeper
     * @param nodeGroup
     * @param nodeId
     */
    public static void setZkKit(ZooKeeper zooKeeper, String nodeGroup, String nodeId) {
        ZkKit.init(zooKeeper, nodeGroup, nodeId);
    }
}
