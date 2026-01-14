package com.gmlimsqi.business.labtest.service;

import java.util.List;
import com.gmlimsqi.business.labtest.domain.OpFeedEntrustOrderItem;

/**
 * 饲料样品委托-项目对应Service接口
 * 
 * @author hhy
 * @date 2025-09-17
 */
public interface IOpFeedEntrustOrderItemService 
{
    /**
     * 查询饲料样品委托-项目对应
     * 
     * @param opFeedEntrustOrderItemId 饲料样品委托-项目对应主键
     * @return 饲料样品委托-项目对应
     */
    public OpFeedEntrustOrderItem selectOpFeedEntrustOrderItemByOpFeedEntrustOrderItemId(String opFeedEntrustOrderItemId);

    /**
     * 查询饲料样品委托-项目对应列表
     * 
     * @param opFeedEntrustOrderItem 饲料样品委托-项目对应
     * @return 饲料样品委托-项目对应集合
     */
    public List<OpFeedEntrustOrderItem> selectOpFeedEntrustOrderItemList(OpFeedEntrustOrderItem opFeedEntrustOrderItem);

    /**
     * 新增饲料样品委托-项目对应
     * 
     * @param opFeedEntrustOrderItem 饲料样品委托-项目对应
     * @return 结果
     */
    public int insertOpFeedEntrustOrderItem(OpFeedEntrustOrderItem opFeedEntrustOrderItem);

    /**
     * 修改饲料样品委托-项目对应
     * 
     * @param opFeedEntrustOrderItem 饲料样品委托-项目对应
     * @return 结果
     */
    public int updateOpFeedEntrustOrderItem(OpFeedEntrustOrderItem opFeedEntrustOrderItem);


}
