package org.jsj.my.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;

import java.util.Arrays;

/**
 * JSON utils
 *
 * @author Flow developers
 */
public class JsonUtil {
    public static String toJSONString(JSON json) {
        return JSON.toJSONString(json);
    }

    public static String objectToJSONString(Object obj) {
        return JSON.toJSONString(obj);
    }

    public static String convertToJsonArrayString(String s) {
        return convertToJsonArrayString(s, ",");
    }

    public static String convertToJsonArrayString(String s, String split) {
        if(s == null) {
            return null;
        }
        JSONArray array = new JSONArray();
        array.addAll(Arrays.asList(s.split(split)));
        return JsonUtil.toJSONString(array);
    }
}
