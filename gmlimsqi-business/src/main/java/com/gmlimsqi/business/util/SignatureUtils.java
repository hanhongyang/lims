package com.gmlimsqi.business.util;

import com.gmlimsqi.common.utils.StringUtils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.nio.charset.StandardCharsets;

public class SignatureUtils {
    // 服务端分配的私钥（文档指定）
    private static final String PRIVATE_KEY = "PASTURELIMS";

    /**
     * 生成签名（按文档规则：JSON请求体→MD5大写→拼接私钥→MD5大写）
     */
    public static String generateSign(String jsonBody) {
        if (StringUtils.isBlank(jsonBody)) {
            throw new IllegalArgumentException("请求体JSON不能为空");
        }
        try {
            // 第一次MD5加密（JSON体）
            String firstMd5 = md5Encrypt(jsonBody).toUpperCase();
            // 第二次MD5加密（第一次结果+私钥）
            return md5Encrypt(firstMd5 + PRIVATE_KEY).toUpperCase();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("签名生成失败", e);
        }
    }

    /**
     * MD5加密核心方法
     */
    private static String md5Encrypt(String content) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] digest = md.digest(content.getBytes(StandardCharsets.UTF_8));
        StringBuilder hexBuilder = new StringBuilder();
        for (byte b : digest) {
            String hex = Integer.toHexString(0xFF & b);
            if (hex.length() == 1) hexBuilder.append('0');
            hexBuilder.append(hex);
        }
        return hexBuilder.toString();
    }

    public static void main(String[] args) {
        String jsonBody = "{\"orderNumber\":\"20250915163731685081\"}";
        String sign = generateSign(jsonBody);
        System.out.println("Generated Sign: " + sign);
    }

}