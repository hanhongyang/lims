package com.gmlimsqi.framework.config;

import com.aliyun.dingtalkcalendar_1_0.models.CreateEventRequest;
import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.OapiV2UserGetRequest;
import com.dingtalk.api.request.OapiV2UserGetbymobileRequest;
import com.dingtalk.api.response.OapiV2UserGetResponse;
import com.dingtalk.api.response.OapiV2UserGetbymobileResponse;
import com.taobao.api.ApiException;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class DingTalkUtils {
    
    //    通过手机号获取ding userid
    public static String getDingUserIdByPhone(String phone, String token) {
        try {
            DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/topapi/v2/user/getbymobile");
            OapiV2UserGetbymobileRequest req = new OapiV2UserGetbymobileRequest();
            req.setMobile(phone);
            req.setSupportExclusiveAccountSearch(true);
            OapiV2UserGetbymobileResponse rsp = client.execute(req, token);
            if (rsp.getErrcode() == 0){
                return rsp.getResult().getUserid();
            }
        } catch (ApiException e) {
            log.error(e.getMessage());
//            throw new ServiceException(e.getMessage());
        }
        return null;
    }
    
    public static String getUnionIdByUserId(String userId, String token) {
        try {
            DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/topapi/v2/user/get");
            OapiV2UserGetRequest req = new OapiV2UserGetRequest();
            req.setUserid(userId);
            OapiV2UserGetResponse rsp = client.execute(req, token);
            if (rsp.getErrcode() == 0){
                return rsp.getResult().getUnionid();
            }
        } catch (ApiException e) {
            log.error(e.getMessage());
//            throw new ServiceException(e.getMessage());
        }
        return null;
    }
    
    public static List<CreateEventRequest.CreateEventRequestAttendees>
    CreateEventRequestAttendees(String[] unionIds, String token) {
        List<CreateEventRequest.CreateEventRequestAttendees> list = new ArrayList<>();
//        根据手机号获取unionId
        for (String unionId : unionIds) {
            CreateEventRequest.CreateEventRequestAttendees attendees =
                    new CreateEventRequest.CreateEventRequestAttendees()
                            .setId(unionId)
                            .setIsOptional(false);
            list.add(attendees);
        }
        return list;
    }
    
    public static com.aliyun.dingtalkcalendar_1_0.Client createDingtalkcalendarClient() throws Exception {
        com.aliyun.teaopenapi.models.Config config = new com.aliyun.teaopenapi.models.Config();
        config.protocol = "https";
        config.regionId = "central";
        return new com.aliyun.dingtalkcalendar_1_0.Client(config);
    }
    
    public static com.aliyun.dingtalktodo_1_0.Client createDingtalktodoClient() throws Exception {
        com.aliyun.teaopenapi.models.Config config = new com.aliyun.teaopenapi.models.Config();
        config.protocol = "https";
        config.regionId = "central";
        return new com.aliyun.dingtalktodo_1_0.Client(config);
    }
    
    public static com.aliyun.dingtalkoauth2_1_0.Client createDingtalkoauthClient() throws Exception {
        com.aliyun.teaopenapi.models.Config config = new com.aliyun.teaopenapi.models.Config();
        config.protocol = "https";
        config.regionId = "central";
        return new com.aliyun.dingtalkoauth2_1_0.Client(config);
    }
    
}
