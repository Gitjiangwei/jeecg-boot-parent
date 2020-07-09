package org.jeecg;

//订单消费者

import org.apache.lucene.spatial3d.geom.Tools;
import org.jeecg.common.util.EmptyUtils;
import org.jeecg.common.util.OrderCodeUtils;
import org.jeecg.common.util.RedisUtil;
import org.kunze.diansh.entity.Order;
import org.kunze.diansh.service.IOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Iterator;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CompletionService;
import java.util.concurrent.DelayQueue;

@Component
public class OrderComsumer extends Thread implements ApplicationRunner {
    public static DelayQueue<Order> queue = new DelayQueue<Order>();

    public static IOrderService iOrderService;

    @Autowired
    public RedisUtil redisUtil;

    @Autowired
    public void setiOrderService(IOrderService iOrderService) {
        OrderComsumer.iOrderService = iOrderService;
    }

    @Override
    public void run() {
        while (true) {
            try {
                System.out.println("目前队列中有"+queue.size()+"个订单！");
                Order order = queue.take();

                //取消订单
                System.out.println("预计取消时间"+ order.getCancelTime().toString());
                String resultInfo = iOrderService.updateOrderStatu("6",order.getOrderId());
                if("ok".equals(resultInfo)){

                    System.out.println("订单取消，当前时间"+ new Date().toString());
                }else{
                    System.out.println("订单取消失败");
                }


            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        System.out.println("*************** 启动轮询订单监听 *******************");
        OrderComsumer oc = new OrderComsumer();
//        for(int i=0; i < 1;i++ ){
//            Order o = new Order();
//            o.setOrderId("123456");
//            o.setCreateTime(new Date());
//            o.setCancelTime(OrderCodeUtils.createCancelTime(o.getCreateTime()));
//            System.out.println("预计取消时间"+ o.getCancelTime().toString());
//            oc.queue.put(o);
//        }
        //启动线程
        oc.start();
    }

    /**加入延迟消息队列**/
    public static boolean addToOrderDelayQueue(Order order){
        return queue.add(order);
    }


    /**
     * 从延迟队列中移除
     * @param orderId 订单编号
     */
    public static void removeToOrderDelayQueue(String orderId){
        if(EmptyUtils.isEmpty(orderId)){
            return;
        }
        for (Iterator<Order> iterator = queue.iterator(); iterator.hasNext();) {
            Order orderNext = (Order) iterator.next();
            if(orderId.equals(orderNext.getOrderId())){
                queue.remove(orderNext);
                System.out.println("已删除："+orderNext.getOrderId());
            }
        }
//        queue.stream().forEach(o -> {
//            if(order.getOrderId().equals(o.getOrderId())){
//                queue.remove(o);
//            }
//        });
        System.out.println("当前队列剩余："+queue.size());
    }
}
