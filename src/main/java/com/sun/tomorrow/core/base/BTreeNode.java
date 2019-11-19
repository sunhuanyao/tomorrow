package com.sun.tomorrow.core.base;

import com.sun.tomorrow.core.util.exception.TooLargeException;

import java.io.Serializable;

/**
 * @Author roger sun
 * @Date 2019/11/7 16:49
 */
public class BTreeNode<T> extends TreeNode {

    public static int DefaultOrderNumber = 5;

    public static int MAXENTRYARRAYS = 100;

    private BTreeNode[] childs;

    private Object[] vals;

    private int level;

    private int nowValIndex;
    private int nowChildIndex;


    public BTreeNode() {
        this(DefaultOrderNumber);
    }

    /**
     * level = 1 表示叶子节点
     * @param cap   index : 0 ~ cap - 1   ---- cap - 1是预留
     */
    public BTreeNode(int cap) {
        if(cap > MAXENTRYARRAYS) throw new TooLargeException();
        this.childs = new BTreeNode[cap + 1];
        this.vals = new Object[cap];
        this.nowValIndex = 0;
        this.nowChildIndex = 0;
        this.level = 1;
    }

    /**
     * 生成叶子节点
     * @param vals   叶子节点里的值
     * @param valIndex  叶子节点里的 值的个数
     */
    public BTreeNode(Object[] vals, int valIndex){
        this.vals = vals;
        this.nowValIndex = valIndex;
        this.childs = new BTreeNode[vals.length];
        this.nowChildIndex = 0;
        this.level = 1;
    }

    /**
     * 生成  节点
     * @param vals 节点里的值
     * @param valIndex 节点里的 值的个数
     * @param level 高度
     * @param childs  节点的子树队列
     * @param nowChildIndex 节点的子树队列的个数
     */
    public BTreeNode(Object[] vals, int valIndex, int level, BTreeNode[] childs, int nowChildIndex){
        this.vals = vals;
        this.nowValIndex = valIndex;
        this.childs = childs;
        this.nowChildIndex = nowChildIndex;
        this.level = level;
    }

    public void valIndexIncrease(){
        this.nowValIndex ++;
    }
    public void childIndexIncrease(){
        this.nowChildIndex ++;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }
    //实际有值 0 ~ nowValIndex - 1
    public int getNowValIndex() {
        return nowValIndex;
    }

    public void setNowValIndex(int nowValIndex) {
        this.nowValIndex = nowValIndex;
    }

    public int getNowChildIndex() {
        return nowChildIndex;
    }

    public void setNowChildIndex(int nowChildIndex) {
        this.nowChildIndex = nowChildIndex;
    }

//    public void add(T val, BTreeNode node){
//        this.vals[nowValIndex ++] = val;
//        this.childs[nowChildIndex ++] = node;
//    }

    public BTreeNode[] getChilds() {
        return childs;
    }

    public void setChilds(BTreeNode[] childs) {
        this.childs = childs;
    }


    @SuppressWarnings("unchecked")
    public T[] getVals() {

        return (T[])vals;
    }

    public void setVals(Object[] vals) {
        this.vals = vals;
    }
}
