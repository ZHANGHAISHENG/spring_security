package com.hamlt.security.authentication.mobile;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

//短信验证码拦截器
public class SmsCodeAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    private String mobileParameter = "mobile";
    private String smsCodeParameter = "smsCode";

    SmsCodeAuthenticationFilter() {
        super(new AntPathRequestMatcher("/mobile/token", "POST"));
    }

    public Authentication attemptAuthentication(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse)
            throws AuthenticationException, IOException {
        String mobile = httpServletRequest.getParameter(mobileParameter);
        String smsCode =  httpServletRequest.getParameter(smsCodeParameter);
        SmsCodeAuthenticationToken authRequest = new SmsCodeAuthenticationToken(mobile, smsCode);
        setDetails(httpServletRequest, authRequest); // Allow subclasses to set the "details" property
        return this.getAuthenticationManager().authenticate(authRequest);
    }

    private void setDetails(HttpServletRequest request, SmsCodeAuthenticationToken authRequest) {
        authRequest.setDetails(authenticationDetailsSource.buildDetails(request));
    }

}
