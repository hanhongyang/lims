package com.gmlimsqi.business.labtest.service;

import com.gmlimsqi.business.labtest.domain.OpNearInfraredSummary;

import java.util.List;

/**
 * 近红外汇总Service接口
 * 
 * @author hhy
 * @date 2025-10-29
 */
public interface IOpNearInfraredSummaryService 
{
    /**
     * 查询近红外汇总
     * 
     * @param dairylandId 近红外汇总主键
     * @return 近红外汇总
     */
    public OpNearInfraredSummary selectOpNearInfraredSummaryByDairylandId(String dairylandId);

    /**
     * 查询近红外汇总列表
     * 
     * @param opNearInfraredSummary 近红外汇总
     * @return 近红外汇总集合
     */
    public List<OpNearInfraredSummary> selectOpNearInfraredSummaryList(OpNearInfraredSummary opNearInfraredSummary);

    /**
     * 新增近红外汇总
     * 
     * @param opNearInfraredSummary 近红外汇总
     * @return 结果
     */
    public int insertOpNearInfraredSummary(OpNearInfraredSummary opNearInfraredSummary);

    /**
     * 修改近红外汇总
     * 
     * @param opNearInfraredSummary 近红外汇总
     * @return 结果
     */
    public int updateOpNearInfraredSummary(OpNearInfraredSummary opNearInfraredSummary);

    /**
     * 批量删除近红外汇总
     * 
     * @param dairylandIds 需要删除的近红外汇总主键集合
     * @return 结果
     */
    public int deleteOpNearInfraredSummaryByDairylandIds(String[] dairylandIds);

    /**
     * 删除近红外汇总信息
     * 
     * @param dairylandId 近红外汇总主键
     * @return 结果
     */
    public int deleteOpNearInfraredSummaryByDairylandId(String dairylandId);

     /**
     * 导入近红外汇总数据
     *
     * @param list 近红外汇总数据列表
     * @return 导入结果
     */
    int importNearInfraredSummary(List<OpNearInfraredSummary> list);

}
