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

    public static void main(String[] args) {
        Jedis jedis = RedisInstance.getInstance().jedisPool.getResource();
////        jedis.lpush("test", "1", "2");
////        Object ok = ok
//        System.out.println(jedis.lrange("test", 0, -1));
//        jedis.set("test_int", "1");
//        jedis.incr("test_int");
//        System.out.println(jedis.exists("test_int"));
//        System.out.println(jedis.get("test_int"));

//        List<String> arr = new ArrayList<>();
//        arr.add("1");
//        arr.add("2");
//        jedis.lpush("key_test", arr.toArray(new String[]{}));
        System.out.println(jedis.exists("key_test"));
    }
}
