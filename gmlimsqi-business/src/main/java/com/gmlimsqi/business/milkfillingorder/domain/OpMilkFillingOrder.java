package com.gmlimsqi.business.milkfillingorder.domain;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import com.gmlimsqi.common.annotation.Excel;
import com.gmlimsqi.common.core.domain.BaseEntity;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * 装奶单对象 op_milk_filling_order
 * 
 * @author hhy
 * @date 2025-11-10
 */
@Data
public class OpMilkFillingOrder extends BaseEntity implements BaseEntity.CreateAware, BaseEntity.UpdateAware
{
    private static final long serialVersionUID = 1L;

    /** 装奶单主键 */
    private String opMilkFillingOrderId;

    /** 装奶单号 */
    @Excel(name = "装奶单号")
    private String milkFillingOrderNumber;

    /** 奶源计划单号 */
    @Excel(name = "奶源计划单号")
    private String milkSourcePlanOrderNumber;

    /** 奶罐车检查表主键 */
    @Excel(name = "奶罐车检查表主键")
    private String inspectionMilkTankersId;

    /** 车牌号 */
    @Excel(name = "车牌号")
    private String licensePlateNumber;

    /** 状态，0：保存，1：审核 */
    @Excel(name = "状态，0：保存，1：审核")
    private String status;

    /** 最早一滴挤奶时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "最早一滴挤奶时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date firstMilkingTime;

    /** 装奶总量 */
    @Excel(name = "装奶总量")
    private String totalMilkCapacity;

    /** 部门id */
    @Excel(name = "部门id")
    private String deptId;

    /** 创建人名称 */
    @Excel(name = "创建人名称")
    private String createByName;

    /** 前置是否完成 */
    @Excel(name = "前置是否完成")
    private String preStepCompleted;

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

    /** 装奶时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "装奶时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date milkFillingTime;

    /** 装奶开始时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "装奶开始时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date milkFillingStartTime;

     /** 装奶结束时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "装奶结束时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date milkFillingEndTime;

    /** 是否推送奶源：0-否 1-是 */
    private String isPushMilkSource;

    /** 上传文件路径 */
    private String file;

     /** 上传文件路径url */
    private List<String> fileUrl;

    /** 装奶单子表 */
    private List<OpMilkFillingOrderDetail> opMilkFillingOrderDetailList;

    /**
     * 磅单号
     */
    private String cweightno;

    /**
     * 司机车牌号
     */
    private String driverCode;

    /**
     * 是否过毛（默认0）
     */
    private String gmFlag;

    /**
     * 毛重
     */
    private String gmweight;

    /**
     * 过毛时间
     */
    private String gmtime;

    /**
     * 是否过皮（默认0）
     */
    private String gpFlag;

    /**
     * 皮重
     */
    private String gpweight;

    /**
     * 过皮时间
     */
    private String gptime;

    /**
     * 净重
     */
    private String netWeight;

    /**
     * 结算重量
     */
    private String jsWeight;

    /**
     * 地磅上传时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date dbCreateTime;

    /**
     * 是否关联磅单 (0: 未关联, 1: 已关联)
     */
    private String isAssociatedWeighbridge;

    /**
     * 磅单数量
     */
    private String weighbridgeBillQuantity;

}
