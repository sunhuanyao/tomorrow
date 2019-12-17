package com.sun.tomorrow.core.tool.algo;

/**
 * @Author roger sun
 * @Date 2019/12/17 18:22
 */

import com.sun.tomorrow.core.tool.domain.BaseTreeRec;
import com.sun.tomorrow.core.tool.domain.RecTreeNode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 递归树逻辑
 *
 * 用法：
 * 继承treeNode 类，然后，先全部查出来，进行递归。
 *
 * @param
 */
public class RecurTree<T1 extends RecTreeNode<T1, T2>, T2> extends BaseTreeRec<T1, T2> {

    public List<T1> treeList = new ArrayList<>();


    public List<T1> getTree(List<T1> list, T2 id){
        treeList = list;
        if(treeList == null) {
            return null;
        }
        List<T1> list1 = new ArrayList<>();
        list1.add(recursiveTree(id));
        return list1;

    }


    public Map<T2, T1> build(){
        Map<T2, T1> idx = new HashMap<>();
        T1 tmp = null;
        for(int i = 0 ; i < treeList.size() ; ++ i){
            tmp = treeList.get(i);
            idx.put(tmp.getId(), tmp);
        }
        return idx;
    }

    public T1 getNodeById(T2 id){
        return build().get(id);
    }

    public List<T1> getTreeChildNode(T2 id){
        List<T1> list = new ArrayList<>();
        if( null != treeList){
            for(T1 tmp : treeList){
                if(null != id){
                    if(id.equals(tmp.getpId())){
                        list.add(tmp);
                    }
                }
            }
        }
        return list;
    }
}