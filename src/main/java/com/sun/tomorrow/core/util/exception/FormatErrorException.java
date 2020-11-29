package com.sun.tomorrow.core.util.exception;

public class FormatErrorException extends RuntimeException{

    public FormatErrorException(){
        super();
    }

    public FormatErrorException(String msg){
        super("format is error: " + msg);

    }
}
