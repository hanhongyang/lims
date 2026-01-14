package com.gmlimsqi.sap.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gmlimsqi.sap.accept.domain.sapresponse.FrozenSemenQcResponse;
import com.gmlimsqi.sap.accept.domain.sapresponse.InOutInfoResponse;
import com.gmlimsqi.sap.accept.domain.sapresponse.InboundInspectionResponse;
import com.gmlimsqi.sap.accept.domain.vo.CheckResRetJudgeVO;
import com.gmlimsqi.sap.accept.domain.vo.FrozenSemenQcDetailVO;
import com.gmlimsqi.sap.accept.domain.vo.InOutInfoVO;
import com.gmlimsqi.sap.accept.domain.vo.InboundInspectionDetailVO;

import java.util.*;

/**
 * JSON解析工具类（终极兼容版：无Jackson API依赖，解决大小写+空格+兼容问题）
 */
public class JsonParseUtils {

    // 基础ObjectMapper：仅保留必要配置，不依赖任何高级API
    private static final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * 原有方法：解析InOutInfoVO列表（无改动）
     */
    public static List<InOutInfoVO> parseInOutInfo(String json) throws JsonProcessingException {
        InOutInfoResponse response = objectMapper.readValue(json, InOutInfoResponse.class);
        return response.getData();
    }

    /**
     * 原有方法：解析CheckResRetJudgeVO（无改动）
     */
    public static CheckResRetJudgeVO parseCheckResRetJudge(String json) throws JsonProcessingException {
        return objectMapper.readValue(json, CheckResRetJudgeVO.class);
    }

    /**
     * 核心方法：解析入库检验批明细（中转Map处理，彻底解决大小写+空格问题）
     */
    public static List<InboundInspectionDetailVO> parseInboundInspectionDetail(String result) {
        try {
            // 第一步：解析外层响应，拿到原始data数组（不直接转实体类）
            InboundInspectionResponse response = objectMapper.readValue(result, InboundInspectionResponse.class);
            if (response == null || response.getData() == null) {
                return Collections.emptyList();
            }

            // 第二步：将原始data数组转为JSON字符串（便于后续转Map处理）
            String dataJson = objectMapper.writeValueAsString(response.getData());
            // 转为List<Map>：每个Map对应一个明细对象
            List<Map<String, Object>> originalMapList = objectMapper.readValue(dataJson, List.class);

            // 第三步：处理每个Map的大小写和空格
            List<InboundInspectionDetailVO> detailVOList = new ArrayList<>();
            for (Map<String, Object> originalMap : originalMapList) {
                // 处理Map：key转大写+value去空格
                Map<String, Object> processedMap = processMap(originalMap);
                // 将处理后的Map转为实体类
                InboundInspectionDetailVO detailVO = objectMapper.convertValue(processedMap, InboundInspectionDetailVO.class);
                detailVOList.add(detailVO);
            }

            return detailVOList; // 保持返回值类型不变
        } catch (JsonProcessingException e) {
            throw new RuntimeException("解析入库检验批结果失败" + e);
        }
    }

    /**
     * 原有方法：解析FrozenSemenQcDetailVO列表（无改动）
     */
    public static List<FrozenSemenQcDetailVO> parseFrozenSemenQcDetail(String result) {
        try {
            FrozenSemenQcResponse response = objectMapper.readValue(result, FrozenSemenQcResponse.class);
            return response.getData();
        } catch (JsonProcessingException e) {
            throw new RuntimeException("解析冻 semen 检验批结果失败" + e);
        }
    }

    /**
     * 核心工具方法：处理Map（key统一转大写+value去空格）
     */
    private static Map<String, Object> processMap(Map<String, Object> originalMap) {
        Map<String, Object> processedMap = new HashMap<>();
        if (originalMap == null || originalMap.isEmpty()) {
            return processedMap;
        }

        for (Map.Entry<String, Object> entry : originalMap.entrySet()) {
            // 1. Key处理：统一转为大写（和实体类字段名匹配，解决大小写重复）
            String key = entry.getKey() == null ? "" : entry.getKey().toUpperCase(Locale.ENGLISH);
            // 2. Value处理：字符串类型去空格，全空格转null；其他类型保持不变
            Object value = entry.getValue();
            Object processedValue = processValue(value);

            // 覆盖重复key（如WERK和werk最终只保留一个，value取最后一个）
            processedMap.put(key, processedValue);
        }
        return processedMap;
    }

    /**
     * 辅助方法：处理单个值（字符串去空格，全空格转null）
     */
    private static Object processValue(Object value) {
        if (value == null) {
            return null;
        }
        // 仅处理字符串类型
        if (value instanceof String) {
            String strValue = (String) value;
            String trimmed = strValue.trim();
            // 全空格转null，否则返回去空格后的值
            return trimmed.isEmpty() ? null : trimmed;
        }
        // 非字符串类型（数字、布尔等）直接返回
        return value;
    }
}