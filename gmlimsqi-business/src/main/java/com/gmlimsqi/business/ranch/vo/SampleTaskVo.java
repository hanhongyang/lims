// (新文件) com/gmlimsqi/business/ranch/vo/SampleTaskVo.java
package com.gmlimsqi.business.ranch.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.gmlimsqi.common.annotation.Excel;
import com.gmlimsqi.common.core.domain.BaseEntity;
import lombok.Data;
import java.util.Date;

@Data
public class SampleTaskVo extends BaseEntity implements BaseEntity.CreateAware, BaseEntity.UpdateAware {

    /**
     * 任务ID (核心)，对应 op_sampling_plan_item 的主键
     */
    @Excel(name = "任务ID")
    private String opSamplingPlanItemId;

    /**
     * 样品ID (父级)
     */
    @Excel(name = "样品ID")
    private String samplingPlanSampleId;

    /**
     * 样品编号 (来自 OpSamplingPlanSample)
     */
    @Excel(name = "样品编号")
    private String sampleNo;

    /**
     * 物料/样品名称 (来自 OpSamplingPlanSample)
     */
    @Excel(name = "物料/样品名称")
    private String invbillName;

    /**
     * 项目ID (来自 OpSamplingPlanItem)
     */
    @Excel(name = "项目ID")
    private String itemId;

    /**
     * 项目名称 (来自 OpSamplingPlanItem)
     */
    @Excel(name = "项目名称")
    private String itemName;

    /**
     * 收样时间 (来自 OpSamplingPlanSample)
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "收样时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date receiveTime;

    private String deptId;
}
