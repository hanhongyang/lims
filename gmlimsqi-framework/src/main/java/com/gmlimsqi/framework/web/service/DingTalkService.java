package com.gmlimsqi.framework.web.service;

import com.aliyun.dingtalkoauth2_1_0.Client;
import com.aliyun.dingtalkoauth2_1_0.models.*;
import com.aliyun.tea.TeaException;
import com.aliyun.teaopenapi.models.Config;
import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.OapiV2UserGetRequest;
import com.dingtalk.api.request.OapiV2UserGetuserinfoRequest;
import com.dingtalk.api.response.OapiV2UserGetResponse;
import com.dingtalk.api.response.OapiV2UserGetuserinfoResponse;
import com.gmlimsqi.common.core.redis.RedisCache;
import com.gmlimsqi.common.exception.ServiceException;
import com.gmlimsqi.common.utils.StringUtils;
import com.gmlimsqi.framework.config.dingtalk.service.DingTalkToken;
import com.gmlimsqi.common.utils.StringUtils;
import com.taobao.api.ApiException;
import org.apache.poi.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * 钉钉绘画处理
 *
 * @author Administrator
 * @date 2022/03/15
 */
@Component
public class DingTalkService {
    private static final String accessToken_prefix = "DingTalk_AccessToken";
    private static final String ssoAccessToken_prefix = "DingTalk_SsoAccessToken";
    private static int expireTime = 7200;
    private static int TEN_MINUTES = 60 * 10;
    @Autowired
    private RedisCache redisCache;
    @Autowired
    private DingTalkToken dingTalkToken;

    public OapiV2UserGetuserinfoResponse.UserGetByCodeResponse getUserInfo(String code) throws Exception {

        try {
            DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/topapi/v2/user/getuserinfo");
            OapiV2UserGetuserinfoRequest req = new OapiV2UserGetuserinfoRequest();
            req.setCode(code);
            OapiV2UserGetuserinfoResponse rsp = client.execute(req, dingTalkToken.getDingToken());
            if (!StringUtils.isEmpty(rsp.getErrorCode()) && rsp.getErrorCode().equals("0")) {
                OapiV2UserGetuserinfoResponse.UserGetByCodeResponse result = rsp.getResult();
                return result;
            } else {
                throw new ServiceException(rsp.getErrorCode() + ":" + rsp.getErrmsg());
            }

        } catch (ApiException err) {
            throw new ServiceException(err.getMessage());
        } catch (ServiceException err) {
            throw new ServiceException(err.getMessage());
        } catch (Exception err) {
            throw new Exception(err.getMessage());
        }
    }


    public OapiV2UserGetResponse.UserGetResponse getUserByCoe(String code) throws Exception {
        OapiV2UserGetuserinfoResponse.UserGetByCodeResponse userInfo = getUserInfo(code);
        if (StringUtils.isNull(userInfo)) {
            throw new ServiceException("获取信息失败");
        } else {
            try {
                DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/topapi/v2/user/get");
                OapiV2UserGetRequest req = new OapiV2UserGetRequest();
                req.setUserid(userInfo.getUserid());
                OapiV2UserGetResponse rsp = client.execute(req, dingTalkToken.getDingToken());
                if (StringUtils.isNotEmpty(rsp.getErrorCode()) && rsp.getErrorCode().equals("0")) {
                    OapiV2UserGetResponse.UserGetResponse result = rsp.getResult();
                    return result;
                } else {
                    throw new ServiceException(rsp.getErrorCode() + ":" + rsp.getErrmsg());
                }

            } catch (ApiException e) {
                throw new ServiceException(e.getMessage());
            } catch (ServiceException err) {
                throw new ServiceException(err.getMessage());
            } catch (Exception err) {
                throw new Exception(err.getMessage());
            }
        }
    }

        public String getSsoAccessToken () throws Exception {
            long expire = redisCache.getExpire(ssoAccessToken_prefix);
            if (expire < TEN_MINUTES) {
                return setSsoAccessTokenCache();
            }
            return getSsoAccessTokenCache();
        }

        public String setSsoAccessTokenCache () throws Exception {
            Client client = createClient();
            GetSsoAccessTokenRequest ssoAccessTokenRequest = new GetSsoAccessTokenRequest();
            ssoAccessTokenRequest.setCorpid("ding2ce60e1031a6c61e24f2f5cc6abecb85")
                    .setSsoSecret("IosAoFnHvKPHxWnMMoiImOi5xuWnq3NASu4lLLftXG__FFiH1Vqp8JoAsrFIVD6A");
            try {
                GetSsoAccessTokenResponse ssoAccessTokenResponse = client.getSsoAccessToken(ssoAccessTokenRequest);
                GetSsoAccessTokenResponseBody body = ssoAccessTokenResponse.getBody();
                String accessToken = body.getAccessToken();
                int expireIn = body.getExpireIn().intValue();
                redisCache.setCacheObject(ssoAccessToken_prefix, accessToken, expireIn, TimeUnit.SECONDS);
                return accessToken;
            } catch (TeaException err) {
                throw new ServiceException(err.getMessage());
            } catch (Exception err) {
                throw new ServiceException(err.getMessage());
            }
        }

        public String getSsoAccessTokenCache () {
            return redisCache.getCacheObject(ssoAccessToken_prefix);
        }

        public String getAccessToken () throws Exception {
            long expire = redisCache.getExpire(accessToken_prefix);
            if (expire < TEN_MINUTES) {
                return setAccessTokenCache();
            }
            return getAccessTokenCache();
        }

        public String setAccessTokenCache () throws Exception {
            Client client = createClient();
            GetAccessTokenRequest accessTokenRequest = new GetAccessTokenRequest();
            accessTokenRequest.setAppKey("dingcrtm9cme3ade60cr")
                    .setAppSecret("wZMO7r1tgzG0zHxYCV3pq4NFpGgXXGEebtKSeyVc_3Z8Igje3cKW4Ekz9SKp6e4K");
            try {
                GetAccessTokenResponse accessTokenResponse = client.getAccessToken(accessTokenRequest);
                GetAccessTokenResponseBody body = accessTokenResponse.getBody();
                String accessToken = body.getAccessToken();
                int expireIn = body.getExpireIn().intValue();
                redisCache.setCacheObject(accessToken_prefix, accessToken, expireIn, TimeUnit.SECONDS);
                return accessToken;
            } catch (TeaException err) {
                throw new ServiceException(err.getMessage());
            } catch (Exception err) {
                throw new ServiceException(err.getMessage());
            }
        }

        public String getAccessTokenCache () {
            return redisCache.getCacheObject(accessToken_prefix);
        }

        public Client createClient () throws Exception {
            Config config = new Config();
            config.protocol = "https";
            config.regionId = "central";
            return new Client(config);
        }
    }
