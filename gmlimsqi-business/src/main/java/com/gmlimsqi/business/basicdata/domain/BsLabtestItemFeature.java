package com.gmlimsqi.business.basicdata.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.gmlimsqi.common.annotation.Excel;
import com.gmlimsqi.common.core.domain.BaseEntity;

/**
 * 检测项目特性对应对象 bs_labtest_item_feature
 * 
 * @author hhy
 * @date 2025-09-05
 */
@Data
public class BsLabtestItemFeature extends BaseEntity implements BaseEntity.CreateAware, BaseEntity.UpdateAware
{
    private static final long serialVersionUID = 1L;

    /** id */
    private String bsLabtestItemFeatureId;

    /** 项目id */
    private String itemId;

    /** 项目名称 */
    @Excel(name = "项目名称")
    private String itemName;

    /** 特性id */
    private String featureId;

    /** 特性名称 */
    @Excel(name = "特性名称")
    private String featureName;

    /** 组织id */
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private String deptId;

    /** 是否删除（0否1是） */
    private String isDelete;

    /** 是否启用（0否1是） */
    private String isEnable;
    private String itemCode;
    private String itemSapCode;
    private String upperLimit;
    private String lowerLimit;

    /** 单位 */
    private String unitOfMeasurement;
}
