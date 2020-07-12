package com.hamlt.security.store;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;
import javax.sql.DataSource;

/**
 * @author Administrator
 * @date 2020-07-13 0:14
 **/
@Configuration
@ConditionalOnProperty(prefix = "security.oauth2", name = "storeType", havingValue = "jdbc")
public class JdbcTokenStoreConfig {

    @Autowired
    private DataSource dataSource;

    @Bean
    public TokenStore tokenStore(){
        // 基于 JDBC 实现，令牌保存到数据库
        return new JdbcTokenStore(dataSource);
    }

}
