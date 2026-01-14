package com.gmlimsqi.common.utils.email;

import lombok.Getter;

/**
 * 主流邮箱SMTP配置枚举（发件人邮箱对应的服务器信息）
 */
@Getter
public enum EmailSmtpConfig {
    // QQ邮箱（包括企业邮）
    QQ_EMAIL("smtp.qq.com", 465, true),
    // 163邮箱（网易）
    NETEASE_163("smtp.163.com", 465, true),
    // 126邮箱
    NETEASE_126("smtp.126.com", 465, true),
    // 企业微信邮箱（腾讯企业邮）
    QQ_EXMAIL("smtp.exmail.qq.com", 465, true),
    // Gmail（需科学上网）
    GMAIL("smtp.gmail.com", 587, false), // Gmail推荐587端口（TLS）
    // 新浪邮箱
    SINA("smtp.sina.com.cn", 465, true),
    // 阿里云企业邮
    ALIBABA_ENTERPRISE("smtp.mxhichina.com", 465, true),
    // --- 新增：光明乳业邮箱配置 ---
    // 注意：请向IT确认 host 是否为 smtp.brightdairy.com，如果托管在腾讯企业邮，这里应填 smtp.exmail.qq.com
    BRIGHT_DAIRY("smtp.brightdairy.com", 25, false);

    private final String smtpHost;  // SMTP服务器地址
    private final int smtpPort;     // 端口
    private final boolean isSSL;    // 是否启用SSL（465端口通常为true，587为false）

    EmailSmtpConfig(String smtpHost, int smtpPort, boolean isSSL) {
        this.smtpHost = smtpHost;
        this.smtpPort = smtpPort;
        this.isSSL = isSSL;
    }

    /**
     * 根据发件人邮箱后缀自动匹配SMTP配置
     * 例如：邮箱xxx@qq.com → 匹配QQ_EMAIL
     */
    public static EmailSmtpConfig getByEmailSuffix(String fromEmail) {
        if (fromEmail == null || !fromEmail.contains("@")) {
            throw new IllegalArgumentException("无效的发件人邮箱：" + fromEmail);
        }
        String suffix = fromEmail.split("@")[1].toLowerCase(); // 提取后缀（如qq.com）
        switch (suffix) {
            case "qq.com":
                return QQ_EMAIL;
            case "163.com":
                return NETEASE_163;
            case "126.com":
                return NETEASE_126;
            case "exmail.qq.com":
                return QQ_EXMAIL;
            case "gmail.com":
                return GMAIL;
            case "sina.com.cn":
                return SINA;
            case "aliyun.com":
            case "mxhichina.com":
                return ALIBABA_ENTERPRISE;
            case "brightdairy.com":
                return BRIGHT_DAIRY;
            default:
                throw new IllegalArgumentException("未支持的邮箱后缀：" + suffix + "，请手动指定SMTP配置");
        }
    }
}