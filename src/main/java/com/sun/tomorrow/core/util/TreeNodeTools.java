package com.sun.tomorrow.core.util;

/**
 * @Author roger sun
 * @Date 2019/12/13 16:13
 */


import com.sun.tomorrow.core.base.BTreeNode;

/**
 * 该类主要用于树结构节点操作。
 */
public class TreeNodeTools {

    /**
     * 将 key 插入到origin[index]
     * @param origin        即将插入的数组
     * @param key           插入的数
     * @param index         插入的位置
     * @return              完成操作的数组
     */
    public static <T> Object[] insertIntoArray(Object[] origin, T key, int index, int cap){
        if(index == -1){
            origin[0] = key;
            return origin;
        }
        if(origin.length > cap) throw new ArrayIndexOutOfBoundsException("beyond the cap: " + cap);
        for (int i = origin.length - 1; i > index; i --){
            origin[i] = origin[i - 1];
        }
        origin[index] = key;
        return origin;
    }

    /**
     * 数组和数组进行合并 --- 用于 val 值的操作
     * @param origin            -
     * @param tmp               -
     * @param index             插入的未知
     * @param originSize        origin的valindex
     * @param tmpSize           tmp的valindex
     * @param cap               -
     * @return                  -
     */
    public static Object[] insertIntoArray(Object[] origin, Object[] tmp, int index, int originSize, int tmpSize, int cap){
        if(origin.length > cap || originSize + tmpSize > cap) throw new ArrayIndexOutOfBoundsException("beyond the cap: " + cap);


        for(int i = origin.length - 1; i > index + tmpSize - 1; i -- ){
            origin[i] = origin[i - tmpSize];
        }
        int tmpPos = tmpSize - 1;
        for(int i = index + tmpSize - 1; i >= index; i --){
            origin[i] = tmp[tmpPos --];
        }

        return origin;

    }

    /**
     * 数组和数组进行合并 --- 用于 child 值的操作   和 insertIntoArray 的主要差别在与childs 的合并会有覆盖的情况。
     * @param origin
     * @param tmp
     * @param index
     * @param originSize
     * @param tmpSize
     * @param cap
     * @return
     */
    public static BTreeNode[] coverAndAdd(BTreeNode[] origin, BTreeNode[] tmp, int index, int originSize, int tmpSize, int cap){
        if(origin.length > (cap + 1) || originSize + tmpSize - 1 > cap + 1) throw new ArrayIndexOutOfBoundsException("beyond the cap: " + cap);


        for(int i = origin.length - 1; i > index + tmpSize - 1; i -- ){
            origin[i] = origin[i - tmpSize + 1];
        }
        int tmpPos = tmpSize - 1;
        for(int i = index + tmpSize - 1; i >= index; i --){
            origin[i] = tmp[tmpPos --];
        }

        return origin;
    }

}
