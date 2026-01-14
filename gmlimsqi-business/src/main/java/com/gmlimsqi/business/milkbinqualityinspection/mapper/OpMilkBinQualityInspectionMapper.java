package com.gmlimsqi.business.milkbinqualityinspection.mapper;

import com.gmlimsqi.business.milkbinqualityinspection.domain.OpMilkBinQualityInspection;

import java.util.List;

/**
 * 奶仓质检单Mapper接口
 * 
 * @author hhy
 * @date 2025-11-10
 */
public interface OpMilkBinQualityInspectionMapper 
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
    public int insertOpMilkBinQualityInspection(OpMilkBinQualityInspection opMilkBinQualityInspection);

    /**
     * 修改奶仓质检单
     * 
     * @param opMilkBinQualityInspection 奶仓质检单
     * @return 结果
     */
    public int updateOpMilkBinQualityInspection(OpMilkBinQualityInspection opMilkBinQualityInspection);

    /**
     * 通过奶仓质检单主键更新删除标志
     *
     * @param id 奶仓质检单ID
     * @return 结果
     */
    public int updateDeleteFlagById(String id);

    /**
     * 批量通过奶仓质检单主键更新删除标志
     *
     * @param id 奶仓质检单ID
     * @return 结果
     */
    public int updateDeleteFlagByIds(String[] ids);



}
