package org.origami.ws.security;


import org.apache.commons.codec.digest.DigestUtils;

public class MD5PasswordEncoder   {


    public String encode(CharSequence charSequence) {
        String encPass = "";
        try {
            encPass = DigestUtils.md5Hex(charSequence.toString());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return encPass;
    }

    public boolean matches(CharSequence charSequence, String s) {
        return encode(charSequence).equals(s);
    }
}