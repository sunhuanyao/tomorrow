package com.sun.tomorrow.core.container;

/**
 * @Author roger sun
 * @Date 2019/11/7 17:19
 */
public abstract class TreeFactory<T> {
    /**
     * 比较方法， 比较两节点的大小， 小于0 则 v1 < v2 大于0 则 v1 > v2
     * @param v1
     * @param v2
     * @return
     */
    public abstract int cmp(T v1, T v2);

}
