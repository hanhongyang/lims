package com.gmlimsqi.business.labtest.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import com.gmlimsqi.common.annotation.Excel;
import com.gmlimsqi.common.core.domain.BaseEntity;

import java.util.Date;

/**
 * 饲料样品委托-项目对应对象 op_feed_entrust_order_item
 * 
 * @author hhy
 * @date 2025-09-17
 */
@Data
public class OpFeedEntrustOrderItem extends BaseEntity implements BaseEntity.CreateAware, BaseEntity.UpdateAware
{
    private static final long serialVersionUID = 1L;

    /** id */
    private String opFeedEntrustOrderItemId;

    /** 样品委托单主表id */
    @Excel(name = "样品委托单物料表id")
    private String opFeedEntrustOrderSampleId;

    //血样项目字典值
    @Excel(name = "项目编码")
    private String itemCode;

    @Excel(name = "项目")
    private String itemName;
    /** 项目id */
    private String itemId;

    /** 是否删除 */
    private String isDelete;

    /** 检测结果 */
    private String testResult;

    /** 检测人id */
    private String testUserId;
    /** 检测人 */
    private String testUser;
}
