package com.example.spacelab.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cglib.core.Local;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;
import org.springframework.web.servlet.view.RedirectView;

import java.util.Locale;

@Slf4j
@Controller
@RequiredArgsConstructor
public class ApiController {

    @GetMapping
    public String swaggerRedirect() {
        return "redirect:/swagger-ui/index.html";
    }

    // fixme
    @PutMapping("/api/locale")
    public ResponseEntity<?> changeLocale(@RequestParam String lang) {
        log.info(lang);
        return ResponseEntity.ok().build();
    }
}
