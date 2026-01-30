package com.gmlimsqi.business.samplingplan.pojo.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.gmlimsqi.business.ranch.domain.OpSamplingPlanSample;
import com.gmlimsqi.common.core.domain.BaseEntity;
import lombok.Data;
import com.gmlimsqi.common.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

/**
 * 成品，库存，垫料取样计划详情对象 op_finished_product_sampling_plan_detail
 * 
 * @author hhy
 * @date 2025-11-24
 */
@Data
public class OpFinishedProductSamplingPlanDetail extends BaseEntity implements BaseEntity.CreateAware, BaseEntity.UpdateAware
{
    private static final long serialVersionUID = 1L;

    /** 成品，库存，垫料取样计划详情主键 */
    private String finishedProductSamplingPlanDetailId;

    /** 成品，库存，垫料取样计划主键 */
    @Excel(name = "成品，库存，垫料取样计划主键")
    private String finishedProductSamplingPlanId;

    /** 物料编码 */
    @Excel(name = "物料编码")
    private String materialCode;

    /** 物料名称 */
    @Excel(name = "物料名称")
    private String materialName;

    /** 生产订单号 */
    @Excel(name = "生产订单号")
    private String productionOrderNumber;

    /** 计划生产量 */
    @Excel(name = "计划生产量")
    private String plannedProductionVolume;

    /** 计划取样份数 */
    @Excel(name = "计划取样份数")
    private String plannedSampleQuantity;

    /** 已取样份数 */
    @Excel(name = "已取样份数")
    private String haveSampleCopies;

    /** 附件 */
    private String file;

     /** 附件url */
    private String fileUrl;

    // 主表数据
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

    /** 部门ID */
    private String deptId;

     /** 导入人 */
    @Excel(name = "导入人")
    private String importBy;

    // 主表数据

    //样品子表
    private List<OpSamplingPlanSample> opSamplingPlanSampleList;

}
