package org.jeecg.common.util;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class TestUtil1 {

    public synchronized void mathodA(){
        Thread thread = Thread.currentThread();
        try {
            System.out.println("准备执行方法A。。。");
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(thread.getName()+"：方法A执行完毕");
    }

    public void methodB(){
        System.out.println("等待执行方法B。。。");
        synchronized (this){
            Thread thread = Thread.currentThread();
            System.out.println(thread.getName()+"：正在运行方法B......");
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(thread.getName()+"：运行B执行完毕");
        }
    }

    public static void main(String[] args){
/*        final TestUtil1 testUtil1 = new TestUtil1();
        new Thread(()->{
            testUtil1.mathodA();
        },"AAA").start();
        new Thread(()->{
            testUtil1.methodB();
        },"BBB").start();*/

        HashMap<String,Object> map = new HashMap<String, Object>();
        map.put("jw","123");
        map.put("jw1","456");
        map.put("jw2","456");
        map.put("jw3","456");
        map.put("jw4","456");
        map.put("jw5","456");
        map.put("jw6","456");
        map.put("jw7","456");
        map.put("jw8","45645645646546464");
        int[] ints = {1,2,3};
        map.put("jw8",ints);

        System.out.println(map.get("jw"));
    }
}
