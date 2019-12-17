package com.sun.tomorrow.core.tool.domain;

import java.util.List;

/**
 * @Author roger sun
 * @Date 2019/12/17 18:23
 */
public abstract class BaseTreeRec<T1 extends RecTreeNode<T1, T2>, T2> {
    public T1 recursiveTree(T2 id){

        T1 node = getNodeById(id);
        List<T1> childs = getTreeChildNode(id);
        if (childs == null || childs.size() == 0){
            node.setChilds(null);
        }else {
            for (T1 tmp : childs) {
                T1 tmpNode = recursiveTree(tmp.getId());
                node.getChilds().add(tmpNode);
            }
        }
        return node;

    }

    public abstract T1 getNodeById(T2 id);
    public abstract List<T1> getTreeChildNode(T2 id);
}
