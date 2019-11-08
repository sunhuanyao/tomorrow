package com.sun.tomorrow.core.container;

import com.google.gson.Gson;
import com.sun.tomorrow.core.base.AvlTreeNode;
import sun.awt.HToolkit;
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

    public void delete(T key){
        avlTreeNode = deleteByKey(avlTreeNode, key);

    }

    public AvlTreeNode<T> deleteByKey(AvlTreeNode<T> root, T key){
        if(cmp(root.getVal(), key) == 0){
            int dis = getDisBetweenTwoNode(root);
            AvlTreeNode<T> right = root.getRight();
            AvlTreeNode<T> left = root.getLeft();
            if(dis > 0){
                // 左子树 顶替
                addLeft(right, left.getRight());
                left.setRight(right);
                left.setHeight(maintainHeight(left));
                return left;
            }else{
                addRight(left, right.getLeft());
                right.setLeft(left);
                right.setHeight(maintainHeight(right));
                return right;
            }
        }
        if(cmp(root.getVal(), key) > 0){
            root.setLeft(deleteByKey(root.getLeft(), key));
            root.setRight(balanced(root.getRight()));
        }else{
            root.setRight(deleteByKey(root.getRight(), key));
            root.setLeft(balanced(root.getLeft()));
        }
        return root;
    }

    private AvlTreeNode<T> balanced(AvlTreeNode<T> root){

        int dis = getDisBetweenTwoNode(root);
        if(dis == 0){
            root.setHeight(maintainHeight(root));
            return root;
        }
        if(dis > 0){
            root.setLeft( balanced(root.getLeft()));
        }else{
            root.setRight( balanced(root.getRight()));
        }
        root = keepBanlance(root);
        root.setHeight(maintainHeight(root));
        return root;
    }

    private void addLeft(AvlTreeNode<T> root, AvlTreeNode<T> tmp){
        if(root.getLeft() == null){
            root.setLeft(tmp);
            root.setHeight(maintainHeight(root));
            return ;
        }
        addLeft(root.getLeft(), tmp);
        root.setHeight(maintainHeight(root));
    }
    private void addRight(AvlTreeNode<T> root, AvlTreeNode<T> tmp){
        if(root.getRight() == null){
            root.setRight(tmp);
            root.setHeight(maintainHeight(root));
            return ;
        }
        addRight(root.getRight(), tmp);
        root.setHeight(maintainHeight(root));
    }

    //  用于展现检测
    private Gson gson = new Gson();
    public String toString(){
        return gson.toJson(this.avlTreeNode);
    }


}
