package com.gmlimsqi.business.samplingplan.pojo.entity;

import java.util.List;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import com.gmlimsqi.common.annotation.Excel;
import com.gmlimsqi.common.core.domain.BaseEntity;

/**
 * 成品，库存，垫料取样计划对象 op_finished_product_sampling_plan
 * 
 * @author hhy
 * @date 2025-11-24
 */
@Data
public class OpFinishedProductSamplingPlan extends BaseEntity implements BaseEntity.CreateAware, BaseEntity.UpdateAware
{
    private static final long serialVersionUID = 1L;

    /** 成品，库存，垫料取样计划主键 */
    private String finishedProductSamplingPlanId;

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

    /** 状态 */
    @Excel(name = "状态，0：待审核，1：已审核")
    private String status;

    /** 是否删除 */
    @Excel(name = "是否删除")
    private String isDelete;

    /** 部门ID */
    private String deptId;

    /** 查询条件转换 */
    private List<String> planTypeList;

    /** 成品，库存，垫料取样计划详情信息 */
    private List<OpFinishedProductSamplingPlanDetail> opFinishedProductSamplingPlanDetailList;

}
