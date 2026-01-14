package com.gmlimsqi.business.labtest.mapper;

import java.util.List;
import com.gmlimsqi.business.labtest.domain.OpFeedEntrustOrderItem;
import org.apache.ibatis.annotations.Param;

/**
 * 饲料样品委托-项目对应Mapper接口
 * 
 * @author hhy
 * @date 2025-09-17
 */
public interface OpFeedEntrustOrderItemMapper 
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


    public void insertBatch(List<OpFeedEntrustOrderItem> itemList);


    public void updateDeleteBySampleId(@Param("updateBy")String updateBy , @Param("opFeedEntrustOrderSampleId")String opFeedEntrustOrderSampleId);

    List<String> selectIdBySampleId(@Param("opFeedEntrustOrderSampleId")String opFeedEntrustOrderSampleId);

    void updateDeleteByOrderId(@Param("updateBy")String updateBy , @Param("opFeedEntrustOrderId") String opFeedEntrustOrderId);

    void updateTestToNullById(@Param("entrustOrderItemId")String entrustOrderItemId, @Param("updateBy")String updateUser);

    void updateDeleteStatusBySampleId(@Param("sampleId")String sampleId,@Param("status") String status, @Param("updateBy")String updateBy);

    List<OpFeedEntrustOrderItem> selectItemsBySampleIdIncludeDeleted(@Param("sampleId")String opFeedEntrustOrderSampleId);
}
