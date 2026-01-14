package com.gmlimsqi.business.milktankinspection.service;

import com.gmlimsqi.business.milktankinspection.domain.OpMilkTankInspection;

import java.util.List;

/**
 * 奶罐车质检Service接口
 * 
 * @author hhy
 * @date 2025-11-12
 */
public interface IOpMilkTankInspectionService 
{
    /**
     * 查询奶罐车质检
     * 
     * @param opMilkTankInspectionId 奶罐车质检主键
     * @return 奶罐车质检
     */
    public OpMilkTankInspection selectOpMilkTankInspectionByOpMilkTankInspectionId(String opMilkTankInspectionId);

    /**
     * 查询奶罐车质检列表
     * 
     * @param opMilkTankInspection 奶罐车质检
     * @return 奶罐车质检集合
     */
    public List<OpMilkTankInspection> selectOpMilkTankInspectionList(OpMilkTankInspection opMilkTankInspection);

    /**
     * 新增奶罐车质检
     * 
     * @param opMilkTankInspection 奶罐车质检
     * @return 结果
     */
    public int insertOpMilkTankInspection(OpMilkTankInspection opMilkTankInspection);

    /**
     * 修改奶罐车质检
     * 
     * @param opMilkTankInspection 奶罐车质检
     * @return 结果
     */
    public int updateOpMilkTankInspection(OpMilkTankInspection opMilkTankInspection);

    /**
     * 审核奶罐车质检
     * @param opMilkTankInspectionId
     * @return
     */
    int audit(String opMilkTankInspectionId);

}
