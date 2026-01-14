package com.gmlimsqi.business.disinfectionmanagement.domain;

import java.util.List;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.gmlimsqi.common.annotation.Excel;
import com.gmlimsqi.common.core.domain.BaseEntity;

/**
 * 消毒管理对象 op_disinfection_management
 * 
 * @author hhy
 * @date 2025-11-06
 */
@Data
public class OpDisinfectionManagement extends BaseEntity implements BaseEntity.CreateAware, BaseEntity.UpdateAware
{
    private static final long serialVersionUID = 1L;

    /** 消毒管理表主键 */
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

    /** 消毒开始时间 */
    private String disinfectionStartTime;

     /** 消毒结束时间 */
    private String disinfectionEndTime;

    /** 消毒方式 */
    @Excel(name = "消毒方式")
    private String disinfectionMethod;

    /** 消毒液 */
    @Excel(name = "消毒液")
    private String disinfectant;

    /** 部门id */
    @Excel(name = "部门id")
    private String deptId;

    /** 是否通过 */
    @Excel(name = "是否通过")
    private String passed;

    /** 是否删除：0-否 1-是 */
    @Excel(name = "是否删除：0-否 1-是")
    private String isDelete;

     /** 部门名称 */
    @Excel(name = "部门名称")
    private String deptName;

    /** 消毒管理记录信息 */
    private List<OpDisinfectionManagementRecord> opDisinfectionManagementRecordList;

}
