/**
 * @(#) ExceptionHandler.java Created on 2012-3-30
 * <p/>
 * Copyright (c) 2012 Aspire. All Rights Reserved
 */
package com.keenbrace.exception;

/**
 * The class <code>ExceptionHandler</code>
 *
 * @author linjunsui
 * @version 1.0
 */
public class ExceptionHandler {

    /**
     * handle a exception
     * @param position additional message
     * @param e
     * @return Exception while need to re-throws exception
     */
    static Exception handle(String position, Exception e) {
        return e;
    }
}
