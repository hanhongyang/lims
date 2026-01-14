package com.gmlimsqi.business.labtest.service;

import com.gmlimsqi.business.labtest.domain.OpFeedEntrustOrder;
import com.gmlimsqi.business.labtest.domain.OpFeedEntrustOrderSample;

import java.util.List;

/**
 * 饲料样品委托单Service接口
 * 
 * @author hhy
 * @date 2025-09-13
 */
public interface IOpFeedEntrustOrderService 
{
    /**
     * 查询饲料样品委托单
     * 
     * @param opFeedEntrustOrderId 饲料样品委托单主键
     * @return 饲料样品委托单
     */
    public OpFeedEntrustOrder selectOpFeedEntrustOrderByOpFeedEntrustOrderId(String opFeedEntrustOrderId);

    /**
     * 查询饲料样品委托单列表
     * 
     * @param opFeedEntrustOrder 饲料样品委托单
     * @return 饲料样品委托单集合
     */
    public List<OpFeedEntrustOrder> selectOpFeedEntrustOrderList(OpFeedEntrustOrder opFeedEntrustOrder);

    /**
     * 新增饲料样品委托单
     * 
     * @param opFeedEntrustOrder 饲料样品委托单
     * @return 结果
     */
    public int insertOpFeedEntrustOrder(OpFeedEntrustOrder opFeedEntrustOrder) ;

    /**
     * 修改饲料样品委托单
     * 
     * @param opFeedEntrustOrder 饲料样品委托单
     * @return 结果
     */
    public int updateOpFeedEntrustOrder(OpFeedEntrustOrder opFeedEntrustOrder);

    /**
     * 修改饲料样品委托单
     *
     * @param opFeedEntrustOrder 饲料样品委托单
     * @return 结果
     */
    public int updateOpFeedEntrustOrder3(OpFeedEntrustOrder opFeedEntrustOrder);
    /**
     * 获取饲料样品委托单打印（化学法）详细信息
     */
    OpFeedEntrustOrder selectPrintOpFeedEntrustOrderByOpFeedEntrustOrderId(String opFeedEntrustOrderId);

    List<OpFeedEntrustOrderSample> selectJhwItemList(List<String> sampleIdList);
}
