package com.sun.tomorrow.core.util;

import com.sun.tomorrow.core.util.exception.ResourceNotFoundException;
import org.springframework.core.io.UrlResource;

import java.io.InputStream;
import java.net.URL;

public class PathUtil {

    private static final String MIDDER_RESOURCE = "/src/main/resources";

    /**
     * 获取相对地址文件路径，意在能够根据用户x自定义路径进行配置，但必须在resources路径下的位置。
     * @return            返回文件路径
     */
    public static String getRelativePath(String path){
        String nowDir = System.getProperty("user.dir");
        return nowDir + path;
    }

    /**
     * 获取相对路径resources
     *
     * @param path luji
     * @return
     */
    public static String getRelativePathUnderResource(String path){
        String nowDir = System.getProperty("user.dir");
        return nowDir + MIDDER_RESOURCE + path;
    }

    public static InputStream getDefaultPath(String path){
        try {
            URL url = Thread.currentThread().getContextClassLoader().getClass().getClassLoader().getResource(path);
            if(url == null) url = ClassLoader.getSystemClassLoader().getResource(path);
            UrlResource urlResource = new UrlResource(url);
            return urlResource.getInputStream();
        }catch (Exception e){
            e.printStackTrace();
            throw new ResourceNotFoundException("error to read Resource!");
        }

    }
}
