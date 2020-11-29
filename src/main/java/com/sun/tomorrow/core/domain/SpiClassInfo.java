package com.sun.tomorrow.core.domain;

import java.util.List;

public class SpiClassInfo {

    private String id;
    private String className;
    private List<Class<?>> clazzObject;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public List<Class<?>> getClazzObject() {
        return clazzObject;
    }

    public void setClazzObject(List<Class<?>> clazzObject) {
        this.clazzObject = clazzObject;
    }
}
