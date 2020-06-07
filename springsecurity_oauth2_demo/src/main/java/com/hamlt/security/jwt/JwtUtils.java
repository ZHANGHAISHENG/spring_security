package com.hamlt.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.util.StringUtils;
import java.io.UnsupportedEncodingException;

/**
 * @author Administrator
 * @date 2020-06-06 19:40
 **/
public class JwtUtils {

    public static Claims parseToken(String token, String signingkey) throws UnsupportedEncodingException {
        //设置默认值
        if(StringUtils.isEmpty(signingkey)){
            signingkey = "zhsSignKey";
        }
        return Jwts.parser()
                // 不加getBytes会报错
                .setSigningKey(signingkey.getBytes("UTF-8"))
                .parseClaimsJws(token)
                .getBody();
    }

}
