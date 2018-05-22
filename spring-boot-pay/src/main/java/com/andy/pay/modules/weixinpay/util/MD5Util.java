package com.andy.pay.modules.weixinpay.util;

import lombok.extern.slf4j.Slf4j;

import java.security.MessageDigest;

/**
 * @Description:
 * @Author: Mr.lyon
 * @CreateBy: 2018-05-20 21:04
 **/
@Slf4j
public class MD5Util {

    private static final String hexDigits[] = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f" };

    private static String byteArrayToHexString(byte b[]) {
        StringBuffer resultSb = new StringBuffer();
        for (int i = 0; i < b.length; i++) {
            resultSb.append(byteToHexString(b[i]));
        }
        return resultSb.toString();
    }

    private static String byteToHexString(byte b) {
        int n = b;
        if (n < 0)
            n += 256;
        int d1 = n / 16;
        int d2 = n % 16;
        return hexDigits[d1] + hexDigits[d2];
    }

    public static String MD5Encode(String origin, String charset) {
        String result = null;
        try {
            result = new String(origin);
            MessageDigest md = MessageDigest.getInstance("MD5");
            if (charset == null || "".equals(charset)) {
                result = byteArrayToHexString(md.digest(result.getBytes()));
            } else {
                result = byteArrayToHexString(md.digest(result.getBytes(charset)));
            }
        } catch (Exception exception) {
            log.info("生成MD5摘要失败！");
        }
        return result;
    }

}
