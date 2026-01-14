package com.gmlimsqi.business.inspectionmilktankers.mapper;

import com.gmlimsqi.business.inspectionmilktankers.domain.OpInspectionMilkTankers;

import java.util.List;

/**
 * 奶罐车检查Mapper接口
 * 
 * @author hhy
 * @date 2025-11-10
 */
public interface OpInspectionMilkTankersMapper 
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
    public int insertOpInspectionMilkTankers(OpInspectionMilkTankers opInspectionMilkTankers);

    /**
     * 修改奶罐车检查
     * 
     * @param opInspectionMilkTankers 奶罐车检查
     * @return 结果
     */
    public int updateOpInspectionMilkTankers(OpInspectionMilkTankers opInspectionMilkTankers);

    /**
     * 通过奶罐车检查主键更新删除标志
     *
     * @param inspectionMilkTankersId 奶罐车检查ID
     * @return 结果
     */
    public int updateDeleteFlagById(String inspectionMilkTankersId);

    /**
     * 批量通过奶罐车检查主键更新删除标志
     *
     * @param inspectionMilkTankersId 奶罐车检查ID
     * @return 结果
     */
    public int updateDeleteFlagByIds(String[] inspectionMilkTankersIds);



}
