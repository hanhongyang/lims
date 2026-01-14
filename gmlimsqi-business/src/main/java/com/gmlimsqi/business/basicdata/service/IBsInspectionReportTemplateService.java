package com.gmlimsqi.business.basicdata.service;

import java.util.List;
import com.gmlimsqi.business.basicdata.domain.BsInspectionReportTemplate;

/**
 * 检验报告模板Service接口
 * 
 * @author hhy
 * @date 2025-11-26
 */
public interface IBsInspectionReportTemplateService 
{
    /**
     * 查询检验报告模板
     * 
     * @param id 检验报告模板主键
     * @return 检验报告模板
     */
    public BsInspectionReportTemplate selectBsInspectionReportTemplateById(String id);

    /**
     * 查询检验报告模板列表
     * 
     * @param bsInspectionReportTemplate 检验报告模板
     * @return 检验报告模板集合
     */
    public List<BsInspectionReportTemplate> selectBsInspectionReportTemplateList(BsInspectionReportTemplate bsInspectionReportTemplate);

    /**
     * 新增检验报告模板
     * 
     * @param bsInspectionReportTemplate 检验报告模板
     * @return 结果
     */
    public int insertBsInspectionReportTemplate(BsInspectionReportTemplate bsInspectionReportTemplate);

    /**
     * 修改检验报告模板
     * 
     * @param bsInspectionReportTemplate 检验报告模板
     * @return 结果
     */
    public int updateBsInspectionReportTemplate(BsInspectionReportTemplate bsInspectionReportTemplate);


}
