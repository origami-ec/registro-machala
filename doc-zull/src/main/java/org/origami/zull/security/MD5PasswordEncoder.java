package org.origami.zull.security;


import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.security.crypto.password.PasswordEncoder;

public class MD5PasswordEncoder implements PasswordEncoder {

    @Override
    public String encode(CharSequence charSequence) {
        String encPass = "";
        try {
            encPass = DigestUtils.md5Hex(charSequence.toString());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return encPass;
    }

    @Override
    public boolean matches(CharSequence charSequence, String s) {
        return encode(charSequence).equals(s);
    }
}