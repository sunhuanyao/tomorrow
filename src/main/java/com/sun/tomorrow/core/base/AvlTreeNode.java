package com.sun.tomorrow.core.base;

import sun.reflect.generics.tree.Tree;

/**
 * @Author roger sun
 * @Date 2019/11/7 16:39
 */
public class AvlTreeNode<T> extends TreeNode<T> {

    private T val;
    private int height;
    private AvlTreeNode<T> left;
    private AvlTreeNode<T> right;


    public AvlTreeNode(AvlTreeNode<T> left, AvlTreeNode<T> right, T val) {

        this.left = left;
        this.right = right;
        this.val = val;
        this.height = 1;
    }

    public AvlTreeNode(T val) {
        this(null,null, val);
    }

    public AvlTreeNode<T> getLeft() {
        return left;
    }

    public void setLeft(AvlTreeNode<T> left) {
        this.left = left;
    }

    public AvlTreeNode<T> getRight() {
        return right;
    }

    public void setRight(AvlTreeNode<T> right) {
        this.right = right;
    }

    public T getVal() {
        return val;
    }

    public void setVal(T val) {
        this.val = val;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }
}
