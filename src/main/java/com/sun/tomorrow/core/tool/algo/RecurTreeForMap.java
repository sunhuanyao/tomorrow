package com.sun.tomorrow.core.tool.algo;

import com.sun.tomorrow.core.tool.domain.BaseTreeRec;
import com.sun.tomorrow.core.tool.domain.RecTreeNode;
import com.sun.tomorrow.core.util.CloneUtils;

import java.util.*;

/**
 * @Author roger sun
 * @Date 2019/12/17 18:25
 */
public class RecurTreeForMap<T1 extends RecTreeNode<T1, T2>, T2> extends BaseTreeRec<T1, T2> {
    private Map<T2, T1> treeMap = new HashMap<>();
    //    public Map<T2, T1> Z05 = new HashMap<>();
    private T2 rootId;

    //优化 提前生成树结构

    private Map<T2, List<T1>> childsMap = new HashMap<>();

    public RecurTreeForMap(
            Map<T2, T1> tmp
    ){
        this.treeMap = CloneUtils.deepClone(tmp);
    }
    public RecurTreeForMap(
            Map<T2, T1> tmp,
            T2 rootId
    ){
        this.treeMap = CloneUtils.deepClone(tmp);
        this.rootId = rootId;
    }

    public RecurTreeForMap(
            Map<T2, T1> tmp,
            T2 rootId,
            Map<T2, List<T1>> tmpChildsMap
    ){
        this.treeMap = CloneUtils.deepClone(tmp);
        this.rootId = rootId;
        childsMap = tmpChildsMap;
    }

    //遍历整颗数

//    public void generateTree(T2 id){
//
//        List<T1> childs = getTreeChildNode(id);
//        baseTree.put(id, childs);
//        for(int i = 0; i < childs.size();  ++ i){
//
//        }
//
//    }



    /**
     *   获取父亲节点层级,层级情况如下图所示
     *   /|-
     *    |-
     *       -
     *         -
     *         -
     *           -
     *             -（当前需要返回的层级Id,传入的参数）
     *             -
     *         -
     *       -
     *       -
     *    |-
     * @param id
     * @return
     */

    public List<T1> getFatherTree( T2 id ){
        if (rootId == null ) throw new NullPointerException("rootId is null");
        List<T1> childs = null;
        T1 tmp = treeMap.get(id);
        while(!tmp.getpId().equals(rootId)){
            T2 pKey = tmp.getpId();
            T1 tmpP = treeMap.get(pKey);
            childs = getTreeChildNode(pKey);

            for(int i = 0 ; i < childs.size(); ++ i){
                T1 child = childs.get(i);

                if(child.getId().equals(tmp.getId())){
                    childs.set(i, tmp);
                }

                if(getTreeChildNode(childs.get(i).getId()).size() == 0){
                    childs.get(i).setChilds(null);
                }

            }
            tmpP.setChilds(childs);
            if(childs.size() > 0)
                tmpP.setExpand(true);
            tmp = tmpP;
        }

        childs = getTreeChildNode(rootId);
        for(int i = 0 ; i < childs.size(); ++ i){
            T1 child = childs.get(i);
            if(child.getId().equals(tmp.getId())){
                childs.set(i, tmp);
            }
        }

        return new ArrayList<>(childs);

    }

    /**
     * 获取所有子节点
     * @return
     */
    public void getChildTreeList(T2 id, List<T2> res){
        res.add(id);
        List<T1> now_childs = getTreeChildNode(id);
        if( now_childs != null && now_childs.size() > 0) {
            for (int i = 0; i < now_childs.size(); ++i) {
                getChildTreeList(now_childs.get(i).getId(), res);
            }
        }

    }


    @Override
    public T1 getNodeById(T2 id){
        return treeMap.get(id);
    }


    /**
     * 该部分做了一个记忆化优化。
     *
     * @param id
     * @return
     */
    @Override
    public List<T1> getTreeChildNode(T2 id) {

        List<T1> list = new ArrayList<>();
        if (childsMap.containsKey(id)) {
            list.addAll(CloneUtils.deepClone(childsMap.get(id)));
            return list;
        }
        Iterator<Map.Entry<T2, T1>> iter = treeMap.entrySet().iterator();

        List<T1> baseList = new ArrayList<>();
        while (iter.hasNext()) {
            Map.Entry<T2, T1> entry = iter.next();
            T2 tmpKey = entry.getKey();
            T1 tmpValue = entry.getValue();
            T1 tmpBaseValue;
            if (id.equals(tmpValue.getpId())) {
                list.add(tmpValue);
                tmpBaseValue = CloneUtils.deepClone(tmpValue);

                tmpBaseValue.setExpand(false);
                tmpBaseValue.setChilds(new ArrayList<>());
                tmpBaseValue.setCheck(false);
                tmpBaseValue.setDisabled(false);
                baseList.add(tmpBaseValue);
            }
        }
        childsMap.put(id, baseList);
        return list;

    }

    public Map<T2, T1> getTreeMap() {
        return treeMap;
    }

    public void setTreeMap(Map<T2, T1> treeMap) {
        this.treeMap = treeMap;
    }

    public T2 getRootId() {
        return rootId;
    }

    public void setRootId(T2 rootId) {
        this.rootId = rootId;
    }
}
