package com.example.spacelab.util;

public interface StringUtils {

    static boolean fieldIsEmpty(String filter) {
        return filter == null || filter.isEmpty() || filter.equalsIgnoreCase("-1");
    }

    static String trimString(String str) {
        return (str == null || str.isEmpty()) ? str : str.trim();
    }

}
