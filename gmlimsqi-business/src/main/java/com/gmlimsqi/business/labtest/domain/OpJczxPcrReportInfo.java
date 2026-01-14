package com.gmlimsqi.business.basicdata.domain;

import com.gmlimsqi.common.annotation.Excel;
import com.gmlimsqi.common.core.domain.BaseEntity;
import lombok.Data;

/**
 * pcr报告子对象 op_jczx_pcr_report_info
 *
 * @author hhy
 * @date 2025-10-21
 */
@Data
public class OpJczxPcrReportInfo extends BaseEntity implements BaseEntity.CreateAware, BaseEntity.UpdateAware
{
    private static final long serialVersionUID = 1L;

    /** id */
    private String opJczxPcrReportInfoId;

    /** 主表id */
    @Excel(name = "主表id")
    private String baseId;

    /** 是否删除 */
    @Excel(name = "是否删除")
    private String isDelete;

    /** 检测结果 */
    @Excel(name = "检测结果")
    private String testResult;

    /** 提取试剂盒批号 */
    @Excel(name = "提取试剂盒批号")
    private String tqsjh;

    /** 扩增试剂盒批号 */
    @Excel(name = "扩增试剂盒批号")
    private String kzsjh;

    /** 所属牧场 */
    @Excel(name = "所属牧场")
    private String deptName;

    /** 样品名称 */
    @Excel(name = "样品名称")
    private String sampleName;

    /** 序号 */
    @Excel(name = "序号")
    private int sequence;

    /** 样品编号 */
    @Excel(name = "样品编号")
    private String sampleNo;

    /** 项目名称 */
    @Excel(name = "项目名称")
    private String itemName;

    //检测类型
    private String pcrTaskItemType;
    //项目排序
    private String sortOrder;

}
