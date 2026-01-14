package com.gmlimsqi.business.labtest.mapper;

import com.gmlimsqi.business.labtest.domain.OpFeedEntrustOrder;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * 饲料样品委托单Mapper接口
 * 
 * @author hhy
 * @date 2025-09-13
 */
public interface OpFeedEntrustOrderMapper 
{
    /**
     * 查询饲料样品委托单
     * 
     * @param opFeedEntrustOrderId 饲料样品委托单主键
     * @return 饲料样品委托单
     */
    public OpFeedEntrustOrder selectOpFeedEntrustOrderByOpFeedEntrustOrderId(String opFeedEntrustOrderId);

    //查询饲料样品委托单
    public OpFeedEntrustOrder selectOrderDetailById(String opFeedEntrustOrderId);
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
    public int insertOpFeedEntrustOrder(OpFeedEntrustOrder opFeedEntrustOrder);

    /**
     * 修改饲料样品委托单
     * 
     * @param opFeedEntrustOrder 饲料样品委托单
     * @return 结果
     */
    public int updateOpFeedEntrustOrder(OpFeedEntrustOrder opFeedEntrustOrder);


    public OpFeedEntrustOrder selectByNo(@Param("entrustOrderNo") String entrustOrderNo);

    public String selectNotEndCountById(@Param("opFeedEntrustOrderId")String opFeedEntrustOrderId);

    public int updateStatusById(@Param("status")String status, @Param("opFeedEntrustOrderId")String opFeedEntrustOrderId);

    OpFeedEntrustOrder selectPrintOrderDetailById(String opFeedEntrustOrderId);

    void updateExecutionPeriod(@Param("executionPeriod")Date executionPeriod, @Param("opFeedEntrustOrderId")String opFeedEntrustOrderId);
    // 添加这个方法
    int migrateResultToNewItem(@Param("sampleId") String sampleId,
                               @Param("dictItemId") String dictItemId,
                               @Param("newItemId") String newItemId);
}
