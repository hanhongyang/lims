package com.gmlimsqi.business.basicdata.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import com.gmlimsqi.common.annotation.Excel;
import com.gmlimsqi.common.core.domain.BaseEntity;

/**
 * 通讯方式对象 bs_contact
 *
 * @author wgq
 * @date 2025-09-15
 */
@Data
public class BsContact extends BaseEntity implements BaseEntity.CreateAware, BaseEntity.UpdateAware
{
    private static final long serialVersionUID = 1L;

    /** 通讯信息表id */
    private String id;

    /** 公司id */
    @Excel(name = "公司id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private String deptId;

    /** 是否删除（0否1是） */
    @Excel(name = "是否删除", readConverterExp = "0=否1是")
    private String isDelete;

    /** 是否启用（0否1是） */
    @Excel(name = "是否启用", readConverterExp = "0=否1是")
    private String isEnable;

    /** 公司名称 */
    @Excel(name = "公司名称")
    private String deptName;

    /** 邮箱 */
    @Excel(name = "邮箱")
    private String email;

}
