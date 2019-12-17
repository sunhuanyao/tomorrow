package com.sun.tomorrow.core.tool.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author roger sun
 * @Date 2019/12/17 18:24
 */
public class RecTreeNode<T1 extends RecTreeNode, T2> implements Serializable {
    private List<T1> childs = new ArrayList<>();

    private T2 id;

    private T2 pId;
    //是否展开
    private boolean expand;

    //是否被选中   用于关联组织
    private boolean check;
    //是否可以被点击
    private boolean disabled;



    public boolean isDisabled() {
        return disabled;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

    public boolean isCheck() {
        return check;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }

    public List<T1> getChilds() {
        return childs;
    }

    public void setChilds(List<T1> childs) {
        this.childs = childs;
    }

    public T2 getId() {
        return id;
    }

    public void setId(T2 id) {
        this.id = id;
    }

    public boolean isExpand() {
        return expand;
    }

    public void setExpand(boolean expand) {
        this.expand = expand;
    }

    public T2 getpId() {
        return pId;
    }

    public void setpId(T2 pId) {
        this.pId = pId;
    }
}
