package com.sun.tomorrow.core.base;

import sun.reflect.generics.tree.Tree;

/**
 * @Author roger sun
 * @Date 2019/11/7 16:39
 */
public class AvlTreeNode<T> extends TreeNode {

    private AvlTreeNode left;
    private AvlTreeNode right;
    private T val;

    public AvlTreeNode(AvlTreeNode left, AvlTreeNode right, T val) {
        this.left = left;
        this.right = right;
        this.val = val;
    }

    public AvlTreeNode(T val) {
        this.val = val;
    }

    public AvlTreeNode getLeft() {
        return left;
    }

    public void setLeft(AvlTreeNode left) {
        this.left = left;
    }

    public AvlTreeNode getRight() {
        return right;
    }

    public void setRight(AvlTreeNode right) {
        this.right = right;
    }

    public T getVal() {
        return val;
    }

    public void setVal(T val) {
        this.val = val;
    }
}
