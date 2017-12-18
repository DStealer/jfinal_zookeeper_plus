package com.kuark.jfzk.demo.config;

import com.jfinal.config.*;
import com.jfinal.kit.LogKit;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.cron4j.Cron4jPlugin;
import com.jfinal.plugin.druid.DruidPlugin;
import com.jfinal.render.ViewType;
import com.jfinal.template.Engine;
import com.kuark.jfzk.demo.controller.HelloController;
import com.kuark.jfzk.demo.kit.TransactKit;
import com.kuark.jfzk.demo.model._MappingKit;
import com.kuark.jfzk.demo.protoc.HelloReq;
import com.kuark.jfzk.demo.protoc.HelloResp;
import com.kuark.jfzk.demo.vo.HelloReqJson;
import com.kuark.jfzk.demo.vo.HelloRespJson;

/**
 * jFinal配置
 * Created by LiShiwu on 06/16/2017.
 */
public final class AppConfig extends JFinalConfig {
    @Override
    public void configConstant(Constants me) {
        me.setDevMode(true);
        me.setViewType(ViewType.JSP);
    }

    @Override
    public void configRoute(Routes me) {
        me.setBaseViewPath("WEB-INF/views");
        me.add("/", HelloController.class);
    }

    @Override
    public void configEngine(Engine me) {

    }

    @Override
    public void configPlugin(Plugins me) {
        loadPropertyFile("datasource.properties");
        DruidPlugin druidPlugin = new DruidPlugin(getProperty("jdbc.url"), getProperty("jdbc.username"),
                getProperty("jdbc.password"), getProperty("jdbc.driverClassName"));
        me.add(druidPlugin);
        unloadPropertyFile();

        ActiveRecordPlugin activeRecordPlugin = new ActiveRecordPlugin(druidPlugin);
        _MappingKit.mapping(activeRecordPlugin);
        me.add(activeRecordPlugin);
        loadPropertyFile("bootstrap.properties");
        ZooKeeperPlugin zooKeeperPlugin = new ZooKeeperPlugin(getProperty("zk.service.url"), getProperty("app.node.name"),
                getProperty("app.node.version"), getProperty("app.node.url"));
        me.add(zooKeeperPlugin);
        unloadPropertyFile();

        Cron4jPlugin jPlugin = new Cron4jPlugin();
        jPlugin.addTask("* * * * *", () -> {
            for (int i = 0; i < 10; i++) {
                LogKit.info("Corn task start...");
                HelloReq req = HelloReq.newBuilder().setWho("神谕逍遥").build();
                HelloResp helloResp = TransactKit.transact("usr", "0.0.1", "avro", req, HelloResp.class);
                System.out.println("Response:" + helloResp.getMe());

                HelloReqJson reqJson = new HelloReqJson();
                reqJson.setName("神谕正法");
                HelloRespJson respJson = TransactKit.transact("usr", "0.0.1", "json", reqJson, HelloRespJson.class);
                System.out.println(respJson);
            }
        });
        me.add(jPlugin);
    }

    @Override
    public void configInterceptor(Interceptors me) {

    }

    @Override
    public void configHandler(Handlers me) {

    }
}
