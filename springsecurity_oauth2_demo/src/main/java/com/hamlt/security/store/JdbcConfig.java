package com.hamlt.security.store;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;

import javax.sql.DataSource;

/**
 * @author Administrator
 * @date 2020-07-13 0:14
 **/
@Configuration
public class JdbcConfig {

    @Autowired
    private DataSource dataSource;

    @Bean
    @Primary // 配置上，否则报错
    public ClientDetailsService clientDetails() {
        return new JdbcClientDetailsService(dataSource);
    }

}
