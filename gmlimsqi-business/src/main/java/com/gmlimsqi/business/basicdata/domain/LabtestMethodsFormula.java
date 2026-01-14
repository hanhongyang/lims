package com.gmlimsqi.business.basicdata.domain;

import lombok.Data;
import com.gmlimsqi.common.core.domain.BaseEntity;

/**
 * 检测公式对象 bs_labtest_methods_formula
 * 
 * @author ruoyi
 * @date 2025-08-06
 */
@Data
public class LabtestMethodsFormula extends BaseEntity implements BaseEntity.CreateAware, BaseEntity.UpdateAware
{
    private static final long serialVersionUID = 1L;

    /** id */
    private String bsLabtestMethodsFormulaId;

    /** 检测方法id */
    private String bsLabtestMethodsId;

    /** 公式内容 */
    private String formulaContent;

    /** 公式描述 */
    private String formulaDescription;

    /** 是否删除（0否1是） */
    private String isDelete;

}
