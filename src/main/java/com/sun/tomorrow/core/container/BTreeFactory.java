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
     * 该二分用作 寻找 插入节点应该插入的位置
     * @param orgin  原数组
     * @param valIndex  数组包含真实数大小
     * @param key  插入值
     * @return 位置
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

    /**
     * 将 key 插入到origin[index]
     * @param origin 即将插入的数组
     * @param key    插入的数
     * @param index  插入的位置
     * @return  完成操作的数组
     */
    public Object[] insertIntoArray(Object[] origin, T key, int index){
        if(origin.length > cap) throw new ArrayIndexOutOfBoundsException("beyond the cap: " + cap);
        for (int i = origin.length - 1; i > index; i --){
            origin[i] = origin[i - 1];
        }
        origin[index] = key;
        return origin;
    }

    /**
     * 完成插入操作
     * @param root
     * @param key
     * @return
     */
    public BTreeNode<T> addIntoLevelNode(BTreeNode<T> root, T key){
        int index = midFind(root.getVals(), root.getNowValIndex(), key);
        root.setVals(insertIntoArray(root.getVals(), key, index));
        root.valIndexIncrease();
        return root;
    }
    // 获取  分裂后，左节点的长度
    private int getLeftSplitNodeLength(int tmp){
        return (tmp + 1) / 2;
    }


    //获取 分裂后， 右节点的起始位置
    private int getRightStartIndex(int tmp){
        return getLeftSplitNodeLength(tmp) + 1;
    }

    public BTreeNode<T> splitNode(BTreeNode<T> root){
        BTreeNode<T> newRoot = new BTreeNode<>(cap);
        Object[] left = new Object[cap];
        Object[] right = new Object[cap];
        System.arraycopy( root.getVals(),
                0,
                left,
                0,
                getLeftSplitNodeLength(root.getNowValIndex())
        );

        System.arraycopy( root.getVals(),
                getRightStartIndex(root.getNowValIndex()),
                right,
                0,
                root.getNowValIndex() - getLeftSplitNodeLength(root.getNowValIndex())
        );
        BTreeNode[] leftChild = new BTreeNode[cap + 1];
        BTreeNode[] rightChild = new BTreeNode[cap + 1];

        System.arraycopy(
                root.getChilds(),
                0,
                leftChild,
                0,
                getLeftSplitNodeLength(root.getNowValIndex()) + 1
        );



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
