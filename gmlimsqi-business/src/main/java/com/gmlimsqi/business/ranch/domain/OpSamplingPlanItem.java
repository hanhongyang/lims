package com.gmlimsqi.business.ranch.domain;

import com.gmlimsqi.common.annotation.Excel;
import com.gmlimsqi.common.core.domain.BaseEntity;
import lombok.Data;

/**
 * 取样计划-项目对应对象 op_sampling_plan_item
 *
 * @author hhy
 * @date 2025-11-04
 */
@Data
public class OpSamplingPlanItem extends BaseEntity implements BaseEntity.CreateAware, BaseEntity.UpdateAware
{
    private static final long serialVersionUID = 1L;

    /** id */
    private String opSamplingPlanItemId;

    /** 样品委托单物料表id */
    @Excel(name = "样品委托单物料表id")
    private String samplingPlanSampleId;

    /** 项目名称 */
    @Excel(name = "项目名称")
    private String itemName;

    /** 项目编码 */
    @Excel(name = "项目编码")
    private String itemCode;

    /** 项目id */
    @Excel(name = "项目id")
    private String itemId;

    /** 是否删除 */
    @Excel(name = "是否删除")
    private String isDelete;

    /** 检测人id */
    @Excel(name = "检测人id")
    private String testUserId;

    /** 检测结果 */
    @Excel(name = "检测结果")
    private String testResult;

    /** 定性类型 1-阴性 2-阳性 */
    private String qualitativeType;

    /** 上限 */
    @Excel(name = "上限")
    private String upperLimit;

    /** 下限 */
    @Excel(name = "下限")
    private String lowerLimit;
    /** 特性 */
    private String featureName;

    /** 参考范围 */
    private String referenceRange;

    /** 定性或定量 1-定性 2-定量 */
    private String qualitativeOrQuantitative;

    /** 项目特性id */
    private String featureId;

    /** 物料项目标准id */
    private String bsInvbillItemStandardId;

    /** 化验单状态 (1待化验、2待提交、3待审核、4已审核) */
    private String testStatus;

    /** 单项判定结果 (合格/不合格) */
    private String checkResult;

    /** 单位 */
    private String unitOfMeasurement;

    /** 是否化验完成 (根据 status != '1' 判断) */
    public Boolean getIsCompleted() {
        return testStatus != null && !"1".equals(testStatus);
    }
}
