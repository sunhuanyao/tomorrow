package com.sun.tomorrow.core.container;

import com.google.gson.Gson;
import com.sun.tomorrow.core.base.AvlTreeNode;

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
    //维护当前节点的平衡
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
//                System.out.println("LL");
                return LL(root);
            }else{
//                System.out.println("LR and LL")
                return LL(LR(root));
            }
        }else{
            int disRight = getDisBetweenTwoNode(root.getRight());
            if(disRight > 0){
                //LL
//                System.out.println("RL and RR");
                return RR(RL(root));
            }else{
//                System.out.println("RR");
                return RR(root);
            }
        }
    }

    @Deprecated
    private boolean isOk(AvlTreeNode<T> root){
        int dis = getDisBetweenTwoNode(root);
        return dis < 2 && dis > -2;
    }

    //平衡 RR 操作
    private AvlTreeNode<T> RR(AvlTreeNode<T> root){
        AvlTreeNode<T> right = root.getRight();
        root.setRight(right.getLeft());
        root.setHeight(maintainHeight(root));
        right.setLeft(root);
        right.setHeight(maintainHeight(right));

        return right;
    }

    //平衡LL 操作
    private AvlTreeNode<T> LL(AvlTreeNode<T> root){
        AvlTreeNode<T> left = root.getLeft();
        root.setLeft(left.getRight());
        root.setHeight(maintainHeight(root));
        left.setRight(root);
        left.setHeight(maintainHeight(left));
        return left;
    }
    //平衡RL 操作
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
    // 平衡 LR 操作
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
    //删除节点操作

    /**
     *  删除操作描述：
     *     1.找到对应节点，然后选择最大高度子树节点顶替；
     *     2.顶替后，维护另一个兄弟节点的平衡:
     *          1). 若左子树节点代替： 则维护右子树的左子树
     *          2). 同理相反
     *     3.向上回溯维护树的平衡；
     * @param key
     */
    public void delete(T key){
//        System.out.println("delete -----------------------------------");
        avlTreeNode = deleteByKey(avlTreeNode, key);

        //  维护下树根
        avlTreeNode = keepBanlance(avlTreeNode);
    }

    //
    public AvlTreeNode<T> deleteByKey(AvlTreeNode<T> root, T key){
        int dis = getDisBetweenTwoNode(root);
        if(cmp(root.getVal(), key) == 0){

            AvlTreeNode<T> right = root.getRight();
            AvlTreeNode<T> left = root.getLeft();
            if(dis > 0){
                // 左子树顶替后，其右子树需要加入到其右兄弟节点的左子树
                addLeft(right, left.getRight());
                left.setRight(balanced(right, 1));
                left.setHeight(maintainHeight(left));
                // 顶替完后需要检查平衡并维护平衡 -- 左子树顶替  只需维护 右兄弟节点的左子树

                return left;
            }else{
                //右子树 顶替
                if(right == null){
                    return null;
                }
                addRight(left, right.getLeft());
//                顶替完后需要检查平衡并维护平衡 --
                right.setLeft(balanced(left, 2));
                right.setHeight(maintainHeight(right));
                return right;
            }

        }
        if(cmp(root.getVal(), key) > 0){
            root.setLeft(deleteByKey(root.getLeft(), key));
        }else{

            root.setRight(deleteByKey(root.getRight(), key));
        }

        // 回溯过程中 需要检查 是否满足 平衡 - 只需进行当前节点平衡

        root = keepBanlance(root);
        root.setHeight(maintainHeight(root));
        return root;
    }

    /**
     * 该节点子树的平衡维护 ：
     *
     * @param root
     * @return
     */
    private AvlTreeNode<T> balanced(AvlTreeNode<T> root, int type){

        int dis = getDisBetweenTwoNode(root);
        //叶子节点判断
        if(dis < 2 && (root.getLeft() == null || root.getRight() == null)){
            root.setHeight(maintainHeight(root));
            return root;
        }
        if(type == 1){
            root.setLeft( balanced(root.getLeft(), 1));
        }else{
            root.setRight( balanced(root.getRight(), 2));
        }
        //剪枝 - 避免无用操作
        if(dis >= 2 || dis <= -2)
            root = keepBanlance(root);
        root.setHeight(maintainHeight(root));
        return root;
    }
    //  在 root 上  加入 左子树
    private void addLeft(AvlTreeNode<T> root, AvlTreeNode<T> tmp){
        if(root == null) return ;
        if(root.getLeft() == null){
            root.setLeft(tmp);
            root.setHeight(maintainHeight(root));
            return ;
        }
        addLeft(root.getLeft(), tmp);
        root.setHeight(maintainHeight(root));
    }
    // 在root 上  加入  右子树
    private void addRight(AvlTreeNode<T> root, AvlTreeNode<T> tmp){

        if(root == null) return ;
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
