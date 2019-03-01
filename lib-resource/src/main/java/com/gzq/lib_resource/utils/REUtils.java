package com.gzq.lib_resource.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class REUtils {
    /**
     * 判断是否为真实手机号
     *
     * @param mobiles
     * @return
     */
    public static boolean isMobile(String mobiles) {
        Pattern p = Pattern.compile("^(13[0-9]|15[012356789]|17[03678]|18[0-9]|14[57])[0-9]{8}$");
        Matcher m = p.matcher(mobiles);
        return m.matches();
    }

    /**
     * 15位和18位身份证号码的正则表达式 身份证验证
     *
     * @param idCard
     * @return
     */
    public static boolean validateIdCard(String idCard) {
        // 15位和18位身份证号码的正则表达式
        String regIdCard = "^(^[1-9]\\d{7}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}$)|(^[1-9]\\d{5}[1-9]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])((\\d{4})|\\d{3}[Xx])$)$";
        Pattern p = Pattern.compile(regIdCard);
        return p.matcher(idCard).matches();
    }
}
