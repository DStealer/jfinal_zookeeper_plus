package com.kuark.jfzk.demo.kit;

import org.apache.avro.message.RawMessageDecoder;
import org.apache.avro.message.RawMessageEncoder;
import org.apache.avro.reflect.ReflectData;
import org.apache.avro.specific.SpecificRecordBase;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;

/**
 * 转化类
 * TODO 此处仍可以优化
 * Created by lixiaoming on 2017/7/7.
 */
public class ProtocKit {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    /**
     * 由字节数组转换到实例
     */
    public static <T extends SpecificRecordBase> T toRecord(byte[] bytes, T record) {

        RawMessageDecoder<T> decoder = new RawMessageDecoder<>(ReflectData.get(), record.getSchema());
        try {
            return decoder.decode(bytes, record);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 由实例转换到字节数组
     */
    public static <T extends SpecificRecordBase> byte[] fromRecord(T record) {
        RawMessageEncoder<T> encoder = new RawMessageEncoder<>(ReflectData.get(), record.getSchema());
        try {
            return encoder.encode(record).array();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 转换实例到json
     */
    public static <T> String toJson(T record) {
        try {
            return OBJECT_MAPPER.writeValueAsString(record);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 转换实例到json
     */
    public static <T> T fromJson(String jsonString, Class<T> tClass) {
        try {
            return OBJECT_MAPPER.readValue(jsonString, tClass);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
