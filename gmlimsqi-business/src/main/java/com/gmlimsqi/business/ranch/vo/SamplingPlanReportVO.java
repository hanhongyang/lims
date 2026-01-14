package com.gmlimsqi.business.ranch.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

// 报表响应VO
@Data
public class SamplingPlanReportVO {
    /** 样品表id */
    private String opSamplingPlanSampleId;

    /** 物料编码 */
    private String invbillCode;

    /** 产品名称 */
    private String invbillName;

    /** 检验日期 */
    private Date testTime;

    /** 样品状态 */
    private String status;

    /** 生产日期 */
    private Date productionDate;

    /** 部门id */
    private String deptId;

    /** 项目列表 */
    private List<ReportItemDTO> items;
    
    @Data
    public static class ReportItemDTO {

        /** 项目id */
        private String itemId;

        /** 项目编码 */
        private String itemCode;

        /** 项目名称 */
        private String itemName;

        /** 特征列表 */
        private ReportFeatureDTO feature;

        /** 结果值 */
        private String result;

        /** 判断结果 1合格2不合格3让步接收 */
        private String checkResult;
    }
    
    @Data 
    public static class ReportFeatureDTO {
        /** 特征名称 */
        private String name;

        /** 特征信息 */
        private String info;

        /** 上限 */
        private BigDecimal upperLimit;

        /** 下限 */
        private BigDecimal lowerLimit;

        /** 单位 */
        private String unitOfMeasurement;

        /** 特征类型 */
        private String qualitativeOrQuantitative;

        /** 特征类型 */
        private String qualitativeType;

        /** 标准值 */
        private String standardValue;

        /** 判定值 */
        private String judgeValue;
    }
}