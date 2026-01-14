package com.gmlimsqi.business.report.domain.vo;

import lombok.Data;

/**
 * 报表公共头信息
 */
@Data
public class ReportPublicHeaderVO {

    /** 取样，样品表id */
    private String opSamplingPlanSampleId;

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
     * 生产日期
     */
    private String productionDate;

    /**
     * 检测日期
     */
    private String testDate;

    /**
     * 供应商名称
     */
    private String supplierName;

}
