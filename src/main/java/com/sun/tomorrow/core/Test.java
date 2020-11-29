package com.sun.tomorrow.core;

import com.sun.tomorrow.core.base.Rectangle;
import com.sun.tomorrow.core.service.ExecutorLocalService;
import com.sun.tomorrow.core.tool.base.BaseEntity;
import com.sun.tomorrow.core.tool.base.Heap;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @Author roger sun
 * @Date 2019/11/7 16:21
 */
public class Test {


    private int n;
    public Test(int n){
        this.n = n;
    }
    private Lock lock = new ReentrantLock();
    private Condition condition  = lock.newCondition();

//    private  boolean ok = false;
    private volatile int ok = 0;
    private volatile int count = 1 ;



    public void task1(){
        while(count < this.n) {
            while(ok != 0) {
//                System.out.print("task1");
                Thread.yield();
            }
            System.out.print(0);
            ok = 1;

        }
    }

    public void task2(){
        while(count <= this.n){
            while(count % 2 == 0 || ok != 1){
//                System.out.print("task2");
                Thread.yield();
            }
            System.out.print(count ++ );
            ok = 0;

        }
    }

    public void task3(){
        while(count <= this.n){
            while(count % 2 != 0 || ok != 2){
//                System.out.print("task3");
                Thread.yield();
            }
            System.out.print(count ++ );
            ok = 0;

        }

    }


    public static void main(String[] args) {


        ExecutorLocalService executorLocalService = new ExecutorLocalService(2);

        executorLocalService.doInvoke(Test.class, "task", int.class, 4);
    }

}
