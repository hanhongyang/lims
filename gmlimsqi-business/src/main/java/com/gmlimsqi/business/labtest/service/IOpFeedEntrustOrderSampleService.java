package com.gmlimsqi.business.labtest.service;

import java.util.List;
import com.gmlimsqi.business.labtest.domain.OpFeedEntrustOrderSample;

/**
 * 饲料样品委托单-样品Service接口
 * 
 * @author hhy
 * @date 2025-09-15
 */
public interface IOpFeedEntrustOrderSampleService 
{
    /**
     * 查询饲料样品委托单-样品
     * 
     * @param opFeedEntrustOrderSampleId 饲料样品委托单-样品主键
     * @return 饲料样品委托单-样品
     */
    public OpFeedEntrustOrderSample selectOpFeedEntrustOrderSampleByOpFeedEntrustOrderSampleId(String opFeedEntrustOrderSampleId);

    /**
     * 查询饲料样品委托单-样品列表
     * 
     * @param opFeedEntrustOrderSample 饲料样品委托单-样品
     * @return 饲料样品委托单-样品集合
     */
    public List<OpFeedEntrustOrderSample> selectOpFeedEntrustOrderSampleList(OpFeedEntrustOrderSample opFeedEntrustOrderSample);

    /**
     * 新增饲料样品委托单-样品
     * 
     * @param opFeedEntrustOrderSample 饲料样品委托单-样品
     * @return 结果
     */
    public int insertOpFeedEntrustOrderSample(OpFeedEntrustOrderSample opFeedEntrustOrderSample);

    /**
     * 修改饲料样品委托单-样品
     * 
     * @param opFeedEntrustOrderSample 饲料样品委托单-样品
     * @return 结果
     */
    public int updateOpFeedEntrustOrderSample(OpFeedEntrustOrderSample opFeedEntrustOrderSample);


    int uploadNirReport(OpFeedEntrustOrderSample feedEntrustOrderSample);
}
