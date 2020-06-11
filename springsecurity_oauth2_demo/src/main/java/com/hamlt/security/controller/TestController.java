package com.hamlt.security.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.TokenRequest;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.oauth2.provider.token.ConsumerTokenServices;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;

@RestController
public class TestController {

    @Value("${security.jwt.signingKey}")
    private String signingkey;

    @Autowired
    private ClientDetailsService clientDetailsService;

    @Autowired
    private AuthorizationServerTokenServices authorizationServerTokenServices;

    @Autowired
    ConsumerTokenServices consumerTokenServices;

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

    @RequestMapping("/token/refresh")
    public OAuth2AccessToken refreshToken(String refreshToken, HttpServletRequest request) {
        String clientId = request.getHeader("clientId");
        ClientDetails clientDetails = clientDetailsService.loadClientByClientId(clientId);
        TokenRequest tokenRequest = new TokenRequest(new HashMap<>(), clientId, clientDetails.getScope(), "custom");
        return authorizationServerTokenServices.refreshAccessToken(refreshToken, tokenRequest);
    }

    @RequestMapping("/loginOutTest")
    public String loginOut(HttpServletRequest request) {
      String token =  request.getHeader("token");
      boolean b = consumerTokenServices.revokeToken(token);
      return b ? "退出成功" : "退出失败";
    }

}
