package com.gmlimsqi.framework.config.dingtalk.service;

import com.aliyun.dingtalkoauth2_1_0.models.GetAccessTokenResponse;
import com.aliyun.dingtalkoauth2_1_0.models.GetAccessTokenResponseBody;
import com.gmlimsqi.common.core.redis.RedisCache;
import com.gmlimsqi.common.exception.ServiceException;
import com.gmlimsqi.framework.config.DingTalkUtils;
import com.gmlimsqi.framework.config.dingtalk.config.DingTalkConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;


@Component
public class DingTalkToken {
    @Autowired
    private RedisCache redisCache;
    @Autowired
    private DingTalkConfig dingTalkConfig;
    
    
    public String getDingToken() {
        long expire = redisCache.getExpire(dingTalkConfig.getPrefix());
        if (expire < dingTalkConfig.getTokenTimeout()) {
            return setTokenCache();
        }
        return getTokenCache();
    }
    
    
    public String setTokenCache() {
        try {
            com.aliyun.dingtalkoauth2_1_0.Client client1 = DingTalkUtils.createDingtalkoauthClient();

            com.aliyun.dingtalkoauth2_1_0.models.GetAccessTokenRequest getAccessTokenRequest = new com.aliyun.dingtalkoauth2_1_0.models.GetAccessTokenRequest()
                    .setAppKey(dingTalkConfig.getAppKey())
                    .setAppSecret(dingTalkConfig.getAppSecret());

            GetAccessTokenResponse getAccessTokenResponse = client1.getAccessToken(getAccessTokenRequest);

            GetAccessTokenResponseBody body = getAccessTokenResponse.getBody();

            String accessToken = body.getAccessToken();
            int expireIn = body.getExpireIn().intValue();
            redisCache.setCacheObject(dingTalkConfig.getPrefix(), accessToken, expireIn-500, TimeUnit.SECONDS);
            return accessToken;
        } catch (Exception e) {
            throw new ServiceException(e.getMessage());
        }
    }
    
    public String getTokenCache() {
        return redisCache.getCacheObject(dingTalkConfig.getPrefix());
    }
    
}
