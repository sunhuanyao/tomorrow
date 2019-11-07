package com.sun.tomorrow.core;

import com.sun.tomorrow.core.container.AvlTreeFactory;

/**
 * @Author roger sun
 * @Date 2019/11/7 16:21
 */
public class test {

    public static void main(String[] args){
        AvlTreeFactory avlTreeFactory = new AvlTreeFactory() {
            @Override
            public int cmp(Object v1, Object v2) {
                return 0;
            }
        }
    }

}
