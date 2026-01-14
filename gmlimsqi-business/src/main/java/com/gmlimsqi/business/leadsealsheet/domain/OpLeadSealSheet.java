package com.gmlimsqi.business.leadsealsheet.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import com.gmlimsqi.common.annotation.Excel;
import com.gmlimsqi.common.core.domain.BaseEntity;

import java.util.Date;
import java.util.List;

/**
 * 铅封单对象 op_lead_seal_sheet
 * 
 * @author hhy
 * @date 2025-11-10
 */
@Data
public class OpLeadSealSheet extends BaseEntity implements BaseEntity.CreateAware, BaseEntity.UpdateAware
{
    private static final long serialVersionUID = 1L;

    /** 铅封单表主键 */
    private String opLeadSealSheetId;

    /** 奶源计划单号 */
    @Excel(name = "奶源计划单号")
    private String milkSourcePlanOrderNumber;

    /** 装奶单主键 */
    @Excel(name = "装奶单主键")
    private String opMilkFillingOrderId;

    /** 奶罐车检查表主键 */
    @Excel(name = "奶罐车检查表主键")
    private String inspectionMilkTankersId;

    /** 状态，0：保存，1：审核 */
    @Excel(name = "状态，0：保存，1：审核")
    private String status;

    /** 前置是否完成 */
    @Excel(name = "前置是否完成")
    private String preStepCompleted;

    /** 车牌号 */
    @Excel(name = "车牌号")
    private String licensePlateNumber;

    /** 铅封单号 */
    @Excel(name = "铅封单号")
    private String leadSealNumber;

    /** 铅封人id */
    @Excel(name = "铅封人id")
    private String sealedManId;

    /** 铅封人 */
    @Excel(name = "铅封人")
    private String sealedMan;

    /** 铅封照片 */
    @Excel(name = "铅封照片")
    private String sealedPhoto;

    /** 铅封照片url */
    private List<String> sealedPhotoUrl;

    /** 创建人名称 */
    @Excel(name = "创建人名称")
    private String createByName;

    /** 部门id */
    @Excel(name = "部门id")
    private String deptId;

    /** 是否删除：0-否 1-是 */
    @Excel(name = "是否删除：0-否 1-是")
    private String isDelete;

    /** 审核人名称 */
    @Excel(name = "审核人名称")
    private String reviewer;

    /** 审核人id */
    @Excel(name = "审核人id")
    private String reviewerId;

    /** 审核时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "审核时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date reviewTime;

    /** 是否推送奶源：0-否 1-是 */
    private String isPushMilkSource;

    /** 铅封号 */
    @Excel(name = "铅封号")
    private String qfNumber;

}
