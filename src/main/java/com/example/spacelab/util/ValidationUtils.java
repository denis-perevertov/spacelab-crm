package com.example.spacelab.util;

import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public interface ValidationUtils {

    List<String> ALLOWED_IMAGE_FORMATS = List.of("jpg", "jpeg", "png");
    Long MAX_FILE_SIZE = 20_971_520L;
    Long MAX_IMAGE_SIZE = 5_242_880L;
    Integer MAX_LESSON_INTERVAL = 30;
    Integer MIN_LESSON_INTERVAL = 1;
    Integer MAX_HOURS_NORM = 65;
    Integer MIN_HOURS_NORM = 10;
    Integer MAX_GROUP_SIZE = 30;
    Integer MIN_GROUP_SIZE = 3;

    static Map<String, String> getErrorMessages(BindingResult bindingResult) {
        return bindingResult.getFieldErrors()
                .stream()
                .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage, (error1, error2) -> error1));
    }

    static boolean fieldIsEmpty(String filter) {
        return filter == null || filter.isEmpty() || filter.equalsIgnoreCase("-1");
    }
    static boolean fieldIsEmpty(Integer filter) {
        return filter == null || filter <= 0;
    }
    static boolean fieldIsEmpty(Long filter) {
        return filter == null || filter <= 0L;
    }

    static boolean fieldIsNotEmpty(String filter) {
        return filter != null && !filter.isEmpty() && !filter.equalsIgnoreCase("-1");
    }

    static boolean fieldLengthIsIncorrect(String field, int minLength, int maxLength) {
        return field.length() < minLength || field.length() > maxLength;
    }

    static boolean fieldMatchesPattern(String field, String pattern) {
        return field.matches(pattern);
    }

    static boolean fieldMinLengthIsIncorrect(String field, int minLength) {
        return field.length() < minLength;
    }

    static boolean fieldMaxLengthIsIncorrect(String field, int maxLength) {
        return field.length() > maxLength;
    }

    static boolean fieldIntValueIsIncorrect(int field, int min, int max) {
        return field < min || field > max;
    }

    static boolean fieldDoubleValueIsIncorrect(double field, double min, double max) {
        return field < min || field > max;
    }

    static boolean fieldIntMinValueIsIncorrect(int field, int min) {
        return field < min;
    }

    static boolean fieldIntMaxValueIsIncorrect(int field, int max) {
        return field > max;
    }

    static boolean fieldDoubleMinValueIsIncorrect(double field, double min) {
        return field < min;
    }

    static boolean fieldDoubleMaxValueIsIncorrect(double field, double max) {
        return field > max;
    }



}