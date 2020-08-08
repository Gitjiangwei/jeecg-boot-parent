package org.jeecg;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class JedisPoolUtils {

    private static JedisPool pool = null;

    static {
        //获得池子对象
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxIdle(30);//最大闲置个数
        poolConfig.setMaxWaitMillis(10000);//最大闲置个数
        poolConfig.setMinIdle(10);//最小闲置个数
        poolConfig.setMaxTotal(100);//最大连接数
        pool = new JedisPool(poolConfig, "localhost", 6379);
    }

    //获得jedis资源的方法
    public static Jedis getJedis() {
        return pool.getResource();
    }

    public static void main(String[] args) {
        Jedis jedis = getJedis();
        System.out.println(jedis);
    }
}
