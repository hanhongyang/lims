package com.gmlimsqi.business.basicdata.domain;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.gmlimsqi.common.annotation.Excel;
import com.gmlimsqi.common.core.domain.BaseEntity;

/**
 * 检测特性对象 bs_labtest_feature
 * 
 * @author hhy
 * @date 2025-09-05
 */
@Data
public class BsLabtestFeature extends BaseEntity implements BaseEntity.CreateAware, BaseEntity.UpdateAware
{
    private static final long serialVersionUID = 1L;

    /** id */
    private String bsLabtestFeatureId;

    /** 特性描述 */
    @Excel(name = "特性描述")
    private String name;

    /** 信息字段 */
    private String info;

    /** 上限 */
    private BigDecimal upperLimit;

    /** 下限 */
    private BigDecimal lowerLimit;

    /** 计量单位 */
    private String unitOfMeasurement;

    /** 组织id */
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private String deptId;

    /** 是否删除（0否1是） */
    private String isDelete;

    /** 是否启用（0否1是） */
    @Excel(name = "是否启用", readConverterExp = "0=否,1=是")
    private String isEnable;

    /** 定性或定量 1-定性 2-定量 */
    private String qualitativeOrQuantitative;

    /** 定性类型 1-阴性 2-阳性 */
    private String qualitativeType;
    //允许误差
    private String allowError;
    //误差判断值
    private String errorInfo;
    private BigDecimal firstUpperLimit;
    private BigDecimal firstLowerLimit;
}
