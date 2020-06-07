package com.hamlt.security.jwt;

import org.springframework.stereotype.Service;

import java.util.HashMap;

/**
 * 拓展jwt token里面的信息
 */
@Service
public class ApiJwtTokenEnhancerHandler implements JwtTokenEnhancerHandler {

    public HashMap<String, Object> getInfoToToken() {
        HashMap<String, Object> info = new HashMap<String, Object>();
        info.put("author", "张威");
        info.put("company", "zhsSignKey-copy");
        return info;
    }
}
