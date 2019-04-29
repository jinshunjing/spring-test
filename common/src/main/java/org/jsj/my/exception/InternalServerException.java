package org.jsj.my.exception;

import org.jsj.my.constant.ErrorCode;

/**
 * 涵盖所有的服务端技术错误
 *
 * @author JSJ
 */
public class InternalServerException extends BaseException {

    public InternalServerException() {
        super(ErrorCode.SERVER_ERROR, "Internal Server Error");
    }

    public InternalServerException(String msg) {
        super(ErrorCode.SERVER_ERROR, msg);
    }

    public InternalServerException(int code, String msg) {
        super(code, msg);
    }

}
