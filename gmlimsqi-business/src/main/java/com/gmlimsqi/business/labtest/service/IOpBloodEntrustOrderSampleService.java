package com.gmlimsqi.business.labtest.service;

import com.gmlimsqi.business.labtest.domain.OpBloodEntrustOrderSample;

import java.util.List;

/**
 * 血样样品委托-样品对应Service接口
 * 
 * @author hhy
 * @date 2025-09-20
 */
public interface IOpBloodEntrustOrderSampleService 
{
    /**
     * 查询血样样品委托-样品对应
     * 
     * @param opBloodEntrustOrderId 血样样品委托-样品对应主键
     * @return 血样样品委托-样品对应
     */
    public OpBloodEntrustOrderSample selectOpBloodEntrustOrderSampleByOpBloodEntrustOrderId(String opBloodEntrustOrderId);

    /**
     * 查询血样样品委托-样品对应列表
     * 
     * @param opBloodEntrustOrderSample 血样样品委托-样品对应
     * @return 血样样品委托-样品对应集合
     */
    public List<OpBloodEntrustOrderSample> selectOpBloodEntrustOrderSampleList(OpBloodEntrustOrderSample opBloodEntrustOrderSample);

    /**
     * 新增血样样品委托-样品对应
     * 
     * @param opBloodEntrustOrderSample 血样样品委托-样品对应
     * @return 结果
     */
    public int insertOpBloodEntrustOrderSample(OpBloodEntrustOrderSample opBloodEntrustOrderSample);

    /**
     * 修改血样样品委托-样品对应
     * 
     * @param opBloodEntrustOrderSample 血样样品委托-样品对应
     * @return 结果
     */
    public int updateOpBloodEntrustOrderSample(OpBloodEntrustOrderSample opBloodEntrustOrderSample);

    List<OpBloodEntrustOrderSample> getBaseByResultNo(String resultNo);
}
