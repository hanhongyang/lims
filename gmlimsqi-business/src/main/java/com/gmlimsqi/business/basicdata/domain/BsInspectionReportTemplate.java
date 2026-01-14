package com.gmlimsqi.business.basicdata.domain;

import lombok.Data;
import com.gmlimsqi.common.annotation.Excel;
import com.gmlimsqi.common.core.domain.BaseEntity;

/**
 * 检验报告模板对象 bs_inspection_report_template
 * 
 * @author hhy
 * @date 2025-11-26
 */
@Data
public class BsInspectionReportTemplate extends BaseEntity implements BaseEntity.CreateAware, BaseEntity.UpdateAware
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private String id;

    /** 模板编码 */
    @Excel(name = "模板编码")
    private String templateCode;

    /** 模板名称 */
    @Excel(name = "模板名称")
    private String templateName;

    /** 模板类型，0：饲料检验模板 */
    @Excel(name = "模板类型，0：饲料检验模板")
    private String templateType;

    /** 是否删除 */
    @Excel(name = "是否删除")
    private String isDelete;

    /** 文件信息 */
    @Excel(name = "文件信息")
    private String fileInfo;

    /** 判断依据 */
    @Excel(name = "判断依据")
    private String basisForJudgment;

    /** 主要仪器 */
    @Excel(name = "主要仪器")
    private String mainInstruments;

    /** 检验方法 */
    @Excel(name = "检验方法")
    private String inspectionMethod;

    /** 感官性状 */
    @Excel(name = "感官性状")
    private String sensoryTraits;

    /** 检验类别 */
    @Excel(name = "检验类别")
    private String inspectionCategory;

    /** 包装规格 */
    @Excel(name = "包装规格")
    private String packagingSpecifications;

    /** 部门ID */
    @Excel(name = "部门ID")
    private String deptId;

}
