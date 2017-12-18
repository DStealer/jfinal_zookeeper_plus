package com.kuark.jfzk.demo.kit;

import com.jfinal.kit.Base64Kit;
import com.jfinal.kit.HttpKit;
import com.jfinal.kit.ReflectKit;
import org.apache.avro.specific.SpecificRecordBase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.ConnectException;

public class TransactKit {
    private static final Logger LOGGER = LoggerFactory.getLogger(TransactKit.class);

    /**
     * 发送请求
     *
     * @param url    请求地址
     * @param req    请求
     * @param tClass 响应实体类型class
     * @param <K>    请求实体类型
     * @param <T>    响应实体类型
     * @return 响应实例
     * @throws Exception
     */
    public static <K extends SpecificRecordBase, T extends SpecificRecordBase> T transact(String url, K req, Class<T> tClass) {
        String data = Base64Kit.encode(ProtocKit.fromRecord(req));
        String receive = HttpKit.post(url, data);
        return ProtocKit.toRecord(Base64Kit.decode(receive), (T) ReflectKit.newInstance(tClass));
    }

    /**
     * 发送请求
     *
     * @param nodeName    节点名称
     * @param nodeVersion 节点版本
     * @param service     服务
     * @param req         请求
     * @param tClass      响应实体类型class
     * @param <K>         请求实体类型
     * @param <T>         响应实体类型
     * @return 响应实例
     * @throws Exception
     */
    public static <K extends SpecificRecordBase, T extends SpecificRecordBase> T transact(
            String nodeName, String nodeVersion, String service, K req, Class<T> tClass) {
        String nodeId = ZkKit.getNode(nodeName, nodeVersion);
        try {
            String data = Base64Kit.encode(ProtocKit.fromRecord(req));
            String receive = HttpKit.post(nodeId + service, data);
            return ProtocKit.toRecord(Base64Kit.decode(receive), (T) ReflectKit.newInstance(tClass));
        } catch (Exception e) {
            LOGGER.warn("Unexpected error", e);
            //无法连接,可以认为是节点宕机
            if (e.getCause() instanceof ConnectException) {
                ZkKit.refreshNode(nodeName, nodeVersion, nodeId);
            }
            throw e;
        }
    }

    /**
     * 发送请求
     *
     * @param url 服务地址
     * @param req 请求
     * @return 响应实例
     * @throws Exception
     */
    public static <T> T transact(String url, Object req, Class<T> tClass) {
        String response = HttpKit.post(url, ProtocKit.toJson(req));
        return ProtocKit.fromJson(response, tClass);
    }

    /**
     * 发送请求
     *
     * @param nodeName    节点名称
     * @param nodeVersion 节点版本
     * @param service     服务
     * @param req         请求
     * @return 响应实例
     * @throws Exception
     */
    public static <T> T transact(String nodeName, String nodeVersion, String service, Object req, Class<T> tClass) {
        String nodeId = ZkKit.getNode(nodeName, nodeVersion);
        try {
            String response = HttpKit.post(nodeId + service, ProtocKit.toJson(req));
            return ProtocKit.fromJson(response, tClass);
        } catch (Exception e) {
            LOGGER.warn("Unexpected error", e);
            //无法连接,可以认为是节点宕机
            if (e.getCause() instanceof ConnectException) {
                ZkKit.refreshNode(nodeName, nodeVersion, nodeId);
            }
            throw e;
        }
    }
}
