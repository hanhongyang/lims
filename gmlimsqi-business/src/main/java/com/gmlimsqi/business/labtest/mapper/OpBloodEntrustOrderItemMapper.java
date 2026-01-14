package com.gmlimsqi.business.labtest.mapper;

import com.gmlimsqi.business.labtest.domain.OpBloodEntrustOrderItem;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * 血样样品委托-项目对应Mapper接口
 *
 * @author hhy
 * @date 2025-09-20
 */
public interface OpBloodEntrustOrderItemMapper
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
     * 通过血样样品委托-项目对应主键更新删除标志
     *
     * @param opFeedEntrustOrderItemId 血样样品委托-项目对应ID
     * @return 结果
     */
    public int updateDeleteFlagById(String opFeedEntrustOrderItemId);

    /**
     * 批量通过血样样品委托-项目对应主键更新删除标志
     *
     * @param opFeedEntrustOrderItemId 血样样品委托-项目对应ID
     * @return 结果
     */
    public int updateDeleteFlagByIds(String[] opFeedEntrustOrderItemIds);

    /**
     * 删除血样样品委托-项目对应
     *
     * @param opFeedEntrustOrderItemId 血样样品委托-项目对应主键
     * @return 结果
     */
    public int deleteOpBloodEntrustOrderItemByOpFeedEntrustOrderItemId(String opFeedEntrustOrderItemId);

    /**
     * 批量删除血样样品委托-项目对应
     *
     * @param opFeedEntrustOrderItemIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteOpBloodEntrustOrderItemByOpFeedEntrustOrderItemIds(String[] opFeedEntrustOrderItemIds);

    /**
     * 根据主表id查询
     *
     * @return
     */
    List<OpBloodEntrustOrderItem> selectAllByOpBloodEntrustOrderId(@Param("opBloodEntrustOrderId") String opBloodEntrustOrderId);

    void updateDeleteFlagByEntrustOrderId(@Param("id")String id, @Param("now") Date now, @Param("updateUserId") String updateUserId);


    List<String> selectIdByOrderId(@Param("opBloodEntrustOrderId")String opBloodEntrustOrderId);
}
