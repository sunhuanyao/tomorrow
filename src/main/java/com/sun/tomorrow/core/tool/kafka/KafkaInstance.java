package com.sun.tomorrow.core.tool.kafka;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.sun.tomorrow.core.util.exception.BusinessException;
import lombok.Getter;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.time.Duration;
import java.util.*;

public class KafkaInstance {

    private static Logger LOG = LoggerFactory.getLogger(KafkaInstance.class);

    public static final String KAFKAPATH = "kafka.txt";

    @Getter
    enum Type{
        PRODUCE("producer"),
        CONSUMER("consumer");

        String info;

        Type(String info) {
            this.info = info;
        }
    }

    public KafkaProducer<String, String> getProducerClient(){
        Properties config = getConfig(Type.PRODUCE);
        return new KafkaProducer<>(config);
    }

    public KafkaConsumer<String, String> getConsumerClient(){
        Properties config = getConfig(Type.CONSUMER);
        return new KafkaConsumer<>(config);
    }

    private Properties getConfig(Type type) {
        Properties properties = new Properties();
        Map<String, Object> res;
        try(InputStream reader = KafkaInstance.class.getClassLoader().getResourceAsStream(KAFKAPATH)) {
           res = JSONObject.parseObject(reader, new TypeReference<Map<String, Object>>() {
            }.getType());
        }catch (IOException e){
            throw new BusinessException("happen io exception:" + e);
        }
        Object info;
        switch (type) {
            case PRODUCE:
                info = res.get(Type.PRODUCE.info);
                LOG.info("producer: {}", info);
                break;
            case CONSUMER:
                info = res.get(Type.CONSUMER.info);
                LOG.info("consumer: {}", info);
                break;
            default:
                LOG.error("not support type");
                throw new BusinessException("not support type");
        }

        Map<String, String> now = JSONObject.parseObject(String.valueOf(info), new TypeReference<Map<String, String>>(){}.getType());

        for(Map.Entry<String, String> entry : now.entrySet()) {
            properties.setProperty(entry.getKey(), entry.getValue());
        }
        return properties;
    }

    public static void main(String[] args) {
        KafkaConsumer<String, String> consumer = new KafkaInstance().getConsumerClient();
        consumer.subscribe(Collections.singletonList("test"));
        consumer.poll(Duration.ofMillis(1000));
    }
}
