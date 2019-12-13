package com.sun.tomorrow.core.util.exception;

/**
 * @Author roger sun
 * @Date 2019/12/13 16:45
 */
public class EmptyArrayException extends RuntimeException {

    public EmptyArrayException(){
        super("empty array!");
    }
}
