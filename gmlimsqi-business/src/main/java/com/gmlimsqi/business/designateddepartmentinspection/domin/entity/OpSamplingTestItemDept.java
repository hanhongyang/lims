package com.gmlimsqi.business.designateddepartmentinspection.domin.entity;

import lombok.Data;
import com.gmlimsqi.common.annotation.Excel;
import com.gmlimsqi.common.core.domain.BaseEntity;

/**
 * 检测项目指定部门检测对象 op_sampling_test_item_dept
 * 
 * @author hhy
 * @date 2025-11-27
 */
@Data
public class OpSamplingTestItemDept extends BaseEntity implements BaseEntity.CreateAware, BaseEntity.UpdateAware
{
    private static final long serialVersionUID = 1L;

    /** 取样检测项目部门主键 */
    private String opSamplingTestItemDeptId;

    /** 原部门id */
    @Excel(name = "原部门id")
    private String deptId;

    /** 指定部门id */
    @Excel(name = "指定部门id")
    private String designatedDeptId;

    /** 物料id */
/*    @Excel(name = "物料id")
    private String invbillId;

    *//** 物料编码 *//*
    @Excel(name = "物料编码")
    private String invbillCode;

    *//** 物料名称 *//*
    @Excel(name = "物料名称")
    private String invbillName;*/

    /** 项目id */
    @Excel(name = "项目id")
    private String itemId;

    /** 项目编码 */
    @Excel(name = "项目编码")
    private String itemCode;

    /** 项目名称 */
    @Excel(name = "项目名称")
    private String itemName;

    /** 是否删除 */
    @Excel(name = "是否删除")
    private String isDelete;

     /** 原部门名称 */
    @Excel(name = "原部门名称")
    private String deptName;

     /** 指定部门名称 */
    @Excel(name = "指定部门名称")
    private String designatedDeptName;

}
