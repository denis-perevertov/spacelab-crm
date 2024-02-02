package com.example.spacelab.util;

import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
@RequiredArgsConstructor
public class TranslationService {

    public final static Locale UA = new Locale("uk", "ua");
    public final static Locale RU = new Locale("ru", "ua");
    public final static Locale EN = new Locale("en", "ua");

    private final MessageSource messageSource;

    public String getMessage(String code, Locale locale) {
        return messageSource.getMessage(code, null, null, locale);
    }

    public static Locale getLocale(String code) {
        return switch(code) {
            case "en" -> EN;
            case "ru" -> RU;
            default -> UA;
        };
    }
}
