package com.gmlimsqi.common.change.domain;

import lombok.Data;

import java.util.Map;

@Data
public class ChangeCompareResult {

    /**
     * 旧数据（仅变化字段）
     */
    private Map<String, Object> oldData;

    /**
     * 新数据（仅变化字段）
     */
    private Map<String, Object> newData;

    /**
     * 变化数据（字段-> old/new）
     */
    private Map<String, Object> diffData;

    /**
     * 无变化返回 true
     */
    public boolean isEmpty() {
        return diffData == null || diffData.isEmpty();
    }
}