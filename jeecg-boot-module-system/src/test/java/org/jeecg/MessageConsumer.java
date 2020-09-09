package org.jeecg;

import redis.clients.jedis.Jedis;

import java.util.List;

public class MessageConsumer implements Runnable {
    public static final String MESSAGE_KEY = "message:queue";
    private volatile int count;

    public void consumerMessage() {
        Jedis jedis = JedisPoolUtils.getJedis();
        jedis.auth("123456");
        //String message = jedis.rpop(MESSAGE_KEY);
        List<String> message = jedis.brpop(0,MESSAGE_KEY);
        System.out.println(Thread.currentThread().getName() + " consumer message,message=" + message + ",count=" + count);
        count++;
    }

    @Override
    public void run() {
        while (true) {
            consumerMessage();
        }
    }

    public static void main(String[] args) {
        MessageConsumer messageConsumer = new MessageConsumer();
        Thread t1 = new Thread(messageConsumer, "thread6");
        Thread t2 = new Thread(messageConsumer, "thread7");
        t1.start();
        t2.start();
    }
}
