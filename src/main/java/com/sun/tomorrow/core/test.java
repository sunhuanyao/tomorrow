package com.sun.tomorrow.core;

import com.sun.tomorrow.core.base.AvlTreeNode;
import com.sun.tomorrow.core.container.AvlTreeFactory;

/**
 * @Author roger sun
 * @Date 2019/11/7 16:21
 */
public class test {

    public static void main(String[] args){
        AvlTreeFactory<Integer> avlTreeFactory = new AvlTreeFactory<Integer>() {
            @Override
            public int cmp(Integer v1, Integer v2) {
                return v1 - v2;
            }
        };

        avlTreeFactory.add(5);
        avlTreeFactory.add(2);
        avlTreeFactory.add(7);
        avlTreeFactory.add(1);
        avlTreeFactory.add(3);
        avlTreeFactory.add(6);
        avlTreeFactory.add(4);
        avlTreeFactory.delete(5);

//        avlTreeFactory.print();
        System.out.println(avlTreeFactory.toString());


    }

}
