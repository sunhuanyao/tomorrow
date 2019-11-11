package com.sun.tomorrow.core.container;

import com.sun.tomorrow.core.base.BTreeNode;
import com.sun.tomorrow.core.util.exception.InitialClassException;

/**
 * @Author roger sun
 * @Date 2019/11/11 16:29
 */
public abstract class BTreeFactory<T> extends TreeFactory<T> implements TreeFactoryMethod<T>{

    private BTreeNode<T> bTreeNode;

    /**
     * 初始化 树结构
     */
    public void build(){
        bTreeNode = new BTreeNode<>();
    }

    public void build(int cap){
        bTreeNode = new BTreeNode<>(cap);
    }

    /**
     * 添加树节点
     * @param key
     */
    public void add(T key){
        if(bTreeNode == null){
            throw new InitialClassException();
        }



    }

    /**
     * 删除树节点
     * @param key
     */

    public void delete(T key){}

}
