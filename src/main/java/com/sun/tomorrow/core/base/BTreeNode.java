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
//    private int nowChildIndex;


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

        this.level = 1;
    }

    /**
     * 生成  节点
     * @param vals 节点里的值
     * @param valIndex 节点里的 值的个数
     * @param level 高度
     * @param childs  节点的子树队列
     */
    public BTreeNode(Object[] vals, int valIndex, int level, BTreeNode[] childs){
        this.vals = vals;
        this.nowValIndex = valIndex;
        this.childs = childs;
        this.level = level;
    }

    public void valIndexIncrease(){
        this.nowValIndex ++;
    }
    public void valIndexIncrease(int num){
        this.nowValIndex += num;
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

/**
 *     举例分裂后节点vals的情况
 *     节点对应的值 vals ： [0 1 2 3]
 *     valindex :  4   tot: 4
 *     分裂后：
 *     left  Index : [0 1] ||| left length： 2
 *     right start index: [3] ||| right tot : tot - left length - 1  :  1
 *     -------------------------------------------------------------
 *     上述对应的孩子节点则有4个
 *     childs: [0 1 2 3 4]  tot: 5
 *
 *     分裂后：
 *     left val index: [0 1] -> childs left index: [0 1 2]   left length : 3
 *     right val index: [3] -->  childs right index  [3 4]   right length: 2  right start Index : 3 = left length
 *
 */


    /**
     * 输入
     * @return 返回 val 长度
     */
    public int getLeftSplitNodeLength(){
        return nowValIndex / 2;
    }
    //获取 分裂后， 右节点的起始位置
    public int getRightStartIndex(){
        return getLeftSplitNodeLength() + 1;
    }

    public int getRightSplitNodeLength(){
        return nowValIndex - getLeftSplitNodeLength() - 1;
    }

    /**
     * 输入
     * @return 返回 index 长度
     */
    public int getLeftSplitChildsNodeLength(){
        return getLeftSplitNodeLength() + 1;
    }
    //获取 分裂后， 右节点的起始位置
    public int getRightStartChildsIndex(){
        return getLeftSplitChildsNodeLength();
    }

    public int getRightSplitChildsNodeLength(){
        return getRightSplitNodeLength() + 1;
    }

    public Object[] getMidValArray(){
        Object[] mid = new Object[vals.length];
        mid[0] = vals[getLeftSplitNodeLength()];
        return mid;
    }

    public int length(){
        return vals.length;
    }

    public BTreeNode[] generateNewChildsNode(BTreeNode left,BTreeNode right){
        BTreeNode[] childs = new BTreeNode[length()];
        childs[0] = left;
        childs[1] = right;
        return childs;
    }

}
