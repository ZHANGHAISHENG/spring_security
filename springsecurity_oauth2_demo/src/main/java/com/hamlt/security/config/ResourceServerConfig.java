package com.hamlt.security.config;

import com.hamlt.security.authentication.access.JwtAuthenticationFilter;
import com.hamlt.security.authentication.mobile.SmsCodeAuthenticationSecurityConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

    @Autowired
    protected AuthenticationSuccessHandler authenticationSuccessHandler;

    @Autowired
    protected AuthenticationFailureHandler authenticationFailureHandler;

    @Autowired
    private SmsCodeAuthenticationSecurityConfig smsCodeAuthenticationSecurityConfig;

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;



    @Override
    public void configure(ResourceServerSecurityConfigurer resources) {
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {

        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        //todo:注意：spring-security formlogin其实就是一个登录页加上一个提交action组成的，
        // 所以在我们的app登录的时候我们只要提交的action，不要跳转到登录页
        http.formLogin()
                //登录页面，app用不到
                //.loginPage("/authentication/login")
                //登录提交action，app会用到
                // 用户名登录地址
                .loginProcessingUrl("/form/token")
                .successHandler(authenticationSuccessHandler)
                .failureHandler(authenticationFailureHandler);

        http//.apply(validateCodeSecurityConfig)
                //	.and()
                // 手机验证码登录
                .apply(smsCodeAuthenticationSecurityConfig)
                .and()
               // .apply(zhsSignKeySocialSecurityConfig)
               // .and()
               // .apply(socialAuthenticationSecurityConfig)
               // .and()
                .authorizeRequests()
                //手机验证码登录地址
                .antMatchers("/mobile/token", "/email/token")
                .permitAll()
                .and()
                .authorizeRequests()
                .antMatchers(
                        "/loginOut/**",
                        "/oauth/**",
                        "/token/refresh",
                        "/register",
                        "/social/**",
                        "/**/*.js",
                        "/**/*.css",
                        "/**/*.jpg",
                        "/**/*.png",
                        "/**/*.woff2",
                        "/code/image")
                .permitAll()//以上的请求都不需要认证
                .anyRequest()
                .authenticated()
                .and()
                .csrf().disable();
    }
}
