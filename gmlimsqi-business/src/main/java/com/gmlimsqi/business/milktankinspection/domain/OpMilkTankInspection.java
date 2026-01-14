package com.gmlimsqi.business.milktankinspection.domain;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import com.gmlimsqi.common.annotation.Excel;
import com.gmlimsqi.common.core.domain.BaseEntity;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * 奶罐车质检对象 op_milk_tank_inspection
 * 
 * @author hhy
 * @date 2025-11-12
 */
@Data
public class OpMilkTankInspection extends BaseEntity implements BaseEntity.CreateAware, BaseEntity.UpdateAware
{
    private static final long serialVersionUID = 1L;

    /** 奶罐车质检表主键 */
    @Excel(name = "奶罐车质检表主键")
    private String opMilkTankInspectionId;

    /** 奶罐车质检单编号 */
    @Excel(name = "奶罐车质检单编号")
    private String milkTankInspectionNumber;

    /** 奶源计划订单号 */
    @Excel(name = "奶源计划订单号")
    private String milkSourcePlanOrderNumber;

    /** 奶罐车id */
    private String carId;

    /** 车牌号 */
    @Excel(name = "车牌号")
    private String licensePlateNumber;

    /** 司机id */
    private String driverId;

    /** 司机名称 */
    @Excel(name = "司机名称")
    private String driverName;

    /** 挂车id */
    private String trailerId;

    /** 挂车车牌号 */
    @Excel(name = "挂车车牌号")
    private String trailerNumber;

    /** 进场时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "进场时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date entryTime;

    /** 目的地 */
    @Excel(name = "目的地")
    private String destination;

    /** 采样人id */
    @Excel(name = "采样人id")
    private String samlerId;

    /** 采样人 */
    @Excel(name = "采样人")
    private String sampler;

    /** 采样时间（默认当前时间） */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "采样时间")
    private Date samplingTime;

    /** 检测人id */
    @Excel(name = "检测人id")
    private String testerId;

    /** 检测人（默认当前人，可修改） */
    @Excel(name = "检测人", readConverterExp = "默=认当前人，可修改")
    private String tester;

    /** 检测日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "检测日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date testDate;

    /** 审核人id */
    @Excel(name = "审核人id")
    private String reviewerId;

    /** 审核人 */
    @Excel(name = "审核人")
    private String reviewer;

    /** 审核日期 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "审核日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date reviewTime;

    /** 菌落总数 */
    @Excel(name = "菌落总数")
    private String colonyCount;

    /** 嗜冷菌 */
    @Excel(name = "嗜冷菌")
    private String psychrophilicBacteria;

    /** 需氧芽孢 */
    @Excel(name = "需氧芽孢")
    private String aerobicSpores;

    /** 耐热芽孢 */
    @Excel(name = "耐热芽孢")
    private String thermotolerantSpores;

    /** 嗜热芽孢 */
    @Excel(name = "嗜热芽孢")
    private String thermophilicSpores;

    /** 耐热+嗜热芽孢 */
    @Excel(name = "耐热+嗜热芽孢")
    private String thermotolerantThermophilicSpores;

    /** 附件列表（存储文件路径或JSON字符串） */
    @Excel(name = "附件列表", readConverterExp = "存=储文件路径或JSON字符串")
    private String file;

    /** 附件列表（存储文件路径或JSON字符串） */
    private List<String> fileUrl;

    /** 状态：0-待审核，1-已审核 */
    @Excel(name = "状态：0-待审核，1-已审核")
    private String status;

    /** 创建人名称 */
    @Excel(name = "创建人名称")
    private String createByName;

    /** 部门id */
    @Excel(name = "部门id")
    private String deptId;

    /** 是否删除：0-否 1-是 */
    @Excel(name = "是否删除：0-否 1-是")
    private String isDelete;

    /**
     * 涂抹部位
     */
    private String applicationArea;

    /**
     * 涂抹总菌落数
     */
    private String smearTotalColony;

    /**
     * 涂抹嗜冷菌
     */
    private String applyPsychrophilicBacteria;

    /**
     * pH残留
     */
    private String residualpH;

    /**
     * 外观（奶车肉眼检查是否干净）
     */
    private String appearance;

    /**
     * 涂抹人
     */
    private String smearPerson;

}
