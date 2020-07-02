package com.hamlt.springsecurity.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

    //private static final String RESOURCE_IDS = "order";

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) {
       // resources.resourceId(RESOURCE_IDS).stateless(true);
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        /*httpSecurity
                .authorizeRequests()
                .antMatchers("/test/**")
                .authenticated()
                .antMatchers(
                 "/oauth/**",
                "/login"
                )
                .permitAll();*/
        http.
                requestMatchers()
                // 必须登录过的用户才可以进行 oauth2 的授权码申请
                .antMatchers("/", "/home", "/login", "/oauth/authorize")
                .and()
                .authorizeRequests()
                .anyRequest().permitAll()
                .and()
                .formLogin()
                .loginPage("/login")
                .and()
                .httpBasic()
                .disable()
                .exceptionHandling()
                .accessDeniedPage("/login?authorization_error=true")
                .and()
                .csrf()
                .requireCsrfProtectionMatcher(new AntPathRequestMatcher("/oauth/authorize"))
                .disable();
    }
}