package com.gmlimsqi.business.ranch.mapper;

import com.gmlimsqi.business.ranch.domain.OpSamplingPlanSample;
import com.gmlimsqi.business.ranch.dto.OpSamplingPlanSamplePushSapDTO;
import com.gmlimsqi.business.ranch.vo.OpSamplingPlanSampleMonitorVO;
import com.gmlimsqi.business.ranch.vo.SampleInfoVO;
import com.gmlimsqi.business.samplingplan.pojo.vo.OpSamplingPlanSampleVO;
import io.lettuce.core.dynamic.annotation.Param;

import java.util.List;

/**
 * 取样计划-样品Mapper接口
 *
 * @author hhy
 * @date 2025-11-04
 */
public interface OpSamplingPlanSampleMapper
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
     * 查询取样计划-样品列表
     *
     * @param opSamplingPlanSample 取样计划-样品
     * @return 取样计划-样品集合
     */
    public List<OpSamplingPlanSampleVO> selectOpSamplingPlanSampleListLinkOFPSP(OpSamplingPlanSample opSamplingPlanSample);

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
     * 修改取样计划-样品
     *
     * @param opSamplingPlanSample 取样计划-样品
     * @return 结果
     */
    public int updateOpSamplingPlanSampleDTO(OpSamplingPlanSamplePushSapDTO opSamplingPlanSample);

    /**
     * 通过取样计划-样品主键更新删除标志
     *
     * @param opSamplingPlanSampleId 取样计划-样品ID
     * @return 结果
     */
    public int updateDeleteFlagById(String opSamplingPlanSampleId);

    /**
     * 批量通过取样计划-样品主键更新删除标志
     *
     * @param opSamplingPlanSampleId 取样计划-样品ID
     * @return 结果
     */
    public int updateDeleteFlagByIds(String[] opSamplingPlanSampleIds);

    /**
     * 删除取样计划-样品
     *
     * @param opSamplingPlanSampleId 取样计划-样品主键
     * @return 结果
     */
    public int deleteOpSamplingPlanSampleByOpSamplingPlanSampleId(String opSamplingPlanSampleId);

    /**
     * 批量删除取样计划-样品
     *
     * @param opSamplingPlanSampleIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteOpSamplingPlanSampleByOpSamplingPlanSampleIds(String[] opSamplingPlanSampleIds);
    /**
     * 批量新增取样计划-样品
     *
     * @param list 样品列表
     * @return 插入的行数
     */
    int batchInsertOpSamplingPlanSample(@Param("list") List<OpSamplingPlanSample> list);

    /**
     * 根据计划id查询取样计划-样品列表
     *
     * @param samplingPlanId 取样计划-样品
     * @return 取样计划-样品集合
     */
    public List<OpSamplingPlanSample> selectOpSamplingPlanSampleListByPlanId(String samplingPlanId);

    /**
     * (新) 根据主键ID列表查询
     */
    public List<OpSamplingPlanSample> selectOpSamplingPlanSampleListByIds( List<String> ids);

    /**
     * (新) 根据 *计划ID* 列表查询样品
     */
    public List<OpSamplingPlanSample> selectOpSamplingPlanSampleListByPlanIdList( List<String> planIds);

    /**
     * 查询未推送SAP的化验结果信息列表
     */
    public List<OpSamplingPlanSample> selectUnPushSapList(OpSamplingPlanSample opSamplingPlanSample);

    /**
     * 根据主键ID查询样品
     * @param opSamplingPlanSampleId
     * @return
     */
    public SampleInfoVO selectOpSamplingPlanSampleId(String opSamplingPlanSampleId);

     /**
     * 查询取样计划-样品详情
     * @param opSamplingPlanSampleId
     * @return
     */
     OpSamplingPlanSample selectOpSamplingPlanSampleDetail(String opSamplingPlanSampleId);

    /**
     * 查询待销毁列表
     */
    List<OpSamplingPlanSample> selectDestroyList(OpSamplingPlanSample opSamplingPlanSample);

    int checkSampleNoUnique(String sampleNo);

    OpSamplingPlanSample selectOpSamplingBySampleNo(String sampleNo);

    /**
     * 平铺查询样品及其对应的统计信息
     */
    List<OpSamplingPlanSampleMonitorVO> selectSampleMonitorList(OpSamplingPlanSample opSamplingPlanSample);
}
