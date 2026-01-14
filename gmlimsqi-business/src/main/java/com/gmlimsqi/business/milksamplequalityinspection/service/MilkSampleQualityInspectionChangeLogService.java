package com.gmlimsqi.business.milksamplequalityinspection.service;

import com.gmlimsqi.business.milksamplequalityinspection.domain.MilkSampleQualityInspectionChangeLogDO;
import com.gmlimsqi.business.milksamplequalityinspection.domain.OpMilkSampleQualityInspection;

import java.util.List;

public interface MilkSampleQualityInspectionChangeLogService {

    /**
     * 记录一条更新日志
     * @param oldData 旧数据
     * @param newData 新数据
     * @param changeReason 变更原因
     * @return 影响行数
     */
    int recordUpdateLog(OpMilkSampleQualityInspection oldData,
                        OpMilkSampleQualityInspection newData,
                        String changeReason);

    /**
     * 根据奶样质检主表ID查询变更日志列表（按时间倒序）
     */
    List<MilkSampleQualityInspectionChangeLogDO> getListByMilkId(String opMilkSampleQualityInspectionId);

    /**
     * 条件查询列表（可用于管理端）
     */
    List<MilkSampleQualityInspectionChangeLogDO> getList(MilkSampleQualityInspectionChangeLogDO query);
}
