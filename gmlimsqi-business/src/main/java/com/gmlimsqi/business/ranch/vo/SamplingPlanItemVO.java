package com.gmlimsqi.business.ranch.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class SamplingPlanItemVO {
    // 样品信息
    private String opSamplingPlanSampleId;
    private String invbillCode;
    private String invbillName;
    private Date testTime;
    private String status;
    private Date productionDate;
    private String deptId;
    
    // 项目信息
    private String itemId;
    private String itemCode;
    private String itemName;
    
    // 特性信息（每个项目一个特性）
    private String featureName;
    private String featureInfo;
    private BigDecimal upperLimit;
    private BigDecimal lowerLimit;
    private String unitOfMeasurement;
    private String qualitativeOrQuantitative;
    private String qualitativeType;

    /** 结果值 */
    private String result;

    /** 判断结果 1合格2不合格3让步接收 */
    private String checkResult;

    /** 标准值 */
    private String standardValue;

    /** 判定值 */
    private String judgeValue;

}