package com.gmlimsqi.business.labtest.domain;

import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.gmlimsqi.common.annotation.Excel;
import com.gmlimsqi.common.core.domain.BaseEntity;

/**
 * 血样委托项目配置对象 op_blood_entrust_item_config
 * 
 * @author hhy
 * @date 2025-09-23
 */
@Data
public class OpBloodEntrustItemConfig extends BaseEntity implements BaseEntity.CreateAware, BaseEntity.UpdateAware
{
    private static final long serialVersionUID = 1L;

    /** id */
    private String opBloodEntrustItemConfigId;

    /** 血样项目编码 */
    @Excel(name = "血样项目编码")
    private String itemCode;

    /** 项目表id */
    @Excel(name = "项目表id")
    private String itemId;

    /** 是否删除 */
    private String isDelete;

    /** 报告模板编码 */
    @Excel(name = "报告模板编码")
    private String reportModelCode;

}
