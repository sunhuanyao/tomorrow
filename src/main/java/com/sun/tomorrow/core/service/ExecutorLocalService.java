package com.sun.tomorrow.core.service;

import org.omg.Messaging.SYNC_WITH_TRANSPORT;

import java.lang.reflect.Constructor;
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
    /**
     *  运行的类
     */

    public ExecutorLocalService(int num){
        this.executorService = Executors.newFixedThreadPool(num);
    }

    public void exec(Runnable[] runnables){

        for(Runnable runnable : runnables){
            this.executorService.execute(runnable);
        }

    }

    /**
     *  调用主入口
     * @param clazz 调用的类
     * @param args  调用的方法
     * @param conArgs 构造函数参数
     */
    public <T> void doInvoke(Class<?> clazz, String methodName, Class<?> parameterType, T ... conArgs){
        Method[] methods = clazz.getDeclaredMethods();

        List<Method> methodList = new ArrayList<>();

        for(Method method:methods){
            if(islegal(method.getName(), methodName)){
                methodList.add(method);
            }
        }
        Runnable[] runnables = new Runnable[methodList.size()];
        int i;

//        final Object single;
        try {
//            final Object single = clazz.newInstance();
            final Object single = createInstance(clazz, parameterType, conArgs);
            for( i = 0 ; i < runnables.length; ++ i){
                final int op = i;
                runnables[i] = ()->{
                    try {
    //                        System.out.println("doInvoke!!!!");
                        //invoke 调用 传参数必须是实例化变量
                        methodList.get(op).invoke(single);
                    }catch (Exception e){

                    }
                };
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        this.exec(runnables);

    }

    public <T> Object createInstance(Class<?> clazz, Class<?> parameterType, T ... args){
        try {
            if(args == null || args.length == 0) return clazz.newInstance();
            Constructor<?> constructor = clazz.getConstructor(parameterType);
            return constructor.newInstance(args);

        }catch ( Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public boolean islegal(String main, String args){
        return main.contains(args);
    }



}
