package com.gmlimsqi.business.labtest.mapper;

import java.util.List;

import com.gmlimsqi.business.labtest.domain.OpFeedEntrustOrder;
import com.gmlimsqi.business.labtest.domain.OpFeedEntrustOrderSample;
import org.apache.ibatis.annotations.Param;

/**
 * 饲料样品委托单-样品Mapper接口
 * 
 * @author hhy
 * @date 2025-09-15
 */
public interface OpFeedEntrustOrderSampleMapper 
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

    public List<OpFeedEntrustOrderSample> selectDeletedSamplesByOrderId(@Param("orderId") String opFeedEntrustOrderId);

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


    void updateDeleteByOrderId(@Param("updateBy") String updateBy, @Param("feedEntrustOrderId")String feedEntrustOrderId);


    void updateDeleteById(@Param("updateBy") String updateBy, @Param("opFeedEntrustOrderSampleId")String opFeedEntrustOrderSampleId);


    void updateReceiveById(@Param("receiverId") String receiverId, @Param("opFeedEntrustOrderSampleId")String opFeedEntrustOrderSampleId);

    String selectOrderIdById(@Param("opFeedEntrustOrderSampleId")String opFeedEntrustOrderSampleId);
    /**
     * 根据委托单ID列表查询样品列表
     *
     * @param orderIds 委托单ID列表
     * @return 样品列表
     */
    List<OpFeedEntrustOrderSample> selectSamplesByOrderIds(@Param("list") List<String> orderIds);

    /**
     * 根据样品ID列表查询计划外项目列表
     * 返回 OpFeedEntrustOrderSample 列表，包含 testItem 集合
     * @param sampleIdList 样品ID列表
     * @return OpFeedEntrustOrderSample 列表
     */
    List<OpFeedEntrustOrderSample> selectJhwItemList(List<String> sampleIdList);
    /**
     * 获取下一个计划网编号序列
     * @return 6位序列号
     */
    public int selectNextJhwNo();

    void updateRetestById(@Param("entrustOrderSampleId") String entrustOrderSampleId);

    void updateReturnReason(@Param("returnReason")String returnReason, @Param("sampleNo")String sampleNo);

    void updateDeleteByNo(@Param("updateBy") String updateBy,@Param("sampleNo")String sampleNo);

    OpFeedEntrustOrderSample selectBySampleNo(@Param("sampleNo")String sampleNo);
}
