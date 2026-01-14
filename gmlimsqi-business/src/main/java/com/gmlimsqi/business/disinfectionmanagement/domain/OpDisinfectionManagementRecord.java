package com.gmlimsqi.business.disinfectionmanagement.domain;

import com.gmlimsqi.common.core.domain.BaseEntity;
import lombok.Data;
import com.gmlimsqi.common.annotation.Excel;

/**
 * 消毒管理记录对象 op_disinfection_management_record
 * 
 * @author hhy
 * @date 2025-11-06
 */
@Data
public class OpDisinfectionManagementRecord extends BaseEntity implements BaseEntity.CreateAware, BaseEntity.UpdateAware
{
    private static final long serialVersionUID = 1L;

    /** 消毒管理记录表主键 */
    private String disinfectionManagementRecordId;

    /** 消毒管理表主键 */
    @Excel(name = "消毒管理表主键")
    private String disinfectionManagementId;

    /** 消毒管理状态 */
    @Excel(name = "消毒管理状态")
    private String disinfectionManagementStatus;

    /** 消毒池名称 */
    @Excel(name = "消毒池名称")
    private String disinfectionTankName;

    /** 浓度 */
    @Excel(name = "浓度")
    private String concentration;

    /** 消毒时间 */
    @Excel(name = "消毒时间")
    private String disinfectionTime;

    /** 消毒方式 */
    @Excel(name = "消毒方式")
    private String disinfectionMethod;

    /** 消毒液 */
    @Excel(name = "消毒液")
    private String disinfectant;

    /** 部门id */
    @Excel(name = "部门id")
    private String deptId;

    /** 是否删除：0-否 1-是 */
    @Excel(name = "是否删除：0-否 1-是")
    private String isDelete;

     /** 部门名称 */
    @Excel(name = "部门名称")
    private String deptName;

}
