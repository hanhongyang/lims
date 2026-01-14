package com.gmlimsqi.business.ranch.mapper;


import com.gmlimsqi.business.ranch.domain.OpSamplingPlan;
import com.gmlimsqi.business.ranch.domain.OpSamplingPlanSample;
import com.gmlimsqi.business.ranch.vo.SamplingReceiveListVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 取样计划主Mapper接口
 *
 * @author hhy
 * @date 2025-11-04
 */
public interface OpSamplingPlanMapper
{
    /**
     * 查询取样计划主
     *
     * @param opSamplingPlanId 取样计划主主键
     * @return 取样计划主
     */
    public OpSamplingPlan selectOpSamplingPlanByOpSamplingPlanId(String opSamplingPlanId);

    /**
     * 查询取样计划主列表
     *
     * @param opSamplingPlan 取样计划主
     * @return 取样计划主集合
     */
    public List<OpSamplingPlan> selectOpSamplingPlanList(OpSamplingPlan opSamplingPlan);

    /**
     * 新增取样计划主
     *
     * @param opSamplingPlan 取样计划主
     * @return 结果
     */
    public int insertOpSamplingPlan(OpSamplingPlan opSamplingPlan);

    /**
     * 修改取样计划主
     *
     * @param opSamplingPlan 取样计划主
     * @return 结果
     */
    public int updateOpSamplingPlan(OpSamplingPlan opSamplingPlan);

    /**
     * 通过取样计划主主键更新删除标志
     *
     * @param opSamplingPlanId 取样计划主ID
     * @return 结果
     */
    public int updateDeleteFlagById(String opSamplingPlanId);

    /**
     * 批量通过取样计划主主键更新删除标志
     *
     * @param opSamplingPlanIds 取样计划主ID
     * @return 结果
     */
    public int updateDeleteFlagByIds(String[] opSamplingPlanIds);

    /**
     * 删除取样计划主
     *
     * @param opSamplingPlanId 取样计划主主键
     * @return 结果
     */
    public int deleteOpSamplingPlanByOpSamplingPlanId(String opSamplingPlanId);

    /**
     * 批量删除取样计划主
     *
     * @param opSamplingPlanIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteOpSamplingPlanByOpSamplingPlanIds(String[] opSamplingPlanIds);

    /**
     * 根据计划id删除样品子表
     * @param updateUserId
     * @param opSamplingPlanId
     */
    void updateSampleDeleteFlagByPlanId(@Param("updateUserId") String updateUserId, @Param("opSamplingPlanId") String opSamplingPlanId);

    /**
     * 根据计划id删除检测项目子表
     * @param updateUserId
     * @param opSamplingPlanId
     */
    void updateItemDeleteFlagByPlanId(@Param("updateUserId") String updateUserId, @Param("opSamplingPlanId") String opSamplingPlanId);

    List<SamplingReceiveListVo> selectOpSamplingPlanReceiveList(OpSamplingPlan opSamplingPlan);

    List<OpSamplingPlan> selectBySignInId(@Param("signInId")String signInId);

    List<SamplingReceiveListVo> selectOpSamplingPlanReceiveList2(OpSamplingPlan opSamplingPlan);

    SamplingReceiveListVo selectopSamplingPlanSampleInfoById(String opSamplingPlanSampleId);
}
