package org.jsj.my.util;

import com.google.common.collect.Lists;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * Page
 *
 * @author JSJ
 */
@Data
public class Page<T> implements Serializable {
    private static final long serialVersionUID = -1337169533626282458L;
    private List<T> content;

    private int total;

    public static <T> Page<T> of(List<T> content, int totalElements) {
        Page<T> p = new Page();
        p.setTotal(totalElements);
        p.setContent(content);
        return p;
    }

    public static <T> Page<T> empty() {
        return Page.of(Lists.newArrayList(), 0);
    }
}
