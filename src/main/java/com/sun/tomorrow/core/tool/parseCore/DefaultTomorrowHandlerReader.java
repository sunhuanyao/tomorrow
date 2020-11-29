package com.sun.tomorrow.core.tool.parseCore;

import com.sun.tomorrow.core.util.ResourceUtil;
import com.sun.tomorrow.core.util.exception.FormatErrorException;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.*;

public class DefaultTomorrowHandlerReader extends AbstractHandlersReader{

    public static final String DEFAULT_TOM_HANDLER = "/TOMO/tomorrow.handlers";

    private static final String A_SPLIT = "@";
    private static final String B_SPLIT = "=";
    private static final String C_SPLIT = ",";

    public DefaultTomorrowHandlerReader(){
        super(DEFAULT_TOM_HANDLER);
    }
    public DefaultTomorrowHandlerReader(String path){
        super(path);
    }

    @Override
    public String getContent() {
        try {
            BufferedReader bufferedReader = ResourceUtil.getBufferReader(this.path);
            StringBuffer stringBuffer = new StringBuffer();
            String content;
            while((content = bufferedReader.readLine()) != null){
                stringBuffer.append(content);
            }
            return stringBuffer.toString();

        }catch (IOException e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Map<String, List<Class<?>>> parseFormat(String content) {
        Map<String, List<Class<?>>> useClasses = new HashMap<>();
        String[] classesContent = content.split(A_SPLIT);
        for(int i = 1 ; i < classesContent.length; ++ i){
            String[] values = classesContent[i].split(B_SPLIT);
            if(values.length != 2) throw new FormatErrorException();
            String classSimpleName = values[0];
            String[] classes = values[1].split(C_SPLIT);
            List<Class<?>> classList =  new ArrayList<>();
            for(int j = 0 ; j < classes.length; ++ j){
                try {
                    classList.add(ClassLoader.getSystemClassLoader().loadClass(classes[j]));

                }catch (Exception e){
                    e.printStackTrace();
                }
            }
            if(useClasses.containsKey(classSimpleName)){
                useClasses.get(classSimpleName).addAll(classList);
            }else{
                useClasses.put(classSimpleName, classList);
            }

        }
        return useClasses;
    }

    public static void main(String[] args){
        DefaultTomorrowHandlerReader defaultTomorrowHandlerReader = new DefaultTomorrowHandlerReader();
        Map<String, List<Class<?>>> classesMap = defaultTomorrowHandlerReader.parseResource();
        System.out.print("ok");
    }
}
