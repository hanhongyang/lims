package com.gmlimsqi.business.samplingplan.pojo.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.gmlimsqi.business.ranch.domain.OpSamplingPlanItem;
import com.gmlimsqi.common.annotation.Excel;
import com.gmlimsqi.common.core.domain.BaseEntity;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 取样单详情
 */
@Data
public class OpSamplingPlanSampleInfoVO extends BaseEntity implements BaseEntity.CreateAware, BaseEntity.UpdateAware {

    /** 取样计划单号 */
    @Excel(name = "取样计划单号")
    private String samplingOrderNumber;

    /** 计划时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "计划时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date planTime;

    /** 计划类型，0：成品，1：库存，2：垫料 */
    @Excel(name = "计划类型，0：成品，1：库存，2：垫料")
    private String planType;

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

    /** 规格 */
    @Excel(name = "规格")
    private String model;

    /** 净重（反填） */
    @Excel(name = "净重", readConverterExp = "反=填")
    private BigDecimal ijweight;

    /** 生产日期 */
    @Excel(name = "生产日期")
    private String productionDate;

    /** 保质期 */
    @Excel(name = "保质期")
    private String shelfLife;

    /** 默认500g */
    @Excel(name = "默认500g")
    private BigDecimal weight;

    /** 部门id */
    private String deptId;

    /** 状态*/
    @Excel(name = "样品状况")
    private String status;

    /** 样品顺序 */
    @Excel(name = "样品顺序")
    private Long sequence;

    /** 是否删除 */
    @Excel(name = "是否删除")
    private String isDelete;

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

    /** 报告id */
    @Excel(name = "报告id")
    private String reportId;

    /** 感官性状 0不合格 1合格 */
    @Excel(name = "感官性状 0不合格 1合格")
    private String ggQualityResult;

    /** 感官质检描述 */
    @Excel(name = "感官质检描述")
    private String ggQualityDescribe;
    //接收人
    private String receiverName;

    //感官质检附件id
    private String ggQualityFileIds;
    //感官质检附件url
    private String ggQualityFileUrls;
    //检测项目
    private List<OpSamplingPlanItem>  opSamplingPlanItemList;


    /**
     * 处理结果描述
     */
    @Excel(name = "处理结果描述")
    private String qualityDescribe;

    /**
     * 质检类型 0感官质检 1理化质检
     */
    @Excel(name = "质检类型 0感官质检 1理化质检")
    private String qualityType;

    /**
     * 扣款结果 0扣重 1扣款 2退货
     */
    @Excel(name = "扣款结果 0扣重 1扣款 2退货")
    private String debitResult;

    /**
     * 扣款金额
     */
    @Excel(name = "扣款金额")
    private BigDecimal debitAmount;

    /**
     * 扣款重量
     */
    @Excel(name = "扣款重量")
    private BigDecimal debitWeight;
    //附件id
    private String qualityFileIds;

    /**
     * 是否合格 R1不合格 A1合格
     */
    @Excel(name = "是否合格 R1不合格 A1合格")
    private String whetherQualified;

    /** 是否上传sap，0：未上传，1：ZTYPE=H已上传，2：ZTYPE=I已上传 */
    private String isPushSap;

    /** 检验批次 */
    @Excel(name = "检验批次")
    @JsonProperty("PRUEFLOS")
    private String PRUEFLOS;

    /** 取样地点 */
    @Excel(name = "取样地点")
    private String samplingLocation;

    /** 附件id */
    private String file;

    /** 附件url */
    private List<String> fileUrl;

    /** 样品类型 0：成品，1：库存，2：垫料 */
    @Excel(name = "样品类型 sampling_type 0：成品，1：库存，2：垫料")
    private String samplingType;

    /** 样品状态 0：待接收，1：已接收，2：已处理 */
    @Excel(name = "样品状态 0：待接收，1：已接收，2：已处理")
    private String sampleStatus;

    private String newSamplingTypes;

    /** 是否主样品 0：是，1：否 */
    private String isMainSample;

    /** 生产订单号 */
    @Excel(name = "生产订单号")
    private String productionOrderNumber;

    /** 出厂时间 */
    @Excel(name = "出厂时间")
    private Date carOutTime;

    /** 取样人 */
    @Excel(name = "取样人")
    private String samplerName;

    /** 仓库编码 */
    @Excel(name = "仓库编码")
    private String warehouseCode;

    /** 仓库名称 */
    private String warehouseName;

    /** 批次 */
    private String batch;

    /** 货位 */
    private String storageLocation;
}
