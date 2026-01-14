package com.gmlimsqi.business.labtest.service;

import com.gmlimsqi.business.labtest.domain.OpOutentrustRegisterSample;

import java.util.List;

/**
 * 外部委托检测单样品子Service接口
 *
 * @author wgq
 * @date 2025-09-17
 */
public interface IOpOutentrustRegisterSampleService
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
     * 批量删除外部委托检测单样品子
     *
     * @param outentrustRegisterSampleIds 需要删除的外部委托检测单样品子主键集合
     * @return 结果
     */
    public int deleteOpOutentrustRegisterSampleByOutentrustRegisterSampleIds(String[] outentrustRegisterSampleIds);

    /**
     * 删除外部委托检测单样品子信息
     *
     * @param outentrustRegisterSampleId 外部委托检测单样品子主键
     * @return 结果
     */
    public int deleteOpOutentrustRegisterSampleByOutentrustRegisterSampleId(String outentrustRegisterSampleId);
}
