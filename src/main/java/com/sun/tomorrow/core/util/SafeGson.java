package com.sun.tomorrow.core.util;

import com.google.gson.Gson;

/**
 * @Author roger sun
 * @Date 2019/12/13 18:21
 */
public class SafeGson {

    private static Gson gson = new Gson();

    public static String toJson(Object msg){
        return gson.toJson(msg);
    }

}
