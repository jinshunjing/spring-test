package org.jsj.my.util;

import java.util.UUID;

/**
 * Utils
 *
 * @author JSJ
 */
public class Utils {
    public static final String getParentPackage(Package p) {
        String name = p.getName();
        return getParentPackage(name);
    }

    public static final String getParentPackage(String name) {
        int index = name.lastIndexOf('.');
        if (index > 0) {
            return name.substring(0, index);
        }
        return name;
    }

    public static String digits(long val, int digits) {
        long hi = 1L << (digits * 4);
        return Long.toHexString(hi | (val & (hi - 1))).substring(1);
    }

    public static String getRandomUUID() {
        UUID uuid = UUID.randomUUID();
        long mostSigBits = uuid.getMostSignificantBits();
        long leastSigBits = uuid.getLeastSignificantBits();
        StringBuilder sb = new StringBuilder();
        sb.append(digits(mostSigBits >> 32, 8));
        sb.append(digits(mostSigBits >> 16, 4));
        sb.append(digits(mostSigBits, 4));
        sb.append(digits(leastSigBits >> 48, 4));
        sb.append(digits(leastSigBits, 12));
        assert (sb.length() == 32);
        return sb.toString();
    }

    public static int length(String value) {
        int valueLength = 0;
        String chinese = "[\u0391-\uFFE5]";
        /* 获取字段值的长度，如果含中文字符，则每个中文字符长度为2，否则为1 */
        for (int i = 0; i < value.length(); i++) {
            /* 获取一个字符 */
            String temp = value.substring(i, i + 1);
            /* 判断是否为中文字符 */
            if (temp.matches(chinese)) {
                /* 中文字符长度为2 */
                valueLength += 2;
            } else {
                /* 其他字符长度为1 */
                valueLength += 1;
            }
        }
        return valueLength;
    }
}
