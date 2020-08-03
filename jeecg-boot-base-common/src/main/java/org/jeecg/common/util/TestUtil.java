package org.jeecg.common.util;

import net.bytebuddy.implementation.bytecode.Throw;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

public class TestUtil {

    AtomicReference<Thread> atomicReference = new AtomicReference<Thread>();

    public void myLock(){
        Thread thread = Thread.currentThread();
        System.out.println(Thread.currentThread().getName()+"\t "+"come in ^0^");
        while (!atomicReference.compareAndSet(null,thread)){
            System.out.println(Thread.currentThread().getName()+"111");
        }
    }

    public void myUnlock(){
        Thread thread = Thread.currentThread();
        atomicReference.compareAndSet(thread,null);
        System.out.println(Thread.currentThread().getName()+"\t "+"invock myUnlock()");
    }


    public void setLock(){
        long thread = Thread.currentThread().getId();

    }
    public static void main(String[] arge){
        TestUtil testUtil = new TestUtil();
        new Thread(()->{
           testUtil.myLock();
            try {
                TimeUnit.SECONDS.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            testUtil.myUnlock();
        },"AAA").start();

        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        new Thread(()->{
           testUtil.myLock();
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            testUtil.myUnlock();
        },"BBB").start();
    }

}
