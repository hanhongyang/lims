package com.gmlimsqi.business.labtest.service;

import com.gmlimsqi.business.labtest.domain.OpBloodEntrustOrderItem;

import java.util.List;


/**
 * 血样样品委托-项目对应Service接口
 *
 * @author hhy
 * @date 2025-09-20
 */
public interface IOpBloodEntrustOrderItemService
{
    /**
     * 查询血样样品委托-项目对应
     *
     * @param opFeedEntrustOrderItemId 血样样品委托-项目对应主键
     * @return 血样样品委托-项目对应
     */
    public OpBloodEntrustOrderItem selectOpBloodEntrustOrderItemByOpFeedEntrustOrderItemId(String opFeedEntrustOrderItemId);

    /**
     * 查询血样样品委托-项目对应列表
     *
     * @param opBloodEntrustOrderItem 血样样品委托-项目对应
     * @return 血样样品委托-项目对应集合
     */
    public List<OpBloodEntrustOrderItem> selectOpBloodEntrustOrderItemList(OpBloodEntrustOrderItem opBloodEntrustOrderItem);

    /**
     * 新增血样样品委托-项目对应
     *
     * @param opBloodEntrustOrderItem 血样样品委托-项目对应
     * @return 结果
     */
    public int insertOpBloodEntrustOrderItem(OpBloodEntrustOrderItem opBloodEntrustOrderItem);

    /**
     * 修改血样样品委托-项目对应
     *
     * @param opBloodEntrustOrderItem 血样样品委托-项目对应
     * @return 结果
     */
    public int updateOpBloodEntrustOrderItem(OpBloodEntrustOrderItem opBloodEntrustOrderItem);

    /**
     * 批量删除血样样品委托-项目对应
     *
     * @param opFeedEntrustOrderItemIds 需要删除的血样样品委托-项目对应主键集合
     * @return 结果
     */
    public int deleteOpBloodEntrustOrderItemByOpFeedEntrustOrderItemIds(String[] opFeedEntrustOrderItemIds);

    /**
     * 删除血样样品委托-项目对应信息
     *
     * @param opFeedEntrustOrderItemId 血样样品委托-项目对应主键
     * @return 结果
     */
    public int deleteOpBloodEntrustOrderItemByOpFeedEntrustOrderItemId(String opFeedEntrustOrderItemId);
}
