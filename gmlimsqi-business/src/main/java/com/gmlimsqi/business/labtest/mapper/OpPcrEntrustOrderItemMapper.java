package com.gmlimsqi.business.labtest.mapper;

import java.util.Date;
import java.util.List;

import com.gmlimsqi.business.labtest.domain.OpFeedEntrustOrderItem;
import com.gmlimsqi.business.labtest.domain.OpPcrEntrustOrderItem;
import org.apache.ibatis.annotations.Param;

/**
 * pcr样品委托-项目对应Mapper接口
 * 
 * @author hhy
 * @date 2025-09-17
 */
public interface OpPcrEntrustOrderItemMapper 
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

    public int updateOpPcrEntrustOrderItem(OpPcrEntrustOrderItem opPcrEntrustOrderItem);

    void insertBatch(List<OpPcrEntrustOrderItem> itemList);

    public void updateDeleteBySampleId(@Param("updateBy")String updateBy , @Param("pcrEntrustOrderSampleId")String pcrEntrustOrderSampleId);


    List<String> selectIdBySampleId(@Param("pcrEntrustOrderSampleId")String pcrEntrustOrderSampleId);

    void updateResultBySampleNo(OpPcrEntrustOrderItem item);

    List<OpPcrEntrustOrderItem> getBaseByEntrustOrderNo(@Param("pcrTaskItemType")String pcrTaskItemType, @Param("entrustOrderNo")String entrustOrderNo);

    int selectExaminCountByIdList(@Param("list")List<String> collect);
    /**
     * 根据项目ID查询所属的委托单ID
     */
    String selectEntrustOrderIdByItemId(String opPcrEntrustOrderItemId);

    /**
     * 批量更新项目审核信息（examine_user, examine_user_id, examine_time）
     */
    int batchUpdateExamineFields(@Param("itemIds") List<String> itemIds,
                                 @Param("examineUserId") String examineUserId,
                                 @Param("examineUser") String examineUser,
                                 @Param("examineTime") Date examineTime);

    /**
     * 统计某个委托单下未审核（examine_user_id 为空）的项目数量
     */
    int countUnexaminedItemsByOrderId(String opPcrEntrustOrderId);

    /**
     * 根据项目ID更新备注（用于审核异议）
     */
    int updateRemarkById(OpPcrEntrustOrderItem item);

    int countUntestedItemsBySampleNo(@Param("sampleNo") String s);

    void updateDeleteByOrderId(@Param("updateBy")String updateBy, @Param("opPcrEntrustOrderId")String opPcrEntrustOrderId);

    /**
     * 批量清除项目审核信息
     */
    int batchClearExamineFields(@Param("itemIds") List<String> itemIds,
                                @Param("updateUserId") String updateUserId,
                                @Param("updateTime") Date updateTime); // 添加updateTime参数



    List<OpPcrEntrustOrderItem> seletKbyBySampleId(@Param("opPcrEntrustOrderSampleId")String opPcrEntrustOrderSampleId);

    List<OpPcrEntrustOrderItem> getBaseByResultNo(@Param("resultNo")String resultNo);
}
