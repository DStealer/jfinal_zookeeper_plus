package com.kuark.jfzk.demo.controller;

import com.jfinal.core.ActionKey;
import com.jfinal.ext.kit.DateKit;
import com.kuark.jfzk.demo.protoc.HelloReq;
import com.kuark.jfzk.demo.protoc.HelloResp;
import com.kuark.jfzk.demo.vo.HelloReqJson;
import com.kuark.jfzk.demo.vo.HelloRespJson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

public class HelloController extends JKController {
    private static final Logger LOGGER = LoggerFactory.getLogger(HelloController.class);

    @ActionKey("/hello")
    public void hello() throws Exception {
        this.renderText("Current time:" + DateKit.toStr(new Date()));
    }

    @ActionKey("/avro")
    public void avro() throws Exception {
        HelloReq req = this.getRequestRecord(HelloReq.class);
        LOGGER.info("hello from:[{}:{}],[{}]", this.getRequest().getRemoteHost(), this.getRequest().getRemotePort(), req.getWho());
        this.setResponseRecord(HelloResp.newBuilder().setMe("末日十七").build());
    }

    @ActionKey("/json")
    public void json() throws Exception {
        HelloReqJson req = this.getRequestJson(HelloReqJson.class);
        LOGGER.info("hello from:[{}:{}],[{}]", this.getRequest().getRemoteHost(), this.getRequest().getRemotePort(), req.getName());
        HelloRespJson resp = new HelloRespJson();
        resp.setWho("I am robot!");
        resp.setTime(DateKit.toStr(new Date()));
        this.setResponseJson(resp);
    }
}
