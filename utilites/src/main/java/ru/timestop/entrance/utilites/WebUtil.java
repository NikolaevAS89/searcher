package ru.timestop.entrance.utilites;

import sun.misc.BASE64Encoder;

public class WebUtil {
    private WebUtil() {

    }

    public static String getAuth(String user, String password) {
        BASE64Encoder encoder = new BASE64Encoder();
        String userpassword = user + ":" + password;
        return encoder.encode(userpassword.getBytes());
    }
}
