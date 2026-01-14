package com.gmlimsqi.business.samplingplan.service;

import com.gmlimsqi.business.ranch.domain.OpSamplingPlanSample;
import com.gmlimsqi.business.samplingplan.pojo.entity.OpFinishedProductSamplingPlan;
import com.gmlimsqi.business.samplingplan.pojo.entity.OpFinishedProductSamplingPlanDetail;
import com.gmlimsqi.business.samplingplan.pojo.vo.OpSamplingPlanSampleInfoVO;
import com.gmlimsqi.business.samplingplan.pojo.vo.OpSamplingPlanSampleVO;

import java.util.List;

/**
 * 成品，库存，垫料取样计划Service接口
 *
 * @author hhy
 * @date 2025-11-24
 */
public interface IOpFinishedProductSamplingPlanService
{
    /**
     * 查询成品，库存，垫料取样计划
     *
     * @param finishedProductSamplingPlanId 成品，库存，垫料取样计划主键
     * @return 成品，库存，垫料取样计划
     */
    public OpFinishedProductSamplingPlan selectOpFinishedProductSamplingPlanByFinishedProductSamplingPlanId(String finishedProductSamplingPlanId);

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
     * 批量新增成品，库存，垫料取样计划
     *
     * @param planList 成品，库存，垫料取样计划列表
     */
    void batchSaveMainAndSub(List<OpFinishedProductSamplingPlan> planList);

    /**
     * 审核取样计划
     *
     * @param finishedProductSamplingPlanId 成品，库存，垫料取样计划主键
     */
    void audit(String finishedProductSamplingPlanId);

     /**
     * 查询成品，库存，垫料取样计划详情列表
     *
     * @param finishedProductSamplingPlanId 成品，库存，垫料取样计划主键
     * @return 成品，库存，垫料取样计划详情集合
     */
    List<OpFinishedProductSamplingPlanDetail> selectOpFinishedProductSamplingPlanDetailList(OpFinishedProductSamplingPlanDetail detail);

    /**
     * 查询取样单列表
     *
     * @param sample 取样单
     * @return 取样单集合
     */
    List<OpSamplingPlanSampleVO> selectOpSamplingPlanSampleList(OpSamplingPlanSample sample);

    /**
     * 修改成品，库存，垫料取样计划子表数据
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
     * 新增取样单
     * @param sample
     * @return
     */
    int addOpSamplingPlanSample(OpSamplingPlanSample sample);

    /**
     * 修改取样单
     * @param sample
     * @return
     */
    int updateOpSamplingPlanSample(OpSamplingPlanSample sample);

     /**
     * 查询取样单详情
     * @param opSamplingPlanSampleId
     * @return
     */
     OpSamplingPlanSample selectOpSamplingPlanSampleDetail(String opSamplingPlanSampleId);

    /**
     * 库存和成品取样单作废
     * @param finishedProductSamplingPlanId
     * @return
     * */
    int cancelOpSamplingPlan(String finishedProductSamplingPlanId);
}
