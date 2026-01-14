package com.gmlimsqi.business.util;

import com.alibaba.fastjson2.JSON;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.gmlimsqi.business.rmts.entity.dto.CarInfoSyncDTO;
import com.gmlimsqi.business.rmts.entity.dto.PlanSyncDTO;
import com.gmlimsqi.business.rmts.entity.dto.QualitySyncDTO;
import com.gmlimsqi.business.rmts.entity.pojo.ApiResponse;
import com.gmlimsqi.business.rmts.entity.vo.CarInfoSyncVO;
import com.gmlimsqi.business.rmts.entity.vo.FactoryQualityVO;
import com.gmlimsqi.business.rmts.entity.vo.PlanSyncVO;
import com.gmlimsqi.common.core.domain.R;
import com.gmlimsqi.common.utils.sign.Md5Utils;
import okhttp3.*;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 生奶追溯系统-牧场LIMS系统Http请求工具类
 */
public class RmtsRanchLimsHttpUtil {
    // 接口基础URL（需根据实际部署地址修改）
    private static final String BASE_URL = "http://172.16.84.214:9080/milksourcelc";
    // 超时时间（秒）
    private static final int TIME_OUT = 30;
    // OkHttpClient单例（复用连接池）
    private static final OkHttpClient OK_HTTP_CLIENT = new OkHttpClient.Builder()
            .connectTimeout(TIME_OUT, TimeUnit.SECONDS)
            .readTimeout(TIME_OUT, TimeUnit.SECONDS)
            .writeTimeout(TIME_OUT, TimeUnit.SECONDS)
            .build();
    // Jackson ObjectMapper（配置日期格式）
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper()
            .registerModule(new JavaTimeModule()
                    .addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(
                            DateTimeFormatter.ofPattern("yyyy-MM-dd"))))
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

    /**
     * 1. 计划同步接口（/sync/plan）
     */
    public static ApiResponse<List<PlanSyncVO>> syncPlan(String request) {
        String url = BASE_URL + "/sync/plan";
        return sendPostRequest(request, url, new TypeReference<ApiResponse<List<PlanSyncVO>>>() {});
    }

    /**
     * 2. 车辆信息接口（/sync/carInfo）
     */
    public static ApiResponse<CarInfoSyncVO> syncCarInfo(String request) {
        String url = BASE_URL + "/sync/carInfo";
        return sendPostRequest(request, url, new TypeReference<ApiResponse<CarInfoSyncVO>>() {});
    }

    /**
     * 3. 质检信息同步接口（/sync/quality）
     */
    public static ApiResponse<Void> syncQuality(String request) {
        String url = BASE_URL + "/sync/quality";
        return sendPostRequest(request, url, new TypeReference<ApiResponse<Void>>() {});
    }

    /**
     * 4. 图片同步接口（/sync/photo）
     */
    public static ApiResponse<Void> syncPhoto(String request) {
        String url = BASE_URL + "/sync/photo";
        return sendPostRequest(request, url, new TypeReference<ApiResponse<Void>>() {});
    }

    /**
     * 5. 质检信息同步接口（下拉数据）（/sync/factoryQuality）
     */
    public static ApiResponse<List<FactoryQualityVO>> syncFactoryQuality(String request) {
        String url = BASE_URL + "/sync/factoryQuality";
        return sendPostRequest(request, url, new TypeReference<ApiResponse<List<FactoryQualityVO>>>() {});
    }

    private static <T, R> R sendPostRequest(String jsonBody, String url, TypeReference<R> responseType) {
        try {
            // 3. 生成签名
            String sign = SignatureUtils.generateSign(jsonBody);

            // 4. 构建请求体（Java 兼容版，无扩展方法问题）
            MediaType mediaType = MediaType.parse("application/json; charset=utf-8");
            RequestBody requestBody = RequestBody.create(
                    mediaType,
                    jsonBody.getBytes(StandardCharsets.UTF_8) // String 转 byte[]
            );

            // 5. 构建请求（设置请求头：sgin + Content-Type）
            Request okRequest = new Request.Builder()
                    .url(url)
                    .header("sgin", sign)
                    .header("Content-Type", "application/json; charset=utf-8")
                    .post(requestBody)
                    .build();

            // 后续逻辑不变...
            Response response = OK_HTTP_CLIENT.newCall(okRequest).execute();
            if (!response.isSuccessful()) {
                throw new RuntimeException("HTTP请求失败，状态码：" + response.code());
            }
            String responseBody = response.body().string();
            return OBJECT_MAPPER.readValue(responseBody, responseType);

        } catch (JsonProcessingException e) {
            throw new RuntimeException("JSON序列化/反序列化失败", e);
        } catch (Exception e) {
            throw new RuntimeException("HTTP请求异常：" + e.getMessage(), e);
        }
    }

    public static void main(String[] args) {
        // 1. 调用计划同步接口示例
        String planRequestJson = "{\"orderNumber\": \"20251104164316245479\"}";

        planRequestJson = JSON.toJSONString(planRequestJson);

        String sign = Md5Utils.hash(planRequestJson);

        sign = sign.toUpperCase();

        System.out.println(sign);

        String hash = Md5Utils.hash(sign + "PASTURELIMS");

        System.out.println(hash.toUpperCase());

    }

}