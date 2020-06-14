package com.hamlt.security.encoder;

import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * @author Administrator
 * @date 2020-06-13 23:50
 **/
public class MyPasswordEncoder implements PasswordEncoder {
    @Override
    public String encode(CharSequence rawPassword) {
        return MD5Util.getMD5(rawPassword.toString());
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        return MD5Util.getMD5(rawPassword.toString()).equals(encodedPassword);
    }
}
