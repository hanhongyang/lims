package com.gmlimsqi.common.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 脱敏工具类
 *
 * @author ruoyi
 */
public class DesensitizedUtil
{
    /**
     * 密码的全部字符都用*代替，比如：******
     *
     * @param password 密码
     * @return 脱敏后的密码
     */
    public static String password(String password)
    {
        if (StringUtils.isBlank(password))
        {
            return StringUtils.EMPTY;
        }
        return StringUtils.repeat('*', password.length());
    }

    /**
     * 车牌中间用*代替，如果是错误的车牌，不处理
     *
     * @param carLicense 完整的车牌号
     * @return 脱敏后的车牌
     */
    public static String carLicense(String carLicense)
    {
        if (StringUtils.isBlank(carLicense))
        {
            return StringUtils.EMPTY;
        }
        // 普通车牌
        if (carLicense.length() == 7)
        {
            carLicense = StringUtils.hide(carLicense, 3, 6);
        }
        else if (carLicense.length() == 8)
        {
            // 新能源车牌
            carLicense = StringUtils.hide(carLicense, 3, 7);
        }
        return carLicense;
    }
    /**
     * 身份证号脱敏
     *
     * @param idCard
     * @return
     */
    public static String idCardDesensitization(String idCard) {
        if (StringUtils.isNotEmpty(idCard)) {
            // 身份证号脱敏规则一：保留前六后三
            if (idCard.length() == 15) {
                idCard = idCard.replaceAll("(\\w{6})\\w*(\\w{3})", "$1******$2");
            } else if (idCard.length() == 18) {
                idCard = idCard.replaceAll("(\\w{6})\\w*(\\w{3})", "$1*********$2");
            }
            // 身份证号脱敏规则二：保留前三后四
            // idCard = idCard.replaceAll("(?<=\\w{3})\\w(?=\\w{4})", "*");
        }
        return idCard;
    }

    /**
     * 手机号码脱敏
     *
     * @param mobilePhone
     * @return
     */
    public static String mobilePhoneDesensitization(String mobilePhone) {
        // 手机号码保留前三后四
        if (StringUtils.isNotEmpty(mobilePhone)) {
            mobilePhone = mobilePhone.replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2");
        }
        return mobilePhone;
    }

    /**
     * 电子邮箱脱敏
     *
     * @param email
     * @return
     */
    public static String emailDesensitization(String email) {
        // 电子邮箱隐藏@前面的3个字符
        if (StringUtils.isEmpty(email)) {
            return email;
        }
        String encrypt = email.replaceAll("(\\w+)\\w{3}@(\\w+)", "$1***@$2");
        if (email.equalsIgnoreCase(encrypt)) {
            encrypt = email.replaceAll("(\\w*)\\w{1}@(\\w+)", "$1*@$2");
        }
        return encrypt;
    }

    /**
     * 银行账号脱敏
     *
     * @param acctNo
     * @return
     */
    public static String acctNoDesensitization(String acctNo) {
        // 银行账号保留前六后四
        if (StringUtils.isNotEmpty(acctNo)) {
            String regex = "(\\w{6})(.*)(\\w{4})";
            Matcher m = Pattern.compile(regex).matcher(acctNo);
            if (m.find()) {
                String rep = m.group(2);
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < rep.length(); i++) {
                    sb.append("*");
                }
                acctNo = acctNo.replaceAll(rep, sb.toString());
            }
        }
        return acctNo;
    }
    /**
     * 家庭地址脱敏
     *
     * @param address
     * @return
     */
    public static String addressDesensitization(String address) {
        // 规则说明：从第4位开始隐藏，隐藏8位。
        if (StringUtils.isNotEmpty(address)) {
            char[] chars = address.toCharArray();
            if (chars.length > 11) {// 由于需要从第4位开始，隐藏8位，因此数据长度必须大于11位
                // 获取第一部分内容
                String str1 = address.substring(0, 4);
                // 获取第二部分
                String str2 = "********";
                // 获取第三部分
                String str3 = address.substring(12);
                StringBuffer sb = new StringBuffer();
                sb.append(str1);
                sb.append(str2);
                sb.append(str3);
                address = sb.toString();
            }
        }
        return address;
    }
}
