package com.gmlimsqi.common.change.base;

public interface BaseChangeLogService<T> {

    /**
     * 记录一条日志
     *
     * @param bizId        业务主键
     * @param oldData      旧数据
     * @param newData      新数据
     * @param changeReason 变更原因
     * @param opType       操作类型
     * @return 影响行数
     */
    int insertLog(
            String bizId,
            T oldData,
            T newData,
            String opType,
            String changeReason
    );
}
