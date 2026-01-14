package com.gmlimsqi.business.ranch.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.gmlimsqi.common.annotation.Excel;
import com.gmlimsqi.common.core.domain.BaseEntity;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;

/**
 * 取样计划主对象 op_sampling_plan
 *
 * @author hhy
 * @date 2025-11-04
 */
@Data
public class OpSamplingPlan extends BaseEntity implements BaseEntity.CreateAware, BaseEntity.UpdateAware {
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    private String opSamplingPlanId;

    /**
     * 送检单号
     */
    @Excel(name = "送检单号")
    private String samplingPlanNo;
    private String sampleNo;
    private String isReceive;
    /**
     * 供应商id
     */
    @Excel(name = "供应商id")
    private String supplierId;

    /**
     * 供应商编码
     */
    @Excel(name = "供应商编码")
    private String supplierCode;

    /**
     * 供应商名称
     */
    @Excel(name = "供应商名称")
    private String supplierName;

    /**
     * 司机名称
     */
    @Excel(name = "司机名称")
    private String driverName;

    /**
     * 司机手机号
     */

    private String driverPhone;

    /**
     * 司机车牌号
     */
    @NotEmpty(message = "车牌号不能为空")
    @Pattern(regexp = "^[京津沪渝冀豫云辽黑湘皖鲁新苏浙赣鄂桂甘晋蒙陕吉闽贵粤青藏川宁琼][A-Z][A-Z0-9]{4,5}[A-Z0-9挂学警港澳]$",
            message = "车牌号格式不正确")
    @Excel(name = "司机车牌号")
    private String driverCode;

    /**
     * 进厂时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "进厂时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date carInTime;

    /**
     * 出厂时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "出厂时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date carOutTime;

    /**
     * 消毒药
     */
    @Excel(name = "消毒药")
    private String toxicide;

    /**
     * 浓度
     */
    @Excel(name = "浓度")
    private String density;

    /**
     * 消毒方式
     */
    @Excel(name = "消毒方式")
    private String disinfection;

    /**
     * 责任人
     */
    @Excel(name = "责任人")
    private String personLiable;

    /**
     * 状态（0未提交  1已提交  2无需取样，3作废）修改状态，0是未提交，不为0且不为3的状态都是已提交，意思是已经取完样品了
     */
    @Excel(name = "0未提交  1已提交  2无需取样，3作废")
    private String status;

    /**
     * 删除id（0为未删除，删除则为id）
     */
    @Excel(name = "删除id", readConverterExp = "0=为未删除，删除则为id")
    private String deleteId;
    /**
     * 部门id
     */
    @Excel(name = "部门id")
    private String deptId;
    private String ggQualityResult;
    //取样人
    private String samplerName;
    //取样时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private String  sampleTime;
    //取样人id
    private String samplerId;
    //随车检验报告id
    private String carFileId;
    //随车检验报告url
    private String carFileUrl;
    //样品子表
    private List<OpSamplingPlanSample> opSamplingPlanSampleList;

    //签到id
    private String signInId;
    //是否放行
    private String isRelease;

    /**
     * 新增取样类型（1.牧场新增，2.饲料厂产品新增，3.饲料厂采购新增）-废弃
     */
    private String newSamplingTypes;

    private String samplingType;

    /** 开始时间（支持前端传yyyy-MM-dd格式） */
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8") // 时区必须加，避免解析偏移
    @DateTimeFormat(pattern = "yyyy-MM-dd")                // 适配表单/URL参数
    private Date createStartTime;

    /** 结束时间（支持前端传yyyy-MM-dd格式） */
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date createEndTime;

    /** 入场开始时间 */
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date carInStartTime;

    /** 入场结束时间 */
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date carInEndTime;

    /** 出场开始时间 */
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date carOutStartTime;

    /** 出场结束时间 */
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date carOutEndTime;

}
