package com.sun.tomorrow.core;

import com.sun.tomorrow.core.base.Rectangle;
import com.sun.tomorrow.core.service.ExecutorLocalService;

import java.util.Arrays;
import java.util.List;

/**
 * @Author roger sun
 * @Date 2019/11/7 16:21
 */
public class test {

    public static List<Integer> t1 = Arrays.asList(5, 2, 8,3,1,7,9, 4 , 6);

    public class TestRect extends Rectangle {

        public int val;

        public TestRect(int minx, int miny, int maxx, int maxy, int val) {
            super(minx, miny, maxx, maxy);
            this.val = val;
        }

        @Override
        public String toString() {
            return val + "--";
        }
    }


    public void task1(){
        System.out.println("1");
    }
    public void task2(){
        System.out.println("2");
    }
    public void task3(){
        System.out.println("3");
    }


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

//        BTreeFactory<Integer> bTreeFactory = new BTreeFactory<Integer>(3) {
//            @Override
//            public int cmp(Integer v1, Integer v2) {
//                return v1 - v2;
//            }
//        };
//        Integer[] a = {1, 3, 5, 7, 9, 11, 13, 15, 17};
//
//        for(int i = 0 ; i < a.length; ++ i){
//            bTreeFactory.add(a[i]);
//        }
//        System.out.println(bTreeFactory);

//        int now = bTreeFactory.midFind(a, 16);
//
//
//        System.out.println(now);
//        Integer[] b = (Integer[])bTreeFactory.insertIntoArray(a, 2, now);
//        System.out.println(b);
//
//        TestRect testRect = new test().new TestRect(1,2,3,4, 1);
//
//        TestRect testRect2 = new test().new TestRect(3,3,5,5, 2);
//        TestRect testRect3 = new test().new TestRect(11,2,12,4, 3);
//        TestRect testRect4 = new test().new TestRect(5,5,7,7, 4);
//
//        RTreeFactory<TestRect> rf = new RTreeFactory<>();
//        rf.init();
//        rf.add(testRect, 0);
//        rf.add(testRect2, 1);
//        rf.add(testRect3, 2);
//        rf.add(testRect4, 3);
//        System.out.println(rf.queryLevel(new Point(1, 3)));


        ExecutorLocalService executorLocalService = new ExecutorLocalService(3);

        executorLocalService.doInvoke(test.class, "task");


    }



}
