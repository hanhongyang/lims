package com.gmlimsqi.business.ranch.service;


import com.gmlimsqi.business.ranch.domain.OpSamplingPlanSample;

import java.util.List;

/**
 * 取样计划-样品Service接口
 * 
 * @author hhy
 * @date 2025-11-04
 */
public interface IOpSamplingPlanSampleService 
{
    /**
     * 查询取样计划-样品
     * 
     * @param opSamplingPlanSampleId 取样计划-样品主键
     * @return 取样计划-样品
     */
    public OpSamplingPlanSample selectOpSamplingPlanSampleByOpSamplingPlanSampleId(String opSamplingPlanSampleId);

    /**
     * 查询取样计划-样品列表
     * 
     * @param opSamplingPlanSample 取样计划-样品
     * @return 取样计划-样品集合
     */
    public List<OpSamplingPlanSample> selectOpSamplingPlanSampleList(OpSamplingPlanSample opSamplingPlanSample);

    /**
     * 新增取样计划-样品
     * 
     * @param opSamplingPlanSample 取样计划-样品
     * @return 结果
     */
    public int insertOpSamplingPlanSample(OpSamplingPlanSample opSamplingPlanSample);

    /**
     * 修改取样计划-样品
     * 
     * @param opSamplingPlanSample 取样计划-样品
     * @return 结果
     */
    public int updateOpSamplingPlanSample(OpSamplingPlanSample opSamplingPlanSample);

    /**
     * 批量删除取样计划-样品
     * 
     * @param opSamplingPlanSampleIds 需要删除的取样计划-样品主键集合
     * @return 结果
     */
    public int deleteOpSamplingPlanSampleByOpSamplingPlanSampleIds(String[] opSamplingPlanSampleIds);

    /**
     * 删除取样计划-样品信息
     * 
     * @param opSamplingPlanSampleId 取样计划-样品主键
     * @return 结果
     */
    public int deleteOpSamplingPlanSampleByOpSamplingPlanSampleId(String opSamplingPlanSampleId);
}
