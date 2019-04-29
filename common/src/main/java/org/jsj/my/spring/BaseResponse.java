package org.jsj.my.spring;

/**
 * 返回对象
 *
 * @author JSJ
 */
public class BaseResponse {
    public int code;
    public String errorMsg;
    public Object data;

    public static BaseResponse make(int code, String errorMsg) {
        BaseResponse ret = new BaseResponse();
        ret.code = code;
        ret.errorMsg = errorMsg;
        return ret;
    }

    public static BaseResponse make(int code) {
        return make(code, null);
    }

    public static BaseResponse make(Object resp) {
        BaseResponse ret = new BaseResponse();
        ret.code = 0;
        ret.data = resp;
        return ret;
    }
}
