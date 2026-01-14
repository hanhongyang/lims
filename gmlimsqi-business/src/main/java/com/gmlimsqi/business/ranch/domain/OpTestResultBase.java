package com.gmlimsqi.business.ranch.domain;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import com.gmlimsqi.common.annotation.Excel;
import com.gmlimsqi.common.core.domain.BaseEntity;

/**
 * 样品化验对象 op_test_result_base
 * 
 * @author hhy
 * @date 2025-11-07
 */
@Data
public class OpTestResultBase extends BaseEntity implements BaseEntity.CreateAware, BaseEntity.UpdateAware
{
    private static final long serialVersionUID = 1L;

    /** 化验结果基础表id */
    private String id;

    /** 部门id */
    private String deptId;

    /** 化验单号 */
    @Excel(name = "化验单号")
    private String resultNo;

    /** 部门名称 */
    @Excel(name = "部门名称")
    private String deptName;

    /** 项目名称 */
    @Excel(name = "项目名称")
    private String itemName;

    /** 项目id */
    private String itemId;

    /** 审核人 */
    @Excel(name = "审核人")
    private String examineUser;

    /** 审核人id */
    private String examineUserId;

    /** 审核时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "审核时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date examineTime;

    /** 检测时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "检测时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date testTime;

    /** 检测人 */
    @Excel(name = "检测人")
    private String testUser;

    /** 检测人id */
    private String testUserId;

    /** 结果录入方式 （1：结果式，2：过程式） */
    @Excel(name = "结果录入方式 ", readConverterExp = "1=：结果式，2：过程式")
    private String resultAddType;

    /** 状态 1待化验、2待提交、3待审核、4已审核 */
    @Excel(name = "状态 1待化验、2待提交、3待审核、4已审核")
    private String status;

    /** 退回原因 */
    @Excel(name = "退回原因")
    private String returnReason;

    /** 删除id（0为未删除，删除则为id） */
    private String deleteId;
    /** 化验结果信息子表 */
    private List<OpTestResultInfo> opTestResultInfoList;

    /** 取样人 */
    @Excel(name = "取样人")
    private String samplerName;
}
