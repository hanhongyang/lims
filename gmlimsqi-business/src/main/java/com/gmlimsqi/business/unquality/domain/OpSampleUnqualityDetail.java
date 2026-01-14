package com.gmlimsqi.business.unquality.domain;

import java.math.BigDecimal;

import com.gmlimsqi.common.core.domain.BaseEntity;
import lombok.Data;
import com.gmlimsqi.common.annotation.Excel;

/**
 * 样品不合格处理单详情对象 op_sample_unquality_detail
 * 
 * @author hhy
 * @date 2025-11-28
 */
@Data
public class OpSampleUnqualityDetail extends BaseEntity implements BaseEntity.CreateAware, BaseEntity.UpdateAware
{
    private static final long serialVersionUID = 1L;

    /** 不合格处理单详情主键 */
    private String opSampleUnqualityDetailId;

    /** 不合格处理单主键 */
    @Excel(name = "不合格处理单主键")
    private String opSampleUnqualityId;

    /** 项目id */
    @Excel(name = "项目id")
    private String itemId;

    /** 项目编码 */
    @Excel(name = "项目编码")
    private String itemCode;

    /** 项目名称 */
    @Excel(name = "项目名称")
    private String itemName;

    /** 特性 */
    private String ctx;

    /** 检测结果 */
    private String ctestresult;

    /** 是否合格 */
    private String chg;

}
