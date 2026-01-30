package com.gmlimsqi.business.inspectionmilktankers.service;

import com.gmlimsqi.business.inspectionmilktankers.domain.OpInspectionMilkTankers;

import java.util.List;

/**
 * 奶罐车检查Service接口
 * 
 * @author hhy
 * @date 2025-11-10
 */
public interface IOpInspectionMilkTankersService 
{
    /**
     * 查询奶罐车检查
     * 
     * @param inspectionMilkTankersId 奶罐车检查主键
     * @return 奶罐车检查
     */
    public OpInspectionMilkTankers selectOpInspectionMilkTankersByInspectionMilkTankersId(String inspectionMilkTankersId);

    /**
     * 查询奶罐车检查列表
     * 
     * @param opInspectionMilkTankers 奶罐车检查
     * @return 奶罐车检查集合
     */
    public List<OpInspectionMilkTankers> selectOpInspectionMilkTankersList(OpInspectionMilkTankers opInspectionMilkTankers);

    /**
     * 新增奶罐车检查
     * 
     * @param opInspectionMilkTankers 奶罐车检查
     * @return 结果
     */
    public String insertOpInspectionMilkTankers(OpInspectionMilkTankers opInspectionMilkTankers);

    /**
     * 修改奶罐车检查
     * 
     * @param opInspectionMilkTankers 奶罐车检查
     * @return 结果
     */
    public int updateOpInspectionMilkTankers(OpInspectionMilkTankers opInspectionMilkTankers);

    /**
     * 审核奶罐车检查单
     * @param inspectionMilkTankersId
     * @return
     */
    int auditOpInspectionMilkTankers(String inspectionMilkTankersId);

    /**
     * 推送奶源
     * @param inspectionMilkTankersId
     * @return
     */
    int pushMilkSource(String inspectionMilkTankersId);

    /**
     * 变更计划单
     * @param opInspectionMilkTankers
     * @return
     */
    int changePlan(OpInspectionMilkTankers opInspectionMilkTankers);
}
