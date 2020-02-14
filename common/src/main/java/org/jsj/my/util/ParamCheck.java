package org.jsj.my.util;

import org.jsj.my.exception.BadArgumentException;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Objects;

/**
 * 输入参数检查
 *
 * @author The flow developers
 */
public final class ParamCheck {

    public static String checkStringNotNull(String param) throws BadArgumentException {
        param = StringUtils.trimToNull(param);
        if (null == param) {
            throw new BadArgumentException();
        }
        return param;
    }

    public static <T> T[] checkArrayNotEmpty(T[] param) throws BadArgumentException {
        if (null == param || 0 == param.length) {
            throw new BadArgumentException();
        }
        return param;
    }

    public static <T> T checkObjectNotNull(T param) throws BadArgumentException{
        if(null == param){
            throw new BadArgumentException();
        }
        return param;
    }

    public static List checkListNotNull(List param) throws BadArgumentException {
        if(Objects.isNull(param) || param.isEmpty()){
            throw new BadArgumentException();
        }
        return param;
    }


    public static long longString(String param) throws BadArgumentException {
        try {
            return Long.parseLong(StringUtils.trimToNull(param));
        } catch (Exception e) {
            throw new BadArgumentException();
        }
    }

    public static int intString(String param) throws BadArgumentException {
        try {
            return Integer.parseInt(StringUtils.trimToNull(param));
        } catch (Exception e) {
            throw new BadArgumentException();
        }
    }

    public static int checkPageNo(Integer pageNo) {
        if (null == pageNo || pageNo < 1) {
            return 1;
        }
        return pageNo;
    }

    public static int checkPageSize(Integer pageSize) {
        if (null == pageSize || pageSize < 0) {
            return 20;
        }
        return pageSize;
    }

    public static <T> T nullToDefault(T param, T val) {
        return (null == param) ? val : param;
    }

    public static String nullStringToDefault(String param, String val) {
        param = StringUtils.trimToNull(param);
        return (null == param) ? val : param;
    }
}
