package com.sun.tomorrow.core.tool.parseCore;

import com.sun.tomorrow.core.domain.SpiClassInfo;
import com.sun.tomorrow.core.domain.TRsource;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class AbstractHandlersReader implements TReader<Map<String, List<Class<?>>>> {

    protected String path;


    public AbstractHandlersReader(String path){
        this.path = path;
    }

    public Map<String, List<Class<?>>> parseResource(){
        return parseFormat(getContent());
    }
    /**
     * 获取path文件里面的内容。返回String
     */
    protected abstract String getContent();

    /**
     * 解析出对应的Handler想要的格式。
     * @param content
     * @return
     */
    protected abstract Map<String, List<Class<?>>> parseFormat(String content);

}
