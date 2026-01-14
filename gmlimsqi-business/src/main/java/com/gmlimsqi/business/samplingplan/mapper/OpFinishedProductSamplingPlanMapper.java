package com.gmlimsqi.business.samplingplan.mapper;


import com.gmlimsqi.business.ranch.domain.OpSamplingPlan;
import com.gmlimsqi.business.samplingplan.pojo.entity.OpFinishedProductSamplingPlan;
import com.gmlimsqi.business.samplingplan.pojo.entity.OpFinishedProductSamplingPlanDetail;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * 成品，库存，垫料取样计划Mapper接口
 *
 * @author hhy
 * @date 2025-11-24
 */
public interface OpFinishedProductSamplingPlanMapper
{
    /**
     * 查询成品，库存，垫料取样计划
     *
     * @param finishedProductSamplingPlanId 成品，库存，垫料取样计划主键
     * @return 成品，库存，垫料取样计划
     */
    public OpFinishedProductSamplingPlan selectOpFinishedProductSamplingPlanByFinishedProductSamplingPlanId(String finishedProductSamplingPlanId);

    /**
     * 根据主表id和物料编码查询数据
     */
    public OpFinishedProductSamplingPlanDetail selectOpFinishedProductSamplingPlanDetailByFinishedProductSamplingPlanIdAndMaterialCode
    (@Param("finishedProductSamplingPlanId") String finishedProductSamplingPlanId, @Param("materialCode") String materialCode);

    /**
     * 查询成品，库存，垫料取样计划列表
     *
     * @param opFinishedProductSamplingPlan 成品，库存，垫料取样计划
     * @return 成品，库存，垫料取样计划集合
     */
    public List<OpFinishedProductSamplingPlan> selectOpFinishedProductSamplingPlanList(OpFinishedProductSamplingPlan opFinishedProductSamplingPlan);

    /**
     * 新增成品，库存，垫料取样计划
     *
     * @param opFinishedProductSamplingPlan 成品，库存，垫料取样计划
     * @return 结果
     */
    public int insertOpFinishedProductSamplingPlan(OpFinishedProductSamplingPlan opFinishedProductSamplingPlan);

    /**
     * 修改成品，库存，垫料取样计划
     *
     * @param opFinishedProductSamplingPlan 成品，库存，垫料取样计划
     * @return 结果
     */
    public int updateOpFinishedProductSamplingPlan(OpFinishedProductSamplingPlan opFinishedProductSamplingPlan);

    /**
     * 批量新增成品，库存，垫料取样计划详情
     *
     * @param opFinishedProductSamplingPlanDetailList 成品，库存，垫料取样计划详情列表
     * @return 结果
     */
    public int batchOpFinishedProductSamplingPlanDetail(List<OpFinishedProductSamplingPlanDetail> opFinishedProductSamplingPlanDetailList);

    /**
     * 根据主表id删除子表
     */
     public int deleteOpFinishedProductSamplingPlanDetailByFinishedProductSamplingPlanId(String finishedProductSamplingPlanId);

    /**
     * 查询成品，库存，垫料取样计划详情列表
     *
     * @param detail 成品，库存，垫料取样计划详情
     * @return 成品，库存，垫料取样计划详情集合
     */
    List<OpFinishedProductSamplingPlanDetail> selectDetailList(OpFinishedProductSamplingPlanDetail detail);

    /**
     * 修改成品，库存，垫料取样计划详情
     *
     * @param detail 成品，库存，垫料取样计划详情
     * @return 结果
     */
    int updateOpFinishedProductSamplingPlanDetail(OpFinishedProductSamplingPlanDetail detail);

    /**
     * 查询成品，库存，垫料取样计划子表详情
     *
     * @param finishedProductSamplingPlanDetailId 成品，库存，垫料取样计划详情主键
     * @return 成品，库存，垫料取样计划详情
     */
    OpFinishedProductSamplingPlanDetail selectOpFinishedProductSamplingPlanDetailByFinishedProductSamplingPlanDetailId(String finishedProductSamplingPlanDetailId);

    /**
     * 根据主表id查询子表数据
     * @param finishedProductSamplingPlanId
     * @return
     */
    List<OpFinishedProductSamplingPlanDetail> selectPlanDetailListBySamplingPlanId(String finishedProductSamplingPlanId);

    OpFinishedProductSamplingPlan selectOpSamplingPlanByOpSamplingPlanId(String finishedProductSamplingPlanId);
}
