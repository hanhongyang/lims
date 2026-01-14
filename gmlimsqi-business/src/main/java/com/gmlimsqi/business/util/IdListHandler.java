package com.gmlimsqi.business.util;

import org.apache.commons.collections4.CollectionUtils;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 逗号分隔ID字符串处理工具类
 */
public class IdListHandler {

    /**
     * 步骤1：解析逗号分隔的ID字符串为去重、非空的ID列表
     * @param idStr 逗号分隔的ID字符串（如 "1001,1002,1003" 或 " 1001 , 1002 " 或 null/""）
     * @return 去重后的非空ID列表（空输入返回空列表）
     */
    public static List<String> parseIdStr(String idStr) {
        // 1. 处理空输入（null或空字符串）
        if (idStr == null || idStr.trim().isEmpty()) {
            return Collections.emptyList();
        }

        // 2. 分割字符串（逗号分隔）→ 去除空格 → 过滤空字符串 → 去重
        return Arrays.stream(idStr.split(","))
                .map(String::trim) // 去除前后空格（处理 "1001 , 1002" 这种情况）
                .filter(trimmedId -> !trimmedId.isEmpty()) // 过滤分割后为空的元素（如 "1001,,1002" → 过滤中间空值）
                .distinct() // 去重（处理 "1001,1001,1002" 这种情况）
                .collect(Collectors.toList());
    }

    /**
     * 单个ID查询（模拟DAO层方法，需根据实际业务实现）
     * @param id 数据ID
     * @return 单个数据实体
     */
    private YourDataEntity queryDataById(String id) {
        // 实际业务逻辑：如MyBatis查询、Redis查询、HTTP请求等
        // 示例：
        // return yourMapper.selectById(id);
        // return redisTemplate.opsForValue().get("data:" + id);
        return new YourDataEntity(id, "测试数据-" + id);
    }

    /**
     * 批量ID查询（推荐！模拟DAO层批量查询方法，需根据实际业务实现）
     * @param idList ID列表
     * @return 批量查询结果
     */
    private List<YourDataEntity> batchQueryDataByIds(List<String> idList) {
        // 实际业务逻辑：如MyBatis的selectList（传入idList）
        // 示例：
        // return yourMapper.selectBatchIds(idList);
        return idList.stream()
                .map(id -> new YourDataEntity(id, "批量查询数据-" + id))
                .collect(Collectors.toList());
    }

    /**
     * 模拟数据实体（替换为你的实际实体类）
     */
    @lombok.Data
    static class YourDataEntity {
        private String id;
        private String name;

        public YourDataEntity(String id, String name) {
            this.id = id;
            this.name = name;
        }
    }

}