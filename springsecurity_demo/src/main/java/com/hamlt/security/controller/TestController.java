package com.hamlt.security.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;

@RestController
public class TestController {

    @Value("${security.jwt.signingKey}")
    private String signingkey;

    /**
     * 获取用户信息
     *
     * @param userDetails
     * @return
     */
    @RequestMapping("/test1")
    public Object test1(@AuthenticationPrincipal UserDetails userDetails) {
        return userDetails;
    }

    @RequestMapping("/test2")
    public Object test2(Authentication user, HttpServletRequest request) throws UnsupportedEncodingException {
        return user;
    }

    @RequestMapping("/test3")
    public Object test3(HttpServletRequest request) throws UnsupportedEncodingException {
        return "test3";
    }


    @RequestMapping("/loginOutTest")
    public String loginOut(HttpServletRequest request) {
      String token =  request.getHeader("token");
      // @TODO
      return true ? "退出成功" : "退出失败";
    }

}
