package com.gmlimsqi.business.basicdata.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.gmlimsqi.common.annotation.Excel;
import com.gmlimsqi.common.core.domain.BaseEntity;

/**
 * 物料项目标准对象 bs_invbill_item_standard
 * 
 * @author hhy
 * @date 2025-09-08
 */
@Data
public class BsInvbillItemStandard extends BaseEntity implements BaseEntity.CreateAware, BaseEntity.UpdateAware
{
    private static final long serialVersionUID = 1L;

    /** id */
    private String bsInvbillItemStandardId;

    /** 物料id */
    private String invbillId;
    /** 物料id */
    private String invbillCode;
    /** 物料名称 */
    private String invbillName;
    /** 组织id */
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private String deptId;
    /** 组织名称 */
    private String deptName;
    /** 项目id */
    private String itemId;
    private String itemCode;
    private String itemName;
    /** 特性id */
    private String featureId;
    private String featureName;
    private String upperLimit;
    private String lowerLimit;
    /** 执行标准 */
    @Excel(name = "执行标准")
    private String zxbz;

    /** 设备id */
    private String instrumentId;
    private String instrumentName;
    /** 是否删除（0否1是） */
    private String isDelete;
    /** 结果录入方式（1：结果式录入，2：过程式录入） */
    @Excel(name = "结果录入方式", readConverterExp = "1=结果式录入,2=过程式录入")
    private String resultAddType;
    /** 是否启用（0否1是） */
    @Excel(name = "是否启用", readConverterExp = "0=否,1=是")
    private String isEnable;

    /** 是否必检（0否1是） */
    @Excel(name = "是否必检", readConverterExp = "0=否,1=是")
    private String isBj;

    /** 是否初检（0否1是） */
    @Excel(name = "是否初检", readConverterExp = "0=否,1=是")
    private String isCj;
    //化验单模板编号
    private String testModelNo;
    //化验单模板名称
    private String testModelName;

    /** 定性或定量 （1：定性，2：定量） */
    private String qualitativeOrQuantitative;

    /** 定性类型 1-阴性 2-阳性 */
    private String qualitativeType;

    /** 单位 */
    private String unitOfMeasurement;

}
