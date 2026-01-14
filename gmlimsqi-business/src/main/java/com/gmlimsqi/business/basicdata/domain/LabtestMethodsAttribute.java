package com.gmlimsqi.business.basicdata.domain;

import java.math.BigDecimal;

import lombok.Data;
import com.gmlimsqi.common.core.domain.BaseEntity;

/**
 * 检测属性对象 bs_labtest_methods_attribute
 * 
 * @author ruoyi
 * @date 2025-08-06
 */
@Data
public class LabtestMethodsAttribute extends BaseEntity implements BaseEntity.CreateAware, BaseEntity.UpdateAware
{
    private static final long serialVersionUID = 1L;

    /** id */
    private String bsLabtestMethodsAttributeId;

    /** 检测方法id */
    private String bsLabtestMethodsId;

    /** 属性名称 */
    private String attributeName;

    /** 属性编码 */
    private String attributeCode;

    /** 属性类型（1字符2数字） */
    private String attributeType;

    /** 序号 */
    private BigDecimal seqNo;

    /** 默认值 */
    private String defaultValue;

    /** 最大值 */
    private String maxValue;

    /** 最小值 */
    private String minValue;

    /** 是否删除（0否1是） */
    private String isDelete;

}
