package com.gmlimsqi.business.ranch.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.gmlimsqi.business.ranch.domain.OpTestResultInfo;
import com.gmlimsqi.common.annotation.Excel;
import lombok.Data;

import java.util.List;

/**
 * 样品检测详情，推送SAP查看的详情
 */
@Data
public class SampleInfoVO {

    /** id */
    private String opSamplingPlanSampleId;

    /** 取样计划主表id */
    @Excel(name = "取样计划主表id")
    private String samplingPlanId;

    /** 样品编号 */
    @Excel(name = "样品编号")
    private String sampleNo;

    /** 物料id */
    @Excel(name = "物料id")
    private String invillId;

    /** 物料编码 */
    @Excel(name = "物料编码")
    private String invbillCode;

    /** 样品名称 */
    @Excel(name = "样品名称")
    private String invbillName;

    /** 是否推送SAP */
    @Excel(name = "是否推送SAP")
    private String isPushSap;

    /** 是否合格 */
    @Excel(name = "是否合格")
    private String whetherQualified;

     /** 检验批次 */
    @Excel(name = "检验批次")
    @JsonProperty("PRUEFLOS")
    private String PRUEFLOS;

    /**
     * 检测项目列表
     */
    private List<OpTestResultInfo> opTestResultInfoList;

}
