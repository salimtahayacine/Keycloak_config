package com.stronglover.demo.test.keycloak.controler;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/demo")
public class demoController {

    @GetMapping("/hello")
    @PreAuthorize("hasRole('simple_User')")
    public String hello(){

        return "hello from springboot & Keycloak";
    }

    @GetMapping()
    @PreAuthorize("hasRole('admin')")
    public String hello_2(){
        return "hello 2  from springboot & Keycloak - ADMIN";
    }
}
