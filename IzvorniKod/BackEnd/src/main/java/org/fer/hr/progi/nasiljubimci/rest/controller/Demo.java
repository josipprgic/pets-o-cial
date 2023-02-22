package org.fer.hr.progi.nasiljubimci.rest.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Demo {

    @GetMapping("/demo")
    public String demoUser() {
        return "Welcome User";
    }

    @GetMapping("/demoAdmin")
    public String demoAdmin() {
        return "Welcome Admin";
    }

    @GetMapping("/demoSuperAdmin")
    public String demoSuperAdmin() {
        return "Welcome Super Admin";
    }
}
