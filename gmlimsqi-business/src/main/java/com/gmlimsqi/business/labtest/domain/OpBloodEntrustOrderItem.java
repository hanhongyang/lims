package com.gmlimsqi.business.labtest.domain;

import com.gmlimsqi.common.annotation.Excel;
import com.gmlimsqi.common.core.domain.BaseEntity;
import lombok.Data;

/**
 * 血样样品委托-项目对应对象 op_blood_entrust_order_item
 *
 * @author hhy
 * @date 2025-09-20
 */
@Data
public class OpBloodEntrustOrderItem extends BaseEntity implements BaseEntity.CreateAware, BaseEntity.UpdateAware
{
    private static final long serialVersionUID = 1L;

    /** id */
    private String opBloodEntrustOrderItemId;

    /** 样品委托单物料表id */
    @Excel(name = "样品委托单物料表id")
    private String opBloodEntrustOrderId;

    /**
     * 项目id
     */
    private String itemId;
    /** 项目编码 */
    @Excel(name = "项目编码")
    private String itemCode;

    /** 检测蹄疫 */
    @Excel(name = "检测蹄疫")
    private String itemTy;

    /** 免疫后的样品免疫时间 */
    private String immunityTime;

    /** 检测布病抗体 */
    @Excel(name = "检测布病抗体")
    private String itemBbkt;

    /** 是否删除 */
    @Excel(name = "是否删除")
    private String isDelete;

    /** 检测结果 */
    private String testResult;

    /** 检测人id */
    private String testUserId;
    /** 检测人 */
    private String testUser;
    //项目名称
    private String itemName;
    //血样检测项目类别
    private String bloodTaskItemType;

}
