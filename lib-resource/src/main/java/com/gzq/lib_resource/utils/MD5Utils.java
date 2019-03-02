package com.gzq.lib_resource.utils;

import java.security.MessageDigest;

public class MD5Utils {
    public static String md5(String text) {
        if (text == null || text.trim().length() == 0) {
            throw new IllegalArgumentException("text == null or empty");
        }
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            return bytesToHexString(md5.digest(text.getBytes("utf-8")));
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public static String bytesToHexString(byte[] data) {
        if (data == null) {
            return "";
        }
        StringBuilder builder = new StringBuilder();
        int length = data.length;
        for (int i = 0; i < length; i++) {
            String hex = Integer.toHexString(0xFF & data[i]);
            if (hex.length() == 1) {
                builder.append('0');
            }
            builder.append(hex);
        }
        return builder.toString();
    }
}
