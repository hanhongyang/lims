package com.gmlimsqi.business.milkbinqualityinspection.service;

import com.gmlimsqi.business.milkbinqualityinspection.domain.OpMilkBinQualityInspection;

import java.util.List;

/**
 * 奶仓质检单Service接口
 * 
 * @author hhy
 * @date 2025-11-10
 */
public interface IOpMilkBinQualityInspectionService 
{
    /**
     * 查询奶仓质检单
     * 
     * @param id 奶仓质检单主键
     * @return 奶仓质检单
     */
    public OpMilkBinQualityInspection selectOpMilkBinQualityInspectionById(String id);

    /**
     * 查询奶仓质检单列表
     * 
     * @param opMilkBinQualityInspection 奶仓质检单
     * @return 奶仓质检单集合
     */
    public List<OpMilkBinQualityInspection> selectOpMilkBinQualityInspectionList(OpMilkBinQualityInspection opMilkBinQualityInspection);

    /**
     * 新增奶仓质检单
     * 
     * @param opMilkBinQualityInspection 奶仓质检单
     * @return 结果
     */
    public String insertOpMilkBinQualityInspection(OpMilkBinQualityInspection opMilkBinQualityInspection);

    /**
     * 修改奶仓质检单
     * 
     * @param opMilkBinQualityInspection 奶仓质检单
     * @return 结果
     */
    public int updateOpMilkBinQualityInspection(OpMilkBinQualityInspection opMilkBinQualityInspection);

    /**
     * 审核奶仓质检单
     * @param id
     * @return
     */
    int auditOpMilkBinQualityInspection(String id);

    /**
     * 查询奶仓质检单列表（按天）
     * @param opMilkBinQualityInspection
     * @return
     */
    List<OpMilkBinQualityInspection> listDay(OpMilkBinQualityInspection opMilkBinQualityInspection);

}
