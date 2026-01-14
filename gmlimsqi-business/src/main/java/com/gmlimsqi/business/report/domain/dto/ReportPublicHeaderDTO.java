package com.gmlimsqi.business.report.domain.dto;

import lombok.Data;

import java.util.Date;

/**
 * 报表公共头信息
 */
@Data
public class ReportPublicHeaderDTO {

    /**
     * 饲料工厂名称
     */
    private String feedFactoryName;

    /**
     * 年份
     */
    private String year;

     /**
      * 月份
      */
    private String month;

     /**
      * 样品编号
      */
    private String sampleNo;

    /**
     * 样品名称
     */
    private String sampleName;

    /**
     * 测试开始日期
     */
    private Date testStartDate;

    /**
     * 测试结束日期
     */
    private Date testEndDate;

    /**
     * 样品类型
     */
    private String samplingType;

    /** 委外厂家 */
    private String outsourcedFactory;

    /** 部门id */
    private String deptId;

}
