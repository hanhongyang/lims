package com.gmlimsqi.business.labtest.domain;

import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.gmlimsqi.common.annotation.Excel;
import com.gmlimsqi.common.core.domain.BaseEntity;

/**
 * 检测中心饲料报告子对象 op_jczx_feed_report_info
 * 
 * @author hhy
 * @date 2025-10-14
 */
@Data
public class OpJczxFeedReportInfo extends BaseEntity implements BaseEntity.CreateAware, BaseEntity.UpdateAware
{
    private static final long serialVersionUID = 1L;

    /** id */
    private String opJczxFeedReportInfoId;

    /** 主表id */
    private String baseId;

    /** 项目id */
    @Excel(name = "项目id")
    private String itemId;

    /** 项目名称 */
    @Excel(name = "项目名称")
    private String itemName;

    /** 单位 */
    @Excel(name = "单位")
    private String unit;

    /** 技术要求 */
    @Excel(name = "技术要求")
    private String sapecification;

    /** 实测值 */
    @Excel(name = "实测值")
    private String valueOfTest;

    /** 检测依据 */
    @Excel(name = "检测依据")
    private String standard;


    /** 删除id（0为未删除，删除则为id） */
    private String deleteId;
    private String sampleNo;
    private String entrustOrderItemId;

}
