package com.sun.tomorrow.core.container;

import com.sun.tomorrow.core.base.BTreeNode;
import com.sun.tomorrow.core.util.exception.InitialClassException;
import com.sun.tomorrow.core.util.exception.TooLargeException;

/**
 * @Author roger sun
 * @Date 2019/11/11 16:29
 */
public abstract class BTreeFactory<T> extends TreeFactory<T> implements TreeFactoryMethod<T>{

    private BTreeNode<T> bTreeNode;

    private int cap;

    /**
     * 初始化 树结构
     */
    public BTreeFactory() {
        this(BTreeNode.DefaultOrderNumber);
    }

    public BTreeFactory(int cap) {
        this.bTreeNode = new BTreeNode<>(cap);
        this.cap = cap;
    }

    public BTreeFactory(BTreeNode<T> bTreeNode, int cap) {
        this.bTreeNode = bTreeNode;
        this.cap = cap;
    }
    public void build(){

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


    public boolean compare(T now, T key){

        if( cmp(key, now) == 0){
            return true;
        }
        return false;
    }

    /**
     * 该二分用作
     * @param orgin
     * @param valIndex
     * @param key
     * @return
     */
    @SuppressWarnings("unchecked")
    public int midFind(Object[] orgin, int valIndex, T key){
        int l = 0;
        int r = valIndex - 1;
        while(l < r){
            int m = ( l + r ) >> 1;
            int dis = cmp((T)orgin[m], key);
            if(dis == 0){
                return m;
            }
            if(dis > 0){
                r = m;
            }else{
                l = m + 1;
            }

        }
        return r;
    }

    public Object[] insertIntoArray(Object[] origin, T key, int index){
        if(origin.length > cap) throw new ArrayIndexOutOfBoundsException("beyond the cap: " + cap);
        for (int i = origin.length - 1; i > index; i --){
            origin[i] = origin[i - 1];
        }
        origin[index] = key;
        return origin;
    }

    public BTreeNode<T> addIntoLevelNode(BTreeNode<T> root, T key){
        int index = midFind(root.getVals(), root.getNowValIndex(), key);
        root.setVals(insertIntoArray(root.getVals(), key, index));
        root.valIndexIncrease();
        return root;
    }

    public BTreeNode<T> splitNode(BTreeNode<T> root){





    }

    private Object[] subArrays(Object[] obj, int start, int end){
        Object[] btNode = new Object[cap] ;
        int pos = 0;
        for(int i = start ; i < end; ++ i){
            btNode[pos ++] = obj[i];
        }
        return btNode;
    }

    public BTreeNode<T> findPosition(BTreeNode<T> root, T key){
//        if ()
        //遍历到叶子节点
        if(root.getLevel() == 1 ) {
            root = addIntoLevelNode(root, key);
            if(root.getNowValIndex() >= cap - 1){
                return splitNode(root);
            }
            return root;
        }



    }

    /**
     * 删除树节点
     * @param key
     */

    public void delete(T key){}


}
