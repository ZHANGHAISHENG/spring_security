package com.hamlt.security.store;

import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;
import java.util.Date;

/**
 * 实现自动续签
 */
public class MyRedisTokenStore extends RedisTokenStore {
    private ClientDetailsService clientDetailsService;

    public MyRedisTokenStore(RedisConnectionFactory connectionFactory, ClientDetailsService clientDetailsService) {
        super(connectionFactory);
        this.clientDetailsService = clientDetailsService;
    }

    @Override
    public OAuth2Authentication readAuthentication(OAuth2AccessToken token) {
        OAuth2Authentication result = readAuthentication(token.getValue());
        if (result != null) {
            // 如果token没有失效  更新AccessToken过期时间
            DefaultOAuth2AccessToken oAuth2AccessToken = (DefaultOAuth2AccessToken) token;

            //重新设置过期时间
            int validitySeconds = getAccessTokenValiditySeconds(result.getOAuth2Request());
            int expiresIn = oAuth2AccessToken.getExpiresIn();
            if(expiresIn < validitySeconds / 2) {
                oAuth2AccessToken.setExpiration(new Date(System.currentTimeMillis() + (validitySeconds * 1000L)));
                //将重新设置过的过期时间重新存入redis, 此时会覆盖redis中原本的过期时间
                storeAccessToken(token, result);
            }
        }
        return result;
    }

    private int getAccessTokenValiditySeconds(OAuth2Request clientAuth) {
        ClientDetails client = clientDetailsService.loadClientByClientId(clientAuth.getClientId());
        return client.getAccessTokenValiditySeconds();
    }

}
