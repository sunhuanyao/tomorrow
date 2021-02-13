package com.sun.tomorrow.core.tool.config;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class JsonConfigReader {

    private static final String DEFAULT_PATH = "config.txt";

    private Map<String, Object> datas = new HashMap<>();

    public JsonConfigReader () {
        init();
    }

    public static JsonConfigReader getInstance() {
        return Holder.instance;
    }

    public static class Holder {
        public static final JsonConfigReader instance  = new JsonConfigReader();
    }

    public void init() {
        try(InputStream inputStream = JsonConfigReader.class.getClassLoader().getResourceAsStream(DEFAULT_PATH)) {
            if (inputStream == null) {
                log.error("input is null.");
                return ;
            }
            this.datas = JSONObject.parseObject(inputStream, new TypeReference<Map<String, Object>>(){}.getType());
        } catch (IOException ex) {
            log.error("parse txt happen io exception:", ex);
        }
    }

    public <T> T getObject (Class<T> clazz, String key) {
        Object object = this.datas.get(key);
        return JSONObject.parseObject(String.valueOf(object), clazz);
    }

    @Getter
    @Setter
    @ToString
    public static class RedisEntity {
        private String host;
        private int port;
    }
    public static void main(String[] args) {
        RedisEntity info = Holder.instance.getObject(RedisEntity.class, "redis");
        System.out.println(info);
    }


}
