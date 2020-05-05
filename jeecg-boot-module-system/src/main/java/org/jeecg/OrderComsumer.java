package org.jeecg;

//订单消费者

import org.kunze.diansh.entity.Order;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CompletionService;
import java.util.concurrent.DelayQueue;

@Component
public class OrderComsumer extends Thread implements ApplicationRunner {
    DelayQueue<Order> queue = new DelayQueue<Order>();


    @Override
    public void run() {
        while (true) {
            try {
                Order order = queue.take();
                //模拟取消订单操作
                System.out.println("预计取消时间"+ order.getCancelTime().toString());
                System.out.println("订单取消，当前时间"+ new Date().toString());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        System.out.println("*************** 启动轮询订单监听 *******************");
        OrderComsumer oc = new OrderComsumer();
        for(int i=0; i < 1;i++ ){
            Order o = new Order();
            o.setCreateTime(new Date());
            System.out.println("预计取消时间"+ o.getCancelTime().toString());
            oc.queue.put(o);
        }
        //启动线程
        oc.start();
    }
}
