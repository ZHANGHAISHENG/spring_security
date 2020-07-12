package com.hamlt.security.authentication.access;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.provider.authentication.TokenExtractor;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Administrator
 * @date 2020-06-14 0:55
 **/
public class MyTokenExtractor implements TokenExtractor {
    @Override
    public Authentication extract(HttpServletRequest request) {
        String tokenValue = request.getHeader("Authorization");
        if (tokenValue != null) {
            PreAuthenticatedAuthenticationToken authentication = new PreAuthenticatedAuthenticationToken(tokenValue, "");
            return authentication;
        }
        return null;
    }
}
