package com.kuark.jfzk.demo.controller;

import com.google.common.io.CharStreams;
import com.jfinal.core.Controller;
import com.jfinal.kit.Base64Kit;
import com.kuark.jfzk.demo.kit.ProtocKit;
import org.apache.avro.specific.SpecificRecordBase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;

/**
 * 处理请求，转换协议
 */
public abstract class JKController extends Controller {
    private static final Logger LOGGER = LoggerFactory.getLogger(JKController.class);

    protected <T extends SpecificRecordBase> T getRequestRecord(Class<T> tClass) throws IOException {
        try (Reader reader = this.getRequest().getReader()) {
            return ProtocKit.toRecord(Base64Kit.decode(CharStreams.toString(reader)), tClass.newInstance());
        } catch (InstantiationException | IllegalAccessException e) {
            LOGGER.error("Unknown exception", e);
            throw new RuntimeException("Convert error");
        }
    }

    public <T extends SpecificRecordBase> void setResponseRecord(T record) throws IOException {
        try (Writer writer = this.getResponse().getWriter()) {
            writer.write(Base64Kit.encode(ProtocKit.fromRecord(record)));
        }
        this.renderNull();
    }

    protected <T> T getRequestJson(Class<T> tClass) throws IOException {
        try (Reader reader = this.getRequest().getReader()) {
            return ProtocKit.fromJson(CharStreams.toString(reader), tClass);
        }
    }

    public <T> void setResponseJson(T record) throws IOException {
        try (Writer writer = this.getResponse().getWriter()) {
            writer.write(ProtocKit.toJson(record));
        }
        this.renderNull();
    }
}
