package com.example.spacelab.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.view.RedirectView;

@Controller
public class ApiController {

    @GetMapping
    public String swaggerRedirect() {
        return "redirect:/swagger-ui/index.html";
    }
}
