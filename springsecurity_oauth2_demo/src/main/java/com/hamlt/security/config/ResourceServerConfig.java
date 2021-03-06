package com.hamlt.security.config;

import com.hamlt.security.authentication.access.MyAuthenticationFilter;
import com.hamlt.security.authentication.access.MyAccessDeniedHandler;
import com.hamlt.security.authentication.access.MyAuthExceptionEntryPoint;
import com.hamlt.security.authentication.access.MyDefaultWebResponseExceptionTranslator;
import com.hamlt.security.authentication.mobile.SmsCodeAuthenticationSecurityConfig;
import com.hamlt.security.authentication.permit.MyAccessDecisionManager;
import com.hamlt.security.authentication.permit.MyOauth2FilterSecurityInterceptor;
import com.hamlt.security.authentication.permit.MySecurityMetadataSource;
import com.hamlt.security.authentication.access.MyTokenExtractor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationProcessingFilter;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.server.authentication.WebFilterChainServerAuthenticationSuccessHandler;

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
    private MyAuthenticationFilter myAuthenticationFilter;

    @Autowired
    private LogoutSuccessHandler logoutSuccessHandler;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private MyAccessDecisionManager accessDecisionManager;

    @Autowired
    private MySecurityMetadataSource securityMetadataSource;

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) {

        /**配置token或权限异不足异常处理**/
        MyAuthExceptionEntryPoint myAuthExceptionEntryPoint = new MyAuthExceptionEntryPoint();
        //myAuthExceptionEntryPoint commence方法未使用到translator
        //在OAuth2AuthenticationEntryPoint commence方法使用到了translator
        //myAuthExceptionEntryPoint.setExceptionTranslator(new MyDefaultWebResponseExceptionTranslator());
        resources.authenticationEntryPoint(myAuthExceptionEntryPoint)
        .accessDeniedHandler(new MyAccessDeniedHandler());
        /**配置默认的token解析**/
        resources.tokenExtractor(new MyTokenExtractor());
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        // 不能直接注入
        // 权限过滤器优先度在 FilterSecurityInterceptor 之后，addFilterBefore也能通过
        /*MyOauth2FilterSecurityInterceptor securityInterceptor = new MyOauth2FilterSecurityInterceptor();
        securityInterceptor.setAuthenticationManager(authenticationManager);
        securityInterceptor.setSecurityMetadataSource(securityMetadataSource);
        securityInterceptor.setAccessDecisionManager(accessDecisionManager);
        http.addFilterAfter(securityInterceptor, FilterSecurityInterceptor.class);*/
        // 配置token认证filter
        http.addFilterBefore(myAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        //todo:注意：spring-security formlogin其实就是一个登录页加上一个提交action组成的，
        // 所以在我们的app登录的时候我们只要提交的action，不要跳转到登录页

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
                //.antMatchers("/test2/*").access("hasRole('ROLE_USER')") // ExpressionBasedFilterInvocationSecurityMetadataSource -> AffirmativeBased -> WebExpressionVoter ->  WebExpressionConfigAttribute
                .antMatchers(WhiteConfig.whiteUrls)
                .permitAll()//以上的请求都不需要认证
                .anyRequest()
                .authenticated()
                .and()
                .csrf().disable();

           /* // 自定义异常处理
            http.exceptionHandling()
                    .accessDeniedHandler(new MyAccessDeniedHandler())*/
                    //.authenticationEntryPoint(new MyAuthExceptionEntryPoint()); // token异常不能放这

    }

}
