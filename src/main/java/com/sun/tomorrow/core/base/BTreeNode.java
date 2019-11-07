package com.sun.tomorrow.core.base;

import java.io.Serializable;

/**
 * @Author roger sun
 * @Date 2019/11/7 16:49
 */
public class BTreeNode<T> extends TreeNode {

    private transient int DefaultOrderNumber = 5;

    private BTreeNode[] childs;

    private Object[] vals;

    public BTreeNode(BTreeNode[] childs, T[] vals) {
        this.childs = childs;
        this.vals = vals;
    }

    public BTreeNode() {
        this.childs = new BTreeNode[DefaultOrderNumber];
        this.vals = new Object[DefaultOrderNumber];
    }

    public BTreeNode(int cap) {
        this.childs = new BTreeNode[cap];
        this.vals = new Object[cap];
    }

    public BTreeNode[] getChilds() {
        return childs;
    }

    public void setChilds(BTreeNode[] childs) {
        this.childs = childs;
    }

    public T[] getVals() {

        return (T[])vals;
    }

    public void setVals(Object[] vals) {
        this.vals = vals;
    }
}
