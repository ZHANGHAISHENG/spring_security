package com.hamlt.security.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

/**
 * ClientCredentialsTokenEndpointFilter
 *  DelegatingPasswordEncoder
 *  https://blog.csdn.net/canon_in_d_major/article/details/79675033
 *
 *  自动续签：
 *  https://juejin.im/post/5c78fbed6fb9a049b2229454
 *
 *  jdbc client:
 *  https://blog.csdn.net/tealala/article/details/104729586
 */
@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private TokenStore redisTokenStore;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtAccessTokenConverter jwtAccessTokenConverter;

    @Autowired
    private TokenEnhancer jwtTokenEnhancer;


    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        //使用Redis作为Token的存储
        endpoints
                .tokenStore(redisTokenStore)
                .userDetailsService(userDetailsService)
                //启用oauth2管理
                .authenticationManager(authenticationManager) // 允许直接使用内部的TokenEndpoint 接口获取token
                //接收GET和POST
                .allowedTokenEndpointRequestMethods(HttpMethod.GET, HttpMethod.POST);

            //1、设置token为jwt形式
            //2、设置jwt 拓展认证信息
            // TokenEnhancerChain enhancerChain = new TokenEnhancerChain();
            // List<TokenEnhancer> enhancers = new ArrayList<TokenEnhancer>();
            // enhancers.add(jwtTokenEnhancer);
            // enhancers.add(jwtAccessTokenConverter);
            // enhancerChain.setTokenEnhancers(enhancers);
            // endpoints.tokenEnhancer(enhancerChain).accessTokenConverter(jwtAccessTokenConverter);
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
       // 采用委托的加密相当于： String finalSecret = "{bcrypt}" + new BCryptPasswordEncoder().encode("123456");
        clients.inMemory()//配置内存中，也可以是数据库
                .withClient("c1")//clientid
                //.secret("s1")
                .secret(passwordEncoder.encode("123456"))
                .accessTokenValiditySeconds(3600)//token有效时间  秒
                .refreshTokenValiditySeconds(3600 * 24)
                .authorizedGrantTypes("refresh_token", "password", "authorization_code")//token模式
                .scopes("all")//限制允许的权限配置

                .and()//下面配置第二个应用   （不知道动态的是怎么配置的，那就不能使用内存模式，应该使用数据库模式来吧）
                .withClient("test")
                .scopes("testSc")
                .accessTokenValiditySeconds(7200)
                .scopes("all");
    }

    @Override
    public void configure(AuthorizationServerSecurityConfigurer oauthServer) {
        // 允许直接使用内部的TokenEndpoint 接口获取token
        oauthServer.allowFormAuthenticationForClients();
    }



}
