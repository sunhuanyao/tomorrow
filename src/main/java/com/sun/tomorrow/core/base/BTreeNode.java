package com.sun.tomorrow.core.base;

import com.sun.tomorrow.core.util.exception.TooLargeException;

import java.io.Serializable;

/**
 * @Author roger sun
 * @Date 2019/11/7 16:49
 */
public class BTreeNode<T> extends TreeNode {

    private static int DefaultOrderNumber = 5;

    public static int MAXENTRYARRAYS = 100;

    private BTreeNode[] childs;

    private Object[] vals;

    private int nowValIndex;
    private int nowChildIndex;


    public BTreeNode() {
        this(DefaultOrderNumber);
    }

    public BTreeNode(int cap) {
        if(cap > MAXENTRYARRAYS) throw new TooLargeException();
        this.childs = new BTreeNode[cap];
        this.vals = new Object[cap];
        this.nowValIndex = 0;
        this.nowChildIndex = 0;
    }


    public void add(T val, BTreeNode node){
        this.vals[nowValIndex ++] = val;
        this.childs[nowChildIndex ++] = node;
    }

    public BTreeNode[] getChilds() {
        return childs;
    }

    public void setChilds(BTreeNode[] childs) {
        this.childs = childs;
    }

    public Object[] getVals() {

        return vals;
    }

    public void setVals(Object[] vals) {
        this.vals = vals;
    }
}
