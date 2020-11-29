package com.sun.tomorrow.core;

import com.sun.tomorrow.core.service.ExecutorLocalService;
import org.omg.Messaging.SYNC_WITH_TRANSPORT;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.IntConsumer;

public class ZeroEvenOd {
    private int n;



    public ZeroEvenOd(int n) {
        this.n = n;
    }

    // printNumber.accept(x) outputs "x", where x is an integer.
    Lock lock = new ReentrantLock();
    Condition z = lock.newCondition();
    Condition num = lock.newCondition();
    volatile boolean zTurn = true;
    volatile int zIndex = 0;

    public void taskZero() throws InterruptedException {
       
        for(;zIndex<n;) {
            lock.lock();
            try {
                while(!zTurn) {
//                    System.out.print(":false: 0");
                    z.await();
                }
//                printNumber.accept(0);
//                System.out.print(0);
                print(0);
                zTurn = false;
                num.signalAll();
                zIndex++;
            }finally {
                lock.unlock();
            }
        }
    }

    public void taskEven() throws InterruptedException {

        for(int i=2; i<=n; i+=2) {
            lock.lock();
            try {
                while(zTurn || (zIndex&1)==1) {
//                    System.out.print(":false:even");
                    num.await();
                }

//                printNumber.accept(i);
//                System.out.print(i);
                print(i);
                zTurn = true;
                z.signal();
            }finally {
                lock.unlock();
            }
        }
    }

    public void taskOdd() throws InterruptedException {

        for(int i=1; i<=n; i+=2) {
            lock.lock();
            try {
                while(zTurn || (zIndex&1)==0) {
//                    System.out.print(":false:odd");
                    num.await();
                }
//                printNumber.accept(i);
//                System.out.print(i);
                print(i);
                zTurn = true;
                z.signal();
            }finally {
                lock.unlock();
            }
        }
    }

    public void print(Object ok){
        System.out.println("thread id "+ Thread.currentThread().getName() + " " + ok);
    }
    public static void main(String[] args){

        ExecutorLocalService executorLocalService = new ExecutorLocalService(3);
        executorLocalService.doInvoke(ZeroEvenOd.class, "task", int.class, 4);
    }
}
