package com.hamlt.security.handler;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class MyLogoutSuccessHandler implements LogoutSuccessHandler {


    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        String token = request.getParameter("token");
        boolean b = true;
        if(token != null && !token.trim().isEmpty()){
             // @TODO
        }
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(b ? "退出成功" : "退出失败");
    }
}

