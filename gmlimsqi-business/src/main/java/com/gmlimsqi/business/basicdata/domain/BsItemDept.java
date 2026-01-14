package com.gmlimsqi.business.basicdata.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.gmlimsqi.common.annotation.Excel;
import com.gmlimsqi.common.core.domain.BaseEntity;

/**
 * 项目组织对应对象 bs_item_dept
 * 
 * @author hhy
 * @date 2025-09-17
 */
@Data
public class BsItemDept extends BaseEntity implements BaseEntity.CreateAware, BaseEntity.UpdateAware
{
    private static final long serialVersionUID = 1L;

    /** id */
    private String bsItemDeptId;

    /** 组织id */
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long deptId;

    @Excel(name = "组织机构")
    private String deptName;

    /** 检测项目id */
    @Excel(name = "检测项目id")
    private String itemId;

    /** 是否删除 */
    private String isDelete;

}
