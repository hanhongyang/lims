package com.gmlimsqi.business.ranch.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.gmlimsqi.business.basicdata.domain.BsInvbillItemStandard;
import com.gmlimsqi.business.ranch.domain.OpSamplingPlanItem;
import com.gmlimsqi.common.annotation.Excel;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class SamplingReceiveListVo {
    /** 送检单号 */
    @Excel(name = "送检单号")
    private String samplingPlanNo;

    private String samplingPlanSampleId;
    /** 样品名称 */
    @Excel(name = "样品名称")
    private String invbillName;
    private String sampleNo;
    /** 规格 */
    @Excel(name = "规格")
    private String model;
    /** 生产日期 */
    @Excel(name = "生产日期")
    private String productionDate;

    /** 保质期 */
    @Excel(name = "保质期")
    private String shelfLife;

    /** 随车检验报告id */
    @Excel(name = "随车检验报告id")
    private String carFileId;

    /** 是否接收 */
    @Excel(name = "是否接收")
    private String isReceive;

    /** 接收时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "接收时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date receiveTime;

    /** 接收人id */
    @Excel(name = "接收人id")
    private String receiverId;

    /** 创建人 */
    private String createBy;

    /** 创建时间 */
    private String createTime;

    /** 感官性状 0不合格 1合格 */
    @Excel(name = "感官性状 0不合格 1合格")
    private String ggQualityResult;

    /** 感官质检描述 */
    @Excel(name = "感官质检描述")
    private String ggQualityDescribe;

    //感官质检附件id
    private String ggQualityFileIds;

    //检测项目
    private  String itemNames;

    //取样人
    @Excel(name = "取样人")
    private String samplerName;

    //取样时间
    @Excel(name = "取样时间")
    private String  sampleTime;
    //取样人id
    private String samplerId;

    /** 物料编码 */
    @Excel(name = "物料编码")
    private String invbillCode;

    /** 样品类型 */
    private String samplingType;

    /** 样品新增的类型0：手动新增，1：扫码新增 */
    private String isscan;

    /**
     * 检测项目列表
     */
    private List<OpSamplingPlanItem> opSamplingPlanItemList;

}
