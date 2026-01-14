package com.gmlimsqi.framework.config.dingtalk.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 钉钉相关配置
 */
@Component
@ConfigurationProperties(prefix = "dingtalk")
@Data
public class DingTalkConfig {

    private String prefix;

    private Long tokenTimeout;

    private Long agentId;

    private String appId;

    private String appKey;

    private String appSecret;
    
    private String cropId;
    
}
