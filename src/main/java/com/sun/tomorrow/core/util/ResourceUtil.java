package com.sun.tomorrow.core.util;

import com.sun.org.apache.xalan.internal.xsltc.dom.CurrentNodeListFilter;
import com.sun.tomorrow.core.util.exception.ResourceNotFoundException;
import org.omg.PortableInterceptor.INACTIVE;
import org.springframework.core.io.UrlResource;

import java.io.*;
import java.net.URL;

import static java.lang.ClassLoader.getSystemResource;

public class ResourceUtil {
    /**
     * 默认先从本地相对路径下找， 如果找不到，则在默认地址下找。
     * @param path
     * @return
     * @throws IOException
     */
    public static BufferedReader getBufferReader(String path) throws IOException{
        try {
            String tmpPath = PathUtil.getRelativePathUnderResource(path);
            return new BufferedReader(new InputStreamReader(new FileInputStream(tmpPath)));
        }catch (Exception e) {
            URL url = ClassLoader.getSystemResource(path);
            if (url == null) throw new ResourceNotFoundException(path);
            UrlResource urlResource = new UrlResource(url);

            return new BufferedReader(new InputStreamReader(urlResource.getInputStream()));
        }
    }


    public static void main(String[] args) throws IOException{

        BufferedReader reader = getBufferReader("/TOMO/tomorrow.handlers");
        String content;
        while((content = reader.readLine()) != null){
            System.out.print(content);
        }

    }
}
