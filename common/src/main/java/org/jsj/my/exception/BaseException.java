package org.jsj.my.exception;

import lombok.Getter;

/**
 * 所有异常的父类
 *
 * @author JSJ
 */
public class BaseException extends Exception {
    /**
     * 前端通过该错误码来展示对应的错误信息，包括国际化的处理
     */
    @Getter
    private int code;

    /**
     * 可选的错误信息，当前端找不到对应的文案时展示
     */
    @Getter
    private String msg;

    public BaseException(int code, String msg) {
        super("{code:" + code + ", msg:" + msg + "}");
        this.code = code;
        this.msg = msg;
    }
}
