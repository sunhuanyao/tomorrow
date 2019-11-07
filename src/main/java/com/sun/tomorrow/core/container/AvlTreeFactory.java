package com.sun.tomorrow.core.container;

import com.sun.tomorrow.core.base.AvlTreeNode;
import sun.util.locale.provider.AvailableLanguageTags;

import javax.swing.*;

/**
 * @Author roger sun
 * @Date 2019/11/7 17:21
 */
public abstract class AvlTreeFactory<T> extends TreeFactory<T> implements TreeFactoryMethod<T>{

    public AvlTreeNode<T> avlTreeNode;

    public void build(){

    }

    public void add(T key){
        AvlTreeNode nowNode = new AvlTreeNode(key);
        if(avlTreeNode == null){
            avlTreeNode = nowNode;
            return ;
        }
        findPosition(avlTreeNode, key);

    }

    private void findPosition(AvlTreeNode root, T val){

        if(cmp(root.getVal(), val) < 0){
            if(root.getRight()==null){
                root.setRight(new AvlTreeNode(val));
                return ;
            }
            findPosition(root.getRight(), val);
        }else{
            if(root.getLeft()==null){
                root.setLeft(new AvlTreeNode(val));
                return ;
            }
            findPosition(root.getLeft(), val);
        }
//        int max = root.getLeft()==null?
//                root.getRight().getHeight(): (
//                        root.getRight()==null?
//                                root.getLeft().getHeight():Math.max(root.getLeft().getHeight(),root.getRight().getHeight()));
        root.setHeight(maintainHeight(root));
    }

    public void delete(T key){
        if(avlTreeNode==null){
            return ;
        }
        if(cmp(avlTreeNode.getVal(), key) == 0){
            avlTreeNode = null;
            return;
        }
        if(cmp(avlTreeNode.getVal(), key) < 0 && avlTreeNode.getRight()!=null){
            findAndDelete(avlTreeNode.getRight(), key, avlTreeNode, 2);
        }else{
            findAndDelete(avlTreeNode.getLeft(), key, avlTreeNode, 1);
        }
    }

    public void findAndDelete(AvlTreeNode root, T val, AvlTreeNode prefix, int type){
        if(root == null) return ;
        if(cmp(root.getVal(), val) == 0){
            if(root.getLeft() == null && root.getRight() ==null){
                if(type == 1){
                    prefix.setLeft(null);
                }else{
                    prefix.setRight(null);
                }
                return ;
            }

            // 右节点为空 或者  左节点的高度 > 右节点的高度  则 让左节点的最右子节点来代替
            if(root.getRight() == null || root.getLeft().getHeight() >= root.getRight().getHeight()){
                AvlTreeNode tmp = getRightTreeNodes(root.getLeft().getRight(), root.getLeft());
//                tmp.setLeft(null);
                tmp.setLeft(root.getLeft());
                if(type == 1){
                    prefix.setLeft(tmp);
                }else{
                    prefix.setRight(tmp);
                }
            }else{
                AvlTreeNode tmp = getLeftTreeNodes(root.getRight().getLeft(), root.getRight());
//                tmp.setLeft(null);
                tmp.setRight(root.getRight());
                tmp.setHeight(maintainHeight(tmp));
                if(type == 1){
                    prefix.setLeft(tmp);
                }else{
                    prefix.setRight(tmp);
                }
            }
            return;
        }
        if(cmp(root.getVal(), val) < 0){
            findAndDelete(root.getRight(), val, root, 2);
        }else{
            findAndDelete(root.getLeft(), val, root, 1);
        }
        root.setHeight(maintainHeight(root));
    }

    public int maintainHeight(AvlTreeNode root){
        int max = root.getLeft()==null?
                root.getRight().getHeight(): (
                root.getRight()==null?
                        root.getLeft().getHeight():Math.max(root.getLeft().getHeight(),root.getRight().getHeight()));
        return max;
    }

    /**
     *  获取  子节点 右叶子结点
     * @param root
     * @return
     */
    private AvlTreeNode getRightTreeNodes(AvlTreeNode root, AvlTreeNode prefix){
        if(root.getRight()==null){
            prefix.setRight(root.getLeft());
            prefix.setHeight(maintainHeight(prefix));
            return root;
        }
        AvlTreeNode res = getRightTreeNodes(root.getRight(), root);
        res.setHeight(maintainHeight(res));
        return res;
    }

    private AvlTreeNode getLeftTreeNodes(AvlTreeNode root, AvlTreeNode prefix){
        if(root.getLeft()==null){
            prefix.setLeft(root.getRight());
            prefix.setHeight(maintainHeight(prefix));
            return root;
        }
        AvlTreeNode res = getLeftTreeNodes(root.getLeft(), root);
        res.setHeight(maintainHeight(res));
        return res;
    }


}
