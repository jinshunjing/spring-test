package org.jsj.my.exception;

import org.jsj.my.constant.ErrorCode;

/**
 * 涵盖所有的网络错误
 *
 * @author JSJ
 */
public class NetworkException extends InternalServerException {

    public NetworkException() {
        super(ErrorCode.NETWORK_ERROR, "Network error");
    }

    public NetworkException(String msg) {
        super(ErrorCode.NETWORK_ERROR, msg);
    }

    public NetworkException(int code, String msg) {
        super(code, msg);
    }

    public NetworkException(int code) {
        super(code, "Network error");
    }

}
