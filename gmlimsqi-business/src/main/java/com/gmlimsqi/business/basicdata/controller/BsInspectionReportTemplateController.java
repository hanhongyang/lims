package com.gmlimsqi.business.basicdata.controller;

import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.gmlimsqi.common.annotation.Log;
import com.gmlimsqi.common.core.controller.BaseController;
import com.gmlimsqi.common.core.domain.AjaxResult;
import com.gmlimsqi.common.enums.BusinessType;
import com.gmlimsqi.business.basicdata.domain.BsInspectionReportTemplate;
import com.gmlimsqi.business.basicdata.service.IBsInspectionReportTemplateService;
import com.gmlimsqi.common.utils.poi.ExcelUtil;
import com.gmlimsqi.common.core.page.TableDataInfo;

/**
 * 检验报告模板Controller
 * 
 * @author hhy
 * @date 2025-11-26
 */
@RestController
@RequestMapping("/basicdata/template")
public class BsInspectionReportTemplateController extends BaseController
{
    @Autowired
    private IBsInspectionReportTemplateService bsInspectionReportTemplateService;

    /**
     * 查询检验报告模板列表
     */
//    @PreAuthorize("@ss.hasPermi('basicdata:template:list')")
    @GetMapping("/list")
    public TableDataInfo list(BsInspectionReportTemplate bsInspectionReportTemplate)
    {
        startPage();
        List<BsInspectionReportTemplate> list = bsInspectionReportTemplateService.selectBsInspectionReportTemplateList(bsInspectionReportTemplate);
        return getDataTable(list);
    }

    /**
     * 导出检验报告模板列表
     */
//    @PreAuthorize("@ss.hasPermi('basicdata:template:export')")
    @Log(title = "检验报告模板", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, BsInspectionReportTemplate bsInspectionReportTemplate)
    {
        List<BsInspectionReportTemplate> list = bsInspectionReportTemplateService.selectBsInspectionReportTemplateList(bsInspectionReportTemplate);
        ExcelUtil<BsInspectionReportTemplate> util = new ExcelUtil<BsInspectionReportTemplate>(BsInspectionReportTemplate.class);
        util.exportExcel(response, list, "检验报告模板数据");
    }

    /**
     * 获取检验报告模板详细信息
     */
//    @PreAuthorize("@ss.hasPermi('basicdata:template:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") String id)
    {
        return success(bsInspectionReportTemplateService.selectBsInspectionReportTemplateById(id));
    }

    /**
     * 新增检验报告模板
     */
//    @PreAuthorize("@ss.hasPermi('basicdata:template:add')")
    @Log(title = "检验报告模板", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody BsInspectionReportTemplate bsInspectionReportTemplate)
    {
        return toAjax(bsInspectionReportTemplateService.insertBsInspectionReportTemplate(bsInspectionReportTemplate));
    }

    /**
     * 修改检验报告模板
     */
//    @PreAuthorize("@ss.hasPermi('basicdata:template:edit')")
    @Log(title = "检验报告模板", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody BsInspectionReportTemplate bsInspectionReportTemplate)
    {
        return toAjax(bsInspectionReportTemplateService.updateBsInspectionReportTemplate(bsInspectionReportTemplate));
    }


}
