package com.hamlt.springsecurity.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.HttpServletRequest;

@RestController
public class HelloController {

    @RequestMapping("/test/1")
    public String test1(@RequestParam(value = "name", defaultValue = "test1") String name, HttpServletRequest request) {
        return String.format("Hello %s! userId %s, userName %s", name, request.getHeader("userId"), request.getHeader("userName"));
    }

    @RequestMapping("/test/2")
    public String test2(@RequestParam(value = "name", defaultValue = "test2") String name, HttpServletRequest request) {
        return String.format("Hello %s! userId %s, userName %s", name, request.getHeader("userId"), request.getHeader("userName"));
    }

}
