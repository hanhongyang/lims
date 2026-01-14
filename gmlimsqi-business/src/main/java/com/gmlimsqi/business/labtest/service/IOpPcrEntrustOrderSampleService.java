package com.gmlimsqi.business.labtest.service;

import java.util.List;
import com.gmlimsqi.business.labtest.domain.OpPcrEntrustOrderSample;

/**
 * pcr样品委托单-样品Service接口
 * 
 * @author hhy
 * @date 2025-09-17
 */
public interface IOpPcrEntrustOrderSampleService 
{
    /**
     * 查询pcr样品委托单-样品
     * 
     * @param opPcrEntrustOrderSampleId pcr样品委托单-样品主键
     * @return pcr样品委托单-样品
     */
    public OpPcrEntrustOrderSample selectOpPcrEntrustOrderSampleByOpPcrEntrustOrderSampleId(String opPcrEntrustOrderSampleId);

    /**
     * 查询pcr样品委托单-样品列表
     * 
     * @param opPcrEntrustOrderSample pcr样品委托单-样品
     * @return pcr样品委托单-样品集合
     */
    public List<OpPcrEntrustOrderSample> selectOpPcrEntrustOrderSampleList(OpPcrEntrustOrderSample opPcrEntrustOrderSample);

    /**
     * 新增pcr样品委托单-样品
     * 
     * @param opPcrEntrustOrderSample pcr样品委托单-样品
     * @return 结果
     */
    public int insertOpPcrEntrustOrderSample(OpPcrEntrustOrderSample opPcrEntrustOrderSample);


}
