package com.example.spacelab.util;

public interface NumericUtils {

    static boolean fieldIsEmpty(Integer filter) {
        return filter == null || filter <= 0;
    }
    static boolean fieldIsEmpty(Long filter) {
        return filter == null || filter <= 0L;
    }

}
