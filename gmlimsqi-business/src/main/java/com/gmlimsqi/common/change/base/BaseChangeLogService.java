package com.gmlimsqi.common.change.base;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gmlimsqi.common.change.domain.BaseChangeLogDO;
import com.gmlimsqi.common.change.domain.ChangeCompareResult;
import com.gmlimsqi.common.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * 变更日志服务抽象类
 *
 * @param <T>   业务对象
 * @param <LOG> 日志对象
 */
@RequiredArgsConstructor
public abstract class BaseChangeLogService<T, LOG extends BaseChangeLogDO> {

    // jackson
    protected final ObjectMapper objectMapper;

    /**
     * 获取业务主键（不同表字段不同）
     *
     * @param data 业务对象
     * @return 业务主键
     */
    protected abstract String getBizId(T data);

    /**
     * 比对业务字段（不同表字段不同）
     *
     * @param oldData 旧数据
     * @param newData 新数据
     * @return 差异字段
     */
    protected abstract ChangeCompareResult compare(T oldData, T newData);

    /**
     * 构建日志对象（子类需要设置 bizId 字段）
     *
     * @param bizId        业务主键
     * @param opType       操作类型
     * @param changeReason 变更原因
     * @param oldDataJson  旧数据 JSON
     * @param newDataJson  新数据 JSON
     * @param diffDataJson 差异字段 JSON
     * @return 日志对象
     */
    protected abstract LOG buildLog(
            String bizId,
            String opType,
            String changeReason,
            String oldDataJson,
            String newDataJson,
            String diffDataJson
    );

    /**
     * 构建日志对象通用字段（子类复用）
     *
     * @param log          日志对象
     * @param opType       操作类型
     * @param changeReason 变更原因
     * @param oldDataJson  旧数据 JSON
     * @param newDataJson  新数据 JSON
     * @param diffDataJson 差异字段 JSON
     * @return 日志对象
     */
    protected LOG buildBaseLog(LOG log,
                               String opType,
                               String changeReason,
                               String oldDataJson,
                               String newDataJson,
                               String diffDataJson) {

        // 1. 操作类型
        log.setOpType(opType);

        // 2. 变更原因
        log.setChangeReason(changeReason);

        // 3. old/new/diff
        log.setOldData(oldDataJson);
        log.setNewData(newDataJson);
        log.setDiffData(diffDataJson);

        // 4. 填充基础字段
        log.fillCreateInfo();

        return log;
    }

    /**
     * 入库（不同表 mapper 不同）
     *
     * @param log 日志对象
     * @return 影响行数
     */
    protected abstract int doInsert(LOG log);

    /**
     * 比对字段
     */
    protected void compareField(String fieldName,
                                Object oldVal,
                                Object newVal,
                                Map<String, Object> oldData,
                                Map<String, Object> newData,
                                Map<String, Object> diffData) {

        // 相等就不记
        if (!Objects.equals(oldVal, newVal)) {
            // 1. 记录旧/新字段值
            oldData.put(fieldName, oldVal);
            newData.put(fieldName, newVal);

            // 2. 记录 diff
            Map<String, Object> diffItem = new HashMap<>();
            diffItem.put("old", oldVal);
            diffItem.put("new", newVal);
            diffData.put(fieldName, diffItem);
        }
    }

    /**
     * 记录 UPDATE 日志（统一复用）
     */
    public int recordUpdateLog(T oldData, T newData, String changeReason) {
        // 1. 参数校验
        if (oldData == null) {
            throw new BusinessException("旧数据不能为空");
        }
        if (newData == null) {
            throw new BusinessException("新数据不能为空");
        }

        // 2. 获取业务主键
        String bizId = getBizId(newData);
        if (!StringUtils.hasText(bizId)) {
            throw new BusinessException("业务主键不能为空");
        }

        // 3. 生成 diff（只记录发生变化的字段）
        ChangeCompareResult result = compare(oldData, newData);
        // 没变化就不记日志
        if (result.isEmpty()) {
            return 0;
        }

        // 4. JSON 化 + 构建日志对象 + 入库
        try {
            String oldJson = objectMapper.writeValueAsString(result.getOldData());
            String newJson = objectMapper.writeValueAsString(result.getNewData());
            String diffJson = objectMapper.writeValueAsString(result.getDiffData());

            LOG log = buildLog(
                    bizId,
                    "UPDATE",
                    changeReason,
                    oldJson,
                    newJson,
                    diffJson
            );

            return doInsert(log);
        } catch (Exception e) {
            throw new BusinessException("生成变更日志失败：" + e.getMessage());
        }
    }
}