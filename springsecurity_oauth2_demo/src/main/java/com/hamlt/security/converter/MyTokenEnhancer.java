package com.hamlt.security.converter;

import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.stereotype.Component;

import java.util.HashMap;

/**
 * 拓展jwt 认证信息
 */
@Component
public class MyTokenEnhancer implements TokenEnhancer {

    public OAuth2AccessToken enhance(OAuth2AccessToken oAuth2AccessToken,
                                     OAuth2Authentication oAuth2Authentication) {
        HashMap<String, Object> info = new HashMap<>();
        info.put("author", "张威");
        info.put("company", "zhsSignKey-copy");
        //todo:判断info是否为空
        ((DefaultOAuth2AccessToken) oAuth2AccessToken).setAdditionalInformation(info);
        return oAuth2AccessToken;
    }
}
