package org.jsj.my.constant;

/**
 * 错误码
 *
 * @author JSJ
 */
public interface ErrorCode {

    /**
     * 技术错误，不需要报告详情
     * 1101：系统内部异常
     */
    int SERVER_ERROR = 1101;
    int DATABASE_ERROR = 1101;
    int NETWORK_ERROR = 1101;
    int TIMEOUT_ERROR = 1102;

    /**
     * 参数错误
     * 1201：输入参数无效
     */
    int BAD_ARGUMENT_ERROR = 1201;

}
