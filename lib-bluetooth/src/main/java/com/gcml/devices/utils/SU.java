package com.gcml.devices.utils;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

public class SU {
    /**
     * byteArr转inputStream
     *
     * @param bytes 字节数组
     * @return 输入流
     */
    public static InputStream bytes2InputStream(byte[] bytes) {
        return new ByteArrayInputStream(bytes);
    }
}
