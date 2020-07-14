package com.hamlt.security.config;

import com.hamlt.security.authentication.access.MyAccessDeniedHandler;
import com.hamlt.security.authentication.access.MyAuthExceptionEntryPoint;
import com.hamlt.security.authentication.access.MyAuthenticationFilter;
import com.hamlt.security.authentication.mobile.SmsCodeAuthenticationSecurityConfig;
import com.hamlt.security.authentication.permit.MyAccessDecisionManager;
import com.hamlt.security.authentication.permit.MySecurityMetadataSource;
import com.hamlt.security.encoder.Md5PasswordEncoder;
import com.hamlt.security.service.ApiUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    protected AuthenticationSuccessHandler authenticationSuccessHandler;

    @Autowired
    protected AuthenticationFailureHandler authenticationFailureHandler;

    @Autowired
    private SmsCodeAuthenticationSecurityConfig smsCodeAuthenticationSecurityConfig;

    @Autowired
    private MyAuthenticationFilter myAuthenticationFilter;

    @Autowired
    private LogoutSuccessHandler logoutSuccessHandler;

    @Autowired
    private MyAccessDecisionManager accessDecisionManager;

    @Autowired
    private MySecurityMetadataSource securityMetadataSource;

    @Autowired
    private ApiUserDetailsService userDetailsService;

    @Bean
    PasswordEncoder passwordEncoder() {
        // return PasswordEncoderFactories.createDelegatingPasswordEncoder();
         return new Md5PasswordEncoder();
    }


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService);
    }

    /**
     * 通过HttpSecurity实现Security的自定义过滤配置
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // 配置token认证filter
        http.addFilterBefore(myAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        http.formLogin()
                //登录页面，app用不到
                //.loginPage("/authentication/login")
                //登录提交action，app会用到
                // 用户名登录地址
                .loginProcessingUrl("/form/token")
                //.passwordParameter("password")
                //.usernameParameter("username")
                .successHandler(authenticationSuccessHandler)
                .failureHandler(authenticationFailureHandler)
                .and()
                .logout()
                //.logoutUrl("/logout")
                .logoutSuccessHandler(logoutSuccessHandler);

        http.apply(smsCodeAuthenticationSecurityConfig)  // 手机验证码登录
                .and()
                .authorizeRequests()
                // 等价于拦截器实现权限校验
                .withObjectPostProcessor(new ObjectPostProcessor<FilterSecurityInterceptor>() {
                    @Override
                    public <O extends FilterSecurityInterceptor> O postProcess(O o) {
                        o.setSecurityMetadataSource(securityMetadataSource);
                        o.setAccessDecisionManager(accessDecisionManager);
                        return o;
                    }
                })
                .antMatchers(WhiteConfig.whiteUrls)
                .permitAll()//以上的请求都不需要认证
                .anyRequest()
                .authenticated()
                .and()
                .csrf().disable()
                .httpBasic();
            // 自定义异常处理
            http.exceptionHandling()
                .accessDeniedHandler(new MyAccessDeniedHandler())
                .authenticationEntryPoint(new MyAuthExceptionEntryPoint());

    }
}