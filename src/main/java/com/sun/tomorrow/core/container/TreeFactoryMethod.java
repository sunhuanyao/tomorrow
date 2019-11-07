package com.sun.tomorrow.core.container;

import com.sun.tomorrow.core.base.TreeNode;

/**
 * @Author roger sun
 * @Date 2019/11/7 16:43
 */
public interface TreeFactoryMethod{
    /**
     * 初始化 树结构
     */
    public void build();

    /**
     * 添加树节点
     * @param key
     */
    public void add(Object key);

    /**
     * 删除树节点
     * @param key
     */

    public void delete(Object key);

}
