package com.sun.tomorrow.core.extension;


import com.sun.tomorrow.core.tool.parseCore.AbstractHandlersReader;
import com.sun.tomorrow.core.tool.parseCore.DefaultTomorrowHandlerReader;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ExtensionFactory {

    public static final String prefix_file_path = "/TOMO/tomorrow/";

    /**
     * 解析器：固定的解析方式-约定
     */


    /**
     * 约定俗成的定义方式： /TOMO/相应的Class全限定名。
     */
    private final String path;

    public final Class<?> clazz;

    public final AbstractHandlersReader handlersReader;

    public ConcurrentHashMap<String, List<Class<?>>> cacheClassesMap = new ConcurrentHashMap<>();


    public ExtensionFactory(Class<?> clazz){
        this.clazz = clazz;
        this.path = prefix_file_path + clazz.getName();
        this.handlersReader = new DefaultTomorrowHandlerReader(this.path);
    }

    public ExtensionFactory(Class<?> clazz, String path){
        this.clazz = clazz;
        this.path = path;
        this.handlersReader = new DefaultTomorrowHandlerReader(this.path);
    }

    public ExtensionFactory(Class<?> clazz, String path, AbstractHandlersReader abstractHandlersReader){
        this.clazz = clazz;
        this.path = path;
        this.handlersReader = abstractHandlersReader;
    }

    /**
     * 按照给定的className 获取到对应想要的扩展Classes。
     * @return  扩展Classes的列表
     */
    public List<Class<?>> getExtensionClass(){
        if(this.cacheClassesMap.contains(this.clazz.getName())){
            return this.cacheClassesMap.get(this.clazz.getName());
        }

        Iterator<Map.Entry<String , List<Class<?>>>> mapEntry = this.handlersReader.parseResource().entrySet().iterator();
        while(mapEntry.hasNext()){
            Map.Entry<String, List<Class<?>>> tmp = mapEntry.next();
            cacheClassesMap.put(tmp.getKey(), tmp.getValue());
        }
        return this.cacheClassesMap.get(this.clazz.getName());
    }


}
