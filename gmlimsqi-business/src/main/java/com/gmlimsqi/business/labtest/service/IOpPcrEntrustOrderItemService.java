package com.gmlimsqi.business.labtest.service;

import java.util.List;

import com.gmlimsqi.business.labtest.domain.OpJczxPcrResultInfo;
import com.gmlimsqi.business.labtest.domain.OpPcrEntrustOrderItem;

/**
 * pcr样品委托-项目对应Service接口
 * 
 * @author hhy
 * @date 2025-09-17
 */
public interface IOpPcrEntrustOrderItemService 
{
    /**
     * 查询pcr样品委托-项目对应
     * 
     * @param opPcrEntrustOrderItemId pcr样品委托-项目对应主键
     * @return pcr样品委托-项目对应
     */
    public OpPcrEntrustOrderItem selectOpPcrEntrustOrderItemByOpPcrEntrustOrderItemId(String opPcrEntrustOrderItemId);

    /**
     * 查询pcr样品委托-项目对应列表
     * 
     * @param opPcrEntrustOrderItem pcr样品委托-项目对应
     * @return pcr样品委托-项目对应集合
     */
    public List<OpPcrEntrustOrderItem> selectOpPcrEntrustOrderItemList(OpPcrEntrustOrderItem opPcrEntrustOrderItem);

    /**
     * 新增pcr样品委托-项目对应
     * 
     * @param opPcrEntrustOrderItem pcr样品委托-项目对应
     * @return 结果
     */
    public int insertOpPcrEntrustOrderItem(OpPcrEntrustOrderItem opPcrEntrustOrderItem);

    public List<OpPcrEntrustOrderItem> getBaseByEntrustOrderNo(String pcrTaskItemType, String entrustOrderNo);

    List<OpPcrEntrustOrderItem> getBaseByResultNo(String resultNo);
}
