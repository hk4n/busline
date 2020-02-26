package com.sbab.home.assignment.bus.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import springfox.documentation.annotations.ApiIgnore;

@ApiIgnore
@Controller
@RequestMapping("/")
public class SwaggerController {

    @GetMapping
     public String redirectSwagger() {
            return "redirect:/swagger-ui.html";
     }
}
