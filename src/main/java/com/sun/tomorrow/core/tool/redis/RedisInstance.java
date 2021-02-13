package com.sun.tomorrow.core.tool.redis;

import com.sun.tomorrow.core.tool.config.JsonConfigReader;
import lombok.Getter;
import lombok.Setter;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

@Getter
public class RedisInstance {

    private Jedis jedis;
    private JedisPool jedisPool;

    public RedisInstance () {
        Holder info = JsonConfigReader.getInstance().getObject(Holder.class, "redis");
        init(info);
        initPool(info);
    }

    public static RedisInstance getInstance () {
        return Holder.redisInstance;
    }

    public void init(Holder holder) {
        this.jedis = new Jedis(holder.getHost(), holder.getPort());
    }

    public void initPool(Holder holder) {
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxIdle(holder.maxIdle);
        jedisPoolConfig.setMaxWaitMillis(holder.maxWaitTime);
        this.jedisPool = new JedisPool(jedisPoolConfig, holder.getHost(), holder.getPort(), holder.timeout);
    }

    @Getter
    @Setter
    public static class Holder {
        public static final RedisInstance redisInstance = new RedisInstance();

        private String host;
        private int port;
        private int maxIdle;
        private int maxWaitTime;
        private int timeout;

    }
}
