package com.gmlimsqi.business.labtest.mapper;

import com.gmlimsqi.business.labtest.domain.OpOutentrustRegisterSample;

import java.util.List;

/**
 * 外部委托检测单样品子Mapper接口
 *
 * @author wgq
 * @date 2025-09-17
 */
public interface OpOutentrustRegisterSampleMapper
{
    /**
     * 查询外部委托检测单样品子
     *
     * @param outentrustRegisterSampleId 外部委托检测单样品子主键
     * @return 外部委托检测单样品子
     */
    public OpOutentrustRegisterSample selectOpOutentrustRegisterSampleByOutentrustRegisterSampleId(String outentrustRegisterSampleId);

    /**
     * 查询外部委托检测单样品子列表
     *
     * @param opOutentrustRegisterSample 外部委托检测单样品子
     * @return 外部委托检测单样品子集合
     */
    public List<OpOutentrustRegisterSample> selectOpOutentrustRegisterSampleList(OpOutentrustRegisterSample opOutentrustRegisterSample);

    /**
     * 新增外部委托检测单样品子
     *
     * @param opOutentrustRegisterSample 外部委托检测单样品子
     * @return 结果
     */
    public int insertOpOutentrustRegisterSample(OpOutentrustRegisterSample opOutentrustRegisterSample);

    /**
     * 修改外部委托检测单样品子
     *
     * @param opOutentrustRegisterSample 外部委托检测单样品子
     * @return 结果
     */
    public int updateOpOutentrustRegisterSample(OpOutentrustRegisterSample opOutentrustRegisterSample);

    /**
     * 通过外部委托检测单样品子主键更新删除标志
     *
     * @param outentrustRegisterSampleId 外部委托检测单样品子ID
     * @return 结果
     */
    public int updateDeleteFlagById(String outentrustRegisterSampleId);

    /**
     * 批量通过外部委托检测单样品子主键更新删除标志
     *
     * @param outentrustRegisterSampleId 外部委托检测单样品子ID
     * @return 结果
     */
    public int updateDeleteFlagByIds(String[] outentrustRegisterSampleIds);

    /**
     * 删除外部委托检测单样品子
     *
     * @param outentrustRegisterSampleId 外部委托检测单样品子主键
     * @return 结果
     */
    public int deleteOpOutentrustRegisterSampleByOutentrustRegisterSampleId(String outentrustRegisterSampleId);

    /**
     * 批量删除外部委托检测单样品子
     *
     * @param outentrustRegisterSampleIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteOpOutentrustRegisterSampleByOutentrustRegisterSampleIds(String[] outentrustRegisterSampleIds);

    List<OpOutentrustRegisterSample> selectAllByOutentrustRegisterId(String id);
}
