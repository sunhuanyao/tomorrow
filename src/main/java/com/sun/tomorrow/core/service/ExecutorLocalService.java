package com.sun.tomorrow.core.service;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 该方法用于对线程调用的整合方法。根据本项目需求设计；
 */

public class ExecutorLocalService {

    private final ExecutorService executorService;

    public ExecutorLocalService(int num){
        this.executorService = Executors.newFixedThreadPool(num);
    }

    public void exec(Runnable[] runnables){

        for(Runnable runnable : runnables){
            this.executorService.execute(runnable);
        }

    }

    public void doInvoke(Class<?> clazz, String args){
        Method[] methods = clazz.getDeclaredMethods();

        List<Method> methodList = new ArrayList<>();

        for(Method method:methods){
            if(islegal(method.getName(), args)){
                methodList.add(method);
            }
        }
        Runnable[] runnables = new Runnable[methodList.size()];
        int i;
        for( i = 0 ; i < runnables.length; ++ i){
            final int op = i;
            runnables[i] = ()->{
                try {
//                        System.out.println("doInvoke!!!!");
                    //invoke 调用 传参数必须是实例化变量
                    methodList.get(op).invoke(clazz.newInstance());
                }catch (Exception e){

                }
            };
        }
        this.exec(runnables);
    }

    public boolean islegal(String main, String args){
        return main.contains(args);
    }



}
