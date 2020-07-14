package com.hamlt.security.config;

public class WhiteConfig {

    public static String[] whiteUrls = new String[]{
            "/error",
            "/login",
            "/form/token",
            "/mobile/token",
            "/email/token",
            "/loginOutTest/**",
            "/oauth/**",
            "/test3",
            "/token/refresh",
            "/register",
            "/social/**",
            "/**/*.js",
            "/**/*.css",
            "/**/*.jpg",
            "/**/*.png",
            "/**/*.woff2",
            "/code/image"
    };


}
