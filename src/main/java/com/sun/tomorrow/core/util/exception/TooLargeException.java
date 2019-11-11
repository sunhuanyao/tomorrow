package com.sun.tomorrow.core.util.exception;


import com.sun.tomorrow.core.base.BTreeNode;

/**
 * @Author roger sun
 * @Date 2019/11/11 16:17
 */
public class TooLargeException extends RuntimeException {


    public TooLargeException() {
        super("Too large array List for build, " + BTreeNode.MAXENTRYARRAYS + " expect.");
    }

    public TooLargeException(String message) {
        super(message);
    }
}
