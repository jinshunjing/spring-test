package org.jsj.my.exception;

import org.jsj.my.constant.ErrorCode;

/**
 * 超时错误
 *
 * @author JSJ
 */
public class TimeoutException extends InternalServerException {

    public TimeoutException() {
        super(ErrorCode.TIMEOUT_ERROR, "Request timeout");
    }

    public TimeoutException(String msg) {
        super(ErrorCode.TIMEOUT_ERROR, msg);
    }

    public TimeoutException(int code, String msg) {
        super(code, msg);
    }

    public TimeoutException(int code) {
        super(code, "Request timeout");
    }

}
