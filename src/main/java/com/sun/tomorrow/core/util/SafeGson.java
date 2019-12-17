package com.sun.tomorrow.core.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Author roger sun
 * @Date 2019/12/13 18:21
 */
public class SafeGson {

    private static Gson gson = new Gson();



    public static List<Integer> fromJsonToListInteger(String tmp)throws Exception{
        if (tmp == null) throw new NullPointerException("parameter is not null!");
        Type IntegerListType = new TypeToken<List<Integer>>(){}.getType();
        return gson.fromJson(tmp, IntegerListType);

    }

    public static List<Long>  fromJsonToListLong(String tmp){
        if (tmp == null) throw new NullPointerException("parameter is not null!");
        Type LongListType = new TypeToken<List<Long>>(){}.getType();
        return gson.fromJson(tmp, LongListType);
    }
    public static List<String> fromJsonToListString(String tmp)throws Exception{
        if (tmp == null) throw new NullPointerException("parameter is not null!");
        Type StringListType = new TypeToken<List<String>>(){}.getType();
        return gson.fromJson(tmp, StringListType);

    }

    public static List<Map<String, Object>> fromJsonToListMap(String tmp) throws Exception{
        if(tmp == null ) throw new NullPointerException("parameter is not null");
        Type MapListType = new TypeToken<List<Map<String, Object>>>(){}.getType();
        return listMapGson.fromJson(tmp, MapListType);
    }

    public static Map<String, Object> fromJsonToMap(String tmp) throws Exception{
        if(tmp == null ) throw new NullPointerException("parameter is not null");
        Type MapType = new TypeToken<Map<String, Object>>(){}.getType();
        return mapGson.fromJson(tmp, MapType);
    }
    public static Map<String, String> fromJsonToMapStringToString(String tmp) throws RuntimeException{
        if(tmp == null ) throw new NullPointerException("parameter is not null");
        Type MapType = new TypeToken<Map<String, String>>(){}.getType();
        return mapGson.fromJson(tmp, MapType);
    }


    //该部分处理  Object整型变成double型 情况----适用于（Map<String, Object>）
    private static Gson mapGson = new GsonBuilder().registerTypeAdapter(
            new TypeToken<Map<String, Object>>(){}.getType(),
            new SafeGson().new ListTypeAdapter()
    ).create();

    private static Gson listMapGson = new GsonBuilder().registerTypeAdapter(
            new TypeToken<List<Map<String, Object>>>(){}.getType(),
            new SafeGson().new ListTypeAdapter()
    ).create();




    public static String toJson(Object obj){
        return gson.toJson(obj);
    }

    public class ListTypeAdapter extends TypeAdapter<Object> {
        @Override
        public void write(JsonWriter out, Object value) throws IOException {
        }

        @Override
        public Object read(JsonReader in) throws IOException {
            return readInternal(in);
        }


        private Object readInternal(JsonReader in) throws IOException {
            JsonToken token = in.peek();
            switch (token) {
                case BEGIN_ARRAY:
                    List<Object> list = new ArrayList<>();
                    in.beginArray();
                    while (in.hasNext()) {
                        list.add(readInternal(in));
                    }
                    in.endArray();
                    return list;

                case BEGIN_OBJECT:
                    Map<String, Object> map = new LinkedTreeMap<>();
                    in.beginObject();
                    while (in.hasNext()) {
                        map.put(in.nextName(), readInternal(in));
                    }
                    in.endObject();
                    return map;

                case STRING:
                    return in.nextString();

                case NUMBER:
                    //将其作为一个字符串读取出来
                    String numberStr = in.nextString();
                    //返回的numberStr不会为null
                    if (numberStr.contains(".") || numberStr.contains("e")
                            || numberStr.contains("E")) {
                        return Double.parseDouble(numberStr);
                    }
                    return Long.parseLong(numberStr);

                case BOOLEAN:
                    return in.nextBoolean();

                case NULL:
                    in.nextNull();
                    return null;

                default:
                    throw new IllegalStateException();
            }
        }
    }

}
