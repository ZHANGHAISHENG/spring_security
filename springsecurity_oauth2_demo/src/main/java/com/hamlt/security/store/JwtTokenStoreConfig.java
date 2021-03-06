package com.hamlt.security.store;

import com.hamlt.security.converter.MyAccessTokenConverter;
import com.hamlt.security.converter.MyTokenEnhancer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.util.StringUtils;

@Configuration
@ConditionalOnProperty(prefix = "security.oauth2", name = "storeType", havingValue = "jwt")
public class JwtTokenStoreConfig {

    @Value("${security.jwt.signingKey}")
    private String signingkey;

    @Bean
    public TokenEnhancer jwtTokenEnhancer() {
        return new MyTokenEnhancer();
    }

    @Bean
    public TokenStore jetTokenStroe() {
        return new JwtTokenStore(jwtAccessTokenConverter());
    }

    @Bean
    public JwtAccessTokenConverter jwtAccessTokenConverter() {
        // Jwt 默认调用DefaultAccessTokenConverter
        JwtAccessTokenConverter jwtAccessTokenConverter = new JwtAccessTokenConverter();
        //设置默认值
        if(StringUtils.isEmpty(signingkey)){
            signingkey = "zhsSignKey";
        }
        //密钥，放到配置文件中
        jwtAccessTokenConverter.setSigningKey(signingkey);
        // MyAccessTokenConverter 用于删除默认的信息，防止生成的token太长
        jwtAccessTokenConverter.setAccessTokenConverter(new MyAccessTokenConverter());
        return jwtAccessTokenConverter;
    }
}
