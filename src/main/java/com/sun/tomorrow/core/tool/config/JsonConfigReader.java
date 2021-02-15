package com.sun.tomorrow.core.tool.config;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

/**
 * 用法：example  获取 key 为redis的配置  这里的holder 需要与解析里面的json对应上。
 * Holder info = JsonConfigReader.getInstance().getObject(Holder.class, "redis");
 *
 */
@Slf4j
public class JsonConfigReader {

    private static final String DEFAULT_PATH = "config.json";

    private Map<String, Object> datas = new HashMap<>();

    private String origin;

    public JsonConfigReader () {
        init();
    }

    public static JsonConfigReader getInstance() {
        return Holder.instance;
    }

    private static class Holder {
        public static final JsonConfigReader instance  = new JsonConfigReader();
    }

    public void init() {
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        String content = parse(cl, DEFAULT_PATH);
        if(content == null) {
            log.error("error to init config txt");
            return ;
        }
        this.origin = content;
        this.datas = JSONObject.parseObject(content, new TypeReference<Map<String, Object>>(){}.getType());

    }

    public static String parse(ClassLoader classLoader, String fileName) {
        try(InputStream inputStream = classLoader.getResourceAsStream(fileName);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream))) {
            String cnt;
            StringBuilder sb = new StringBuilder();
            while((cnt = bufferedReader.readLine()) != null) {
                sb.append(cnt);
            }
            return sb.toString();
        } catch (IOException ex) {
            log.error("parse txt happen io exception:", ex);
        }
        return null;
    }

    public <T> T getObject (Class<T> clazz, String key) {
        if (StringUtils.isEmpty(key)) {
            return JSONObject.parseObject(this.origin, clazz);
        }
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
