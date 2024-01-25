package com.example.spacelab.util;

public interface StringUtils {

    static boolean fieldIsEmpty(String filter) {
        return filter == null || filter.isEmpty() || filter.equalsIgnoreCase("-1");
    }

}
