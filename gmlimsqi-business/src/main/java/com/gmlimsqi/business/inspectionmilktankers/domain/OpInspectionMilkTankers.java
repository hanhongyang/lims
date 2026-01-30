package com.gmlimsqi.business.inspectionmilktankers.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import com.gmlimsqi.common.annotation.Excel;
import com.gmlimsqi.common.core.domain.BaseEntity;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * 奶罐车检查对象 op_inspection_milk_tankers
 * 
 * @author hhy
 * @date 2025-11-10
 */
@Data
public class OpInspectionMilkTankers extends BaseEntity implements BaseEntity.CreateAware, BaseEntity.UpdateAware
{
    private static final long serialVersionUID = 1L;

    /** 奶罐车检查表主键 */
    private String inspectionMilkTankersId;

    /** 奶罐车检查单号 */
    @Excel(name = "奶罐车检查单号")
    private String inspectionMilkTankersNumber;

    /** 奶源计划单号 */
    @Excel(name = "奶源计划单号")
    private String milkSourcePlanOrderNumber;

    /** 状态，0：保存，1：审核 */
    @Excel(name = "状态，0：保存，1：审核")
    private String status;

    /** 车牌号 */
    @Excel(name = "车牌号")
    private String licensePlateNumber;

    /** 缸盖排气孔照片 */
    @Excel(name = "缸盖排气孔照片")
    private String cylinderHeadExhaustHolePhoto;

    /** 缸盖排气孔照片路径 */
    private String cylinderHeadExhaustHolePhotoUrl;

    /** 奶罐喷淋头照片 */
    @Excel(name = "奶罐喷淋头照片")
    private String milkCanSprayHeadPhoto;

    /** 奶罐喷淋头照片路径 */
    private String milkCanSprayHeadPhotoUrl;

    /** 内壁照片 */
    @Excel(name = "内壁照片")
    private String washerPhoto;

     /** 内壁照片路径 */
    private String washerPhotoUrl;

    /** 缸盖垫圈照片 */
/*    @Excel(name = "缸盖垫圈照片")
    private String cylinderHeadGasketPhoto;

     *//** 缸盖垫圈照片路径 *//*
    private String cylinderHeadGasketPhotoUrl;*/

    /** 进场时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "进场时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date entryTime;

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

    /** 计划日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "计划日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date  schedulingDate;

    /** 订单编号 */
    @Excel(name = "订单编号")
    private String orderNumber;

     /** 车id */
    @Excel(name = "车id")
    private String carId;

     /** 司机id */
    private String driverId;

     /** 司机名称 */
    @Excel(name = "司机名称")
    private String driverName;

    /** 挂车id */
    private String trailerId;

     /** 挂车车牌 */
    @Excel(name = "挂车车牌")
    private String trailerNumber;

    /** 是否推送奶源：0-否 1-是 */
    private String isPushMilkSource;

}
