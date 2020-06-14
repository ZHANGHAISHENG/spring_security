package com.hamlt.security.encoder;

import org.springframework.util.DigestUtils;

/**
 * @author Administrator
 * @date 2020-06-13 23:52
 **/
public class MD5Util {

    //盐，用于混交md5
    private static final String slat = "&%5123***&&%%$$#@";
    /**
     * 生成md5
     */
    public static String getMD5(String str) {
        String base = str +"/"+slat;
        return DigestUtils.md5DigestAsHex(base.getBytes());
    }

}
