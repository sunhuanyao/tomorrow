package com.sun.tomorrow.core.container;

import com.google.gson.Gson;
import com.sun.tomorrow.core.base.AvlTreeNode;
import sun.java2d.loops.GraphicsPrimitiveProxy;
import sun.util.locale.provider.AvailableLanguageTags;

import javax.swing.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * @Author roger sun
 * @Date 2019/11/7 17:21
 */
public abstract class AvlTreeFactory<T> extends TreeFactory<T> implements TreeFactoryMethod<T>{

    public AvlTreeNode<T> avlTreeNode;

    public void build(){

    }

    public void add(T key){
        AvlTreeNode<T> nowNode = new AvlTreeNode<>(key);
        if(avlTreeNode == null){
            avlTreeNode = nowNode;
            return ;
        }
        avlTreeNode = findPosition(avlTreeNode, key);

    }

    private AvlTreeNode<T> findPosition(AvlTreeNode<T> root, T val){

        if(cmp(root.getVal(), val) < 0){
            if(root.getRight()==null){
                root.setRight(new AvlTreeNode<>(val));
                root.setHeight(2);
                return root;
            }
            root.setRight(findPosition(root.getRight(), val));
        }else{
            if(root.getLeft()==null){
                root.setLeft(new AvlTreeNode<>(val));
                root.setHeight(2);
                return root;
            }
            root.setLeft(findPosition(root.getLeft(), val));
        }
        root = keepBanlance(root);
        root.setHeight(maintainHeight(root));
        return root;
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
        avlTreeNode.setHeight(maintainHeight(avlTreeNode));
    }

    private void findAndDelete(AvlTreeNode<T> root, T val, AvlTreeNode<T> prefix, int type){
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

            // 右节点为空 或者  左节点的高度 > 右节点的高度  则 让左子树的最大节点来代替
            if(root.getRight() == null || (root.getLeft() != null &&root.getLeft().getHeight() >= root.getRight().getHeight())){
                AvlTreeNode<T> tmp = getRightTreeNodes(root.getLeft().getRight(), root.getLeft());
//                tmp.setLeft(null);
                if(tmp!=null) {
                    tmp.setLeft(root.getLeft());
                    tmp.setHeight(maintainHeight(tmp));
                }else{
                    tmp = root.getLeft();
                }
                if(type == 1){
                    prefix.setLeft(tmp);
                }else{
                    prefix.setRight(tmp);
                }
            }else{ //  左节点的高度 <= 右节点的高度  则 让右子树的最小节点来代替
                AvlTreeNode<T> tmp = getLeftTreeNodes(root.getRight().getLeft(), root.getRight());
//                tmp.setLeft(null);
                if(tmp != null) {
                    tmp.setRight(root.getRight());
                    tmp.setHeight(maintainHeight(tmp));
                }else{
                    tmp = root.getRight();
                }
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
    //维护 树高
    private int maintainHeight(AvlTreeNode<T> root){
        int left = root.getLeft() == null? 0: root.getLeft().getHeight();
        int right = root.getRight() == null? 0: root.getRight().getHeight();

        return Math.max(left, right) + 1;
    }

    //比较两个子节点的高度差
    private int getDisBetweenTwoNode(AvlTreeNode<T> root){
        int left = root.getLeft() == null? 0: root.getLeft().getHeight();
        int right = root.getRight() == null? 0: root.getRight().getHeight();
        return left - right;
    }

    private AvlTreeNode<T> keepBanlance(AvlTreeNode<T> root){

        int dis = getDisBetweenTwoNode(root);
        if(dis < 2 && dis > -2){
            return root;
        }
        if(dis >= 2){
            // 左子树 height > 右子树 height  翻转左子树
            // LL 或 LR
            int disLeft = getDisBetweenTwoNode(root.getLeft());
            if(disLeft > 0){
                //LL
                return LL(root);
            }else{
                root = LR(root);
                return LL(root);
            }
        }else{
            int disRight = getDisBetweenTwoNode(root.getRight());
            if(disRight > 0){
                //LL
                root = RL(root);
                return RR(root);
            }else{
                return RR(root);
            }
        }
    }

    private boolean isOk(AvlTreeNode<T> root){
        int dis = getDisBetweenTwoNode(root);
        return dis < 2 && dis > -2;
    }

    private AvlTreeNode<T> RR(AvlTreeNode<T> root){
        AvlTreeNode<T> right = root.getRight();
        root.setRight(right.getLeft());
        root.setHeight(maintainHeight(root));
        right.setLeft(root);
        right.setHeight(maintainHeight(right));

        return right;
    }

    private AvlTreeNode<T> LL(AvlTreeNode<T> root){
        AvlTreeNode<T> left = root.getLeft();
        root.setLeft(left.getRight());
        root.setHeight(maintainHeight(root));
        left.setRight(root);
        left.setHeight(maintainHeight(left));
        return left;
    }

    private AvlTreeNode<T> RL(AvlTreeNode<T> root){
        AvlTreeNode<T> right = root.getRight();
        AvlTreeNode<T> rightLeft = root.getRight().getLeft();
        root.setRight(right.getLeft());
        right.setLeft(rightLeft.getRight());
        rightLeft.setRight(right);
        right.setHeight(maintainHeight(right));
        rightLeft.setHeight(maintainHeight(rightLeft));
        root.setHeight(maintainHeight(root));
        return root;
    }

    private AvlTreeNode<T> LR(AvlTreeNode<T> root){
        AvlTreeNode<T> left = root.getLeft();
        AvlTreeNode<T> leftRight = root.getLeft().getRight();
        root.setLeft(left.getRight());

        left.setRight(leftRight.getLeft());
        leftRight.setLeft(left);

        left.setHeight(maintainHeight(left));
        leftRight.setHeight(maintainHeight(leftRight));
        root.setHeight(maintainHeight(root));
        return root;
    }


    /**
     *  获取 左子树的 做大节点
     * @param root
     * @return
     */
    private AvlTreeNode<T> getRightTreeNodes(AvlTreeNode<T> root, AvlTreeNode<T> prefix){
        if( root ==null) return null;
        if(root.getRight()==null){
            prefix.setRight(root.getLeft());
            prefix.setHeight(maintainHeight(prefix));
            return root;
        }
        AvlTreeNode<T> res = getRightTreeNodes(root.getRight(), root);
        res.setHeight(maintainHeight(res));
        return res;
    }

    /**
     * 获取  右子树的 最大节点
     * @param root
     * @param prefix
     * @return
     */
    private AvlTreeNode<T> getLeftTreeNodes(AvlTreeNode<T> root, AvlTreeNode<T> prefix){
        if(root == null) return null;
        if(root.getLeft()==null){
            prefix.setLeft(root.getRight());
            prefix.setHeight(maintainHeight(prefix));
            return root;
        }
        AvlTreeNode<T> res = getLeftTreeNodes(root.getLeft(), root);
        res.setHeight(maintainHeight(res));
        return res;
    }

    //  用于展现检测
    private Gson gson = new Gson();
    public String toString(){
        return gson.toJson(this.avlTreeNode);
    }


}
