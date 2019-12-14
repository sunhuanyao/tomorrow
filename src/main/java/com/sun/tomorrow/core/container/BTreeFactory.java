package com.sun.tomorrow.core.container;

import com.sun.tomorrow.core.base.BTreeNode;
import com.sun.tomorrow.core.util.SafeGson;
import com.sun.tomorrow.core.util.TreeNodeTools;
import com.sun.tomorrow.core.util.exception.EmptyArrayException;
import com.sun.tomorrow.core.util.exception.InitialClassException;
import com.sun.tomorrow.core.util.exception.RepeatingFieldException;
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
        this.bTreeNode = findPosition(this.bTreeNode, key);
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
        while(l <= r){
            int m = ( l + r ) >> 1;
            int dis = cmp((T)orgin[m], key);   //  orgin[m] - key
            if(dis == 0){
                throw new RepeatingFieldException();
            }
            if(dis > 0){   // orgin[m] > key
                r = m - 1;
            }else{
                l = m + 1;
            }

        }
        return l;
    }

    /**
     * 完成插入操作
     * @param root
     * @param key
     * @return
     */
    public BTreeNode<T> addIntoLevelNode(BTreeNode<T> root, T key){
        int index = midFind(root.getVals(), root.getNowValIndex(), key);
        root.setVals(TreeNodeTools.insertIntoArray(root.getVals(), key, index, cap));
        root.valIndexIncrease();
        return root;
    }

    /**
     * 分裂节点
     * @param root
     * @return
     */
    public BTreeNode<T> splitNode(BTreeNode<T> root){

        Object[] left = new Object[cap];
        Object[] right = new Object[cap];
        System.arraycopy( root.getVals(),
                0,
                left,
                0,
                root.getLeftSplitNodeLength()
        );

        System.arraycopy( root.getVals(),
                root.getRightStartIndex(),
                right,
                0,
                root.getRightSplitNodeLength()
        );
        BTreeNode[] leftChild = new BTreeNode[cap + 1];
        BTreeNode[] rightChild = new BTreeNode[cap + 1];

        System.arraycopy(
                root.getChilds(),
                0,
                leftChild,
                0,
                root.getLeftSplitChildsNodeLength()
        );

        System.arraycopy(
                root.getChilds(),
                root.getRightStartChildsIndex(),
                rightChild,
                0,
                root.getRightSplitChildsNodeLength()

        );
        return new BTreeNode<>(
                root.getMidValArray(),
                1,
                root.getLevel() + 1,
                root.generateNewChildsNode(
                        new BTreeNode<>(
                                left,
                                root.getLeftSplitNodeLength(),
                                root.getLevel(),
                                leftChild
                        ),
                        new BTreeNode(
                                right,
                                root.getRightSplitNodeLength(),
                                root.getLevel(),
                                rightChild
                        )
                )
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

    @SuppressWarnings("unchecked")
    public BTreeNode<T> findPosition(BTreeNode<T> root, T key){
//        if ()
        //遍历到叶子节点
        if(root.getLevel() == 1 ) {
            root = addIntoLevelNode(root, key);
            if(root.getNowValIndex() >= cap){
                return splitNode(root);
            }
            return root;
        }

        int index = midFind(root.getVals(), root.getNowValIndex(), key);
        BTreeNode[] childs = root.getChilds();

        BTreeNode<T> tmp = findPosition(childs[index], key);
        if(root.getLevel() == tmp.getLevel()) {
            root = merge(root, tmp);
        }
        if(root.getNowValIndex() >= cap){
            return splitNode(root);
        }
        return root;
    }

    /**
     * 合并两个节点 -- 分裂后的操作
     * @param tmp1
     * @param tmp2
     * @return
     */
    @SuppressWarnings("unchecked")
    public BTreeNode<T> merge(BTreeNode<T> tmp1, BTreeNode<T> tmp2){

        Object[] tmp2Vals = tmp2.getVals();
        if(tmp2Vals == null) throw new EmptyArrayException();
        int index = midFind(tmp1.getVals(), tmp1.getNowValIndex(), (T)tmp2Vals[0]);

        tmp1.setVals(TreeNodeTools.insertIntoArray(tmp1.getVals(), tmp2.getVals(), index, tmp1.getNowValIndex(), tmp2.getNowValIndex(), cap));
        tmp1.setChilds(
            TreeNodeTools.coverAndAdd(
                    tmp1.getChilds(),
                    tmp2.getChilds(),
                    index,
                    tmp1.getNowValIndex() + 1,
                    tmp2.getNowValIndex() + 1,
                    cap
            )
        );
        tmp1.valIndexIncrease(tmp2.getNowValIndex());
        return tmp1;
    }


    /**
     * 删除树节点
     * @param key
     */

    public void delete(T key){}


    @Override
    public String toString() {
        return SafeGson.toJson(this.bTreeNode);
    }
}
