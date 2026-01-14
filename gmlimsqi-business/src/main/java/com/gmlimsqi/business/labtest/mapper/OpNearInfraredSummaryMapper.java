package com.gmlimsqi.business.labtest.mapper;

import com.gmlimsqi.business.labtest.domain.OpNearInfraredSummary;

import java.util.List;

/**
 * 近红外汇总Mapper接口
 * 
 * @author hhy
 * @date 2025-10-29
 */
public interface OpNearInfraredSummaryMapper 
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
     * 通过近红外汇总主键更新删除标志
     *
     * @param dairylandId 近红外汇总ID
     * @return 结果
     */
    public int updateDeleteFlagById(String dairylandId);

    /**
     * 批量通过近红外汇总主键更新删除标志
     *
     * @param dairylandId 近红外汇总ID
     * @return 结果
     */
    public int updateDeleteFlagByIds(String[] dairylandIds);

    /**
     * 删除近红外汇总
     * 
     * @param dairylandId 近红外汇总主键
     * @return 结果
     */
    public int deleteOpNearInfraredSummaryByDairylandId(String dairylandId);

    /**
     * 批量删除近红外汇总
     * 
     * @param dairylandIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteOpNearInfraredSummaryByDairylandIds(String[] dairylandIds);
}
