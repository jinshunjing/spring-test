package org.jsj.my.exception;

import org.jsj.my.constant.ErrorCode;

/**
 * 涵盖所有的前端输入参数错误
 *
 * @author JSJ
 */
public class BadArgumentException extends BaseException {

    public BadArgumentException() {
        super(ErrorCode.BAD_ARGUMENT_ERROR, "Bad Argument Error");
    }

    public BadArgumentException(String msg) {
        super(ErrorCode.BAD_ARGUMENT_ERROR, msg);
    }

    public BadArgumentException(int code, String msg) {
        super(code, msg);
    }

}
