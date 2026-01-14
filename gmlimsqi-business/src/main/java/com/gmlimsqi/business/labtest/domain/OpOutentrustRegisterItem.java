package com.gmlimsqi.business.labtest.domain;

import com.gmlimsqi.common.annotation.Excel;
import com.gmlimsqi.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 外部委托检测单化验项目子对象 op_outentrust_register_item
 *
 * @author wgq
 * @date 2025-09-17
 */
@Data
@Accessors(chain=true)
public class OpOutentrustRegisterItem extends BaseEntity implements BaseEntity.CreateAware, BaseEntity.UpdateAware
{
    private static final long serialVersionUID = 1L;

    /** 主表id */
    private String outentrustRegisterId;

    /** 样品子表id */
    @Excel(name = "样品子表id")
    private String outentrustRegisterItemId;

    /** 项目子表id */
    @Excel(name = "项目子表id")
    private String outentrustRegisterSampleId;

    /** 是否删除 */
    @Excel(name = "是否删除")
    private String isDelete;

    /** 样品编号 */
    @Excel(name = "样品编号")
    private String sampleNo;

    /** 样品名称 */
    @Excel(name = "样品名称")
    private String sampleName;

    /** 化验项目 */
    @Excel(name = "化验项目")
    private String itemName;

    /** 化验项目id */
    @Excel(name = "化验项目id")
    private String itemsId;

}
