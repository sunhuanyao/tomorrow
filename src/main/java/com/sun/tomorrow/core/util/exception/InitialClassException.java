package com.sun.tomorrow.core.util.exception;

/**
 * @Author roger sun
 * @Date 2019/11/11 16:35
 */
public class InitialClassException extends RuntimeException{

    public InitialClassException() {
        super("initial class exception, you should new it before use!");
    }
}
