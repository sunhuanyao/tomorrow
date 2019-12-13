package com.sun.tomorrow.core;

import com.sun.tomorrow.core.base.AvlTreeNode;
import com.sun.tomorrow.core.container.AvlTreeFactory;
import com.sun.tomorrow.core.container.BTreeFactory;

import java.util.Arrays;
import java.util.List;

/**
 * @Author roger sun
 * @Date 2019/11/7 16:21
 */
public class test {

    public static List<Integer> t1 = Arrays.asList(5, 2, 8,3,1,7,9, 4 , 6);


    public static void main(String[] args){
//        AvlTreeFactory<Integer> avlTreeFactory = new AvlTreeFactory<Integer>() {
//            @Override
//            public int cmp(Integer v1, Integer v2) {
//                return v1 - v2;
//            }
//        };
//
//        for(int i = 0 ; i < t1.size(); ++ i){
//
//            avlTreeFactory.add(t1.get(i));
//
//        }
//
//        avlTreeFactory.delete(1);
//
////        avlTreeFactory.print();
//        System.out.println(avlTreeFactory.toString());

        BTreeFactory<Integer> bTreeFactory = new BTreeFactory<Integer>(3) {
            @Override
            public int cmp(Integer v1, Integer v2) {
                return v2 - v1;
            }
        };
        Integer[] a = {1, 3, 5, 7, 9, 11, 13, 15, 17};

        for(int i = 0 ; i < a.length; ++ i){
            bTreeFactory.add(a[i]);
        }
        System.out.println(bTreeFactory);

//        int now = bTreeFactory.midFind(a, 16);
//
//
//        System.out.println(now);
//        Integer[] b = (Integer[])bTreeFactory.insertIntoArray(a, 2, now);
//        System.out.println(b);
    }

}
