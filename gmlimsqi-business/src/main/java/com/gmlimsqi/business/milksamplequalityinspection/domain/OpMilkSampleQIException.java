package com.gmlimsqi.business.milksamplequalityinspection.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.gmlimsqi.common.annotation.Excel;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 奶样质检异常列表
 */
@Data
public class OpMilkSampleQIException {

    /** 奶样质检列表id */
    private String opMilkSampleQualityInspectionId;

    /** 车牌号 */
    @Excel(name = "车牌号")
    private String licensePlateNumber;

    /** 奶样质检单号 */
    @Excel(name = "奶样质检单号")
    private String milkSampleQualityInspectionNumber;

    /** 奶源计划单号 */
    @Excel(name = "奶源计划单号")
    private String milkSourcePlanOrderNumber;

    /** 检测人（默认当前人可修改） */
    @Excel(name = "检测人")
    private String tester;

    /** 检测日期（待取样时为空） */
    @Excel(name = "检测日期")
    private Date testTime;

    /** 异常类型 */
    @Excel(name = "异常类型")
    private String exceptionType;

    /** 异常描述 */
    @Excel(name = "异常描述")
    private String exceptionDesc;

    /** 异常是否提交：0-否 1-是 */
    @Excel(name = "异常是否提交：0-否 1-是")
    private String isExceptionSubmit;

    // 查询条件
    /** 计划日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "计划日期")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date schedulingDate;

    /** 部门id */
    private String deptId;

}
