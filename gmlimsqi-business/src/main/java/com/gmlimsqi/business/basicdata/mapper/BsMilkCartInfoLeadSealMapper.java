package com.gmlimsqi.business.basicdata.mapper;

import com.gmlimsqi.business.basicdata.domain.BsMilkCartInfoLeadSeal;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface BsMilkCartInfoLeadSealMapper {
    /**
     * 插入一条铅封信息
     */
    int insert(BsMilkCartInfoLeadSeal record);

    /**
     * 根据主键ID删除铅封信息
     */
    int deleteById(String milkCartInfoLeadSealId);

    /**
     * 根据主表ID删除所有子表数据
     */
    int deleteByMilkCartInfoId(String milkCartInfoId);

    /**
     * 根据主表ID查询铅封信息列表
     */
    List<BsMilkCartInfoLeadSeal> selectByMilkCartInfoId(String milkCartInfoId);

    /**
     * 根据主表ID和灌口查询铅封信息
     */
    List<BsMilkCartInfoLeadSeal> selectByMilkCartInfoIdAndFillingPort(
            @Param("milkCartInfoId") String milkCartInfoId,
            @Param("fillingPort") String fillingPort);

    /**
     * 更新铅封信息
     */
    int update(BsMilkCartInfoLeadSeal record);

    /**
     * 根据ID查询铅封信息
     */
    BsMilkCartInfoLeadSeal selectById(String milkCartInfoLeadSealId);

    /**
     * 批量插入子表数据
     */
    void insertBatch(List<BsMilkCartInfoLeadSeal> milkCartInfoLeadSealList);

}