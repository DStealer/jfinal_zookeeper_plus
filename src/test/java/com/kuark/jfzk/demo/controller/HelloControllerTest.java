package com.kuark.jfzk.demo.controller;

import com.kuark.jfzk.demo.kit.TransactKit;
import com.kuark.jfzk.demo.protoc.HelloReq;
import com.kuark.jfzk.demo.protoc.HelloResp;
import com.kuark.jfzk.demo.vo.HelloReqJson;
import com.kuark.jfzk.demo.vo.HelloRespJson;
import org.junit.Test;

public class HelloControllerTest {
    @Test
    public void tt01() throws Exception {
        HelloReq req = HelloReq.newBuilder().setWho("神谕逍遥").build();
        HelloResp helloResp = TransactKit.transact("http://127.0.0.1:9090/jzd/avro", req, HelloResp.class);
        System.out.println("Response:" + helloResp.getMe());
    }

    @Test
    public void tt02() throws Exception {
        HelloReqJson reqJson = new HelloReqJson();
        reqJson.setName("神谕正法");
        HelloRespJson respJson = TransactKit.transact("http://127.0.0.1:9090/jzd/json", reqJson, HelloRespJson.class);
        System.out.println(respJson);
    }
}