package com.gmlimsqi.web.controller.system;

import com.aliyun.dingtalkoauth2_1_0.models.CreateJsapiTicketResponse;
import com.aliyun.teautil.models.RuntimeOptions;
import com.gmlimsqi.framework.config.dingtalk.service.DingTalkToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Formatter;
import java.util.Random;

/**
 * 计算dd.config的签名参数
 **/
@Component
public class DdConfigSign {
    
    @Autowired
    private DingTalkToken dingTalkToken;

    /**
     * 计算dd.config的签名参数
     *
     * @param jsticket  通过微应用appKey获取的jsticket
     * @param nonceStr  自定义固定字符串
     * @param timeStamp 当前时间戳
     * @param url       调用dd.config的当前页面URL
     * @return
     * @throws Exception
     */
    public String dingSign(String jsticket, String nonceStr, long timeStamp, String url) throws Exception {
        String plain = "jsapi_ticket=" + jsticket + "&noncestr=" + nonceStr + "&timestamp=" + String.valueOf(timeStamp)
                + "&url=" + decodeUrl(url);
        try {
            MessageDigest sha1 = MessageDigest.getInstance("SHA-256");
            sha1.reset();
            sha1.update(plain.getBytes(StandardCharsets.UTF_8));
            return byteToHex(sha1.digest());
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }
    
    // 字节数组转化成十六进制字符串
    private static String byteToHex(final byte[] hash) {
        Formatter formatter = new Formatter();
        for (byte b : hash) {
            formatter.format("%02x", b);
        }
        String result = formatter.toString();
        formatter.close();
        return result;
    }
    
    /**
     * 因为ios端上传递的url是encode过的，android是原始的url。开发者使用的也是原始url,
     * 所以需要把参数进行一般urlDecode
     *
     * @param url
     * @return
     * @throws Exception
     */
    private static String decodeUrl(String url) throws Exception {
        URL urler = new URL(url);
        StringBuilder urlBuffer = new StringBuilder();
        urlBuffer.append(urler.getProtocol());
        urlBuffer.append(":");
        if (urler.getAuthority() != null && !urler.getAuthority().isEmpty()) {
            urlBuffer.append("//");
            urlBuffer.append(urler.getAuthority());
        }
        if (urler.getPath() != null) {
            urlBuffer.append(urler.getPath());
        }
        if (urler.getQuery() != null) {
            urlBuffer.append('?');
            urlBuffer.append(URLDecoder.decode(urler.getQuery(), "utf-8"));
        }
        return urlBuffer.toString();
    }
    
    public static String getRandomStr(int count) {
        String base = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < count; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }
    
    /**
     * 使用 Token 初始化账号Client
     * @return Client
     * @throws Exception
     */
    public static com.aliyun.dingtalkoauth2_1_0.Client createClient() throws Exception {
        com.aliyun.teaopenapi.models.Config config = new com.aliyun.teaopenapi.models.Config();
        config.protocol = "https";
        config.regionId = "central";
        return new com.aliyun.dingtalkoauth2_1_0.Client(config);
    }
    
    public String getJsapiTicket() throws Exception {
        String token = dingTalkToken.getDingToken();
        com.aliyun.dingtalkoauth2_1_0.Client client = null;
        try {
            client = DdConfigSign.createClient();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        com.aliyun.dingtalkoauth2_1_0.models.CreateJsapiTicketHeaders createJsapiTicketHeaders = new com.aliyun.dingtalkoauth2_1_0.models.CreateJsapiTicketHeaders();
        createJsapiTicketHeaders.xAcsDingtalkAccessToken = token;
        
        CreateJsapiTicketResponse jsapiTicketWithOptions
                = client.createJsapiTicketWithOptions(createJsapiTicketHeaders, new RuntimeOptions());
        
        return jsapiTicketWithOptions.getBody().getJsapiTicket();
    }
    
}