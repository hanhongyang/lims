package com.gmlimsqi.business.basicdata.controller;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.gmlimsqi.business.basicdata.domain.LabtestMethodsFormula;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.gmlimsqi.common.annotation.Log;
import com.gmlimsqi.common.core.controller.BaseController;
import com.gmlimsqi.common.core.domain.AjaxResult;
import com.gmlimsqi.common.enums.BusinessType;
import com.gmlimsqi.business.basicdata.service.ILabtestMethodsFormulaService;
import com.gmlimsqi.common.utils.poi.ExcelUtil;
import com.gmlimsqi.common.core.page.TableDataInfo;

/**
 * 检测公式Controller
 * 
 * @author ruoyi
 * @date 2025-08-06
 */
@RestController
@RequestMapping("/basicdata/formula")
public class LabtestMethodsFormulaController extends BaseController
{
    @Autowired
    private ILabtestMethodsFormulaService bsLabtestMethodsFormulaService;

    /**
     * 查询检测公式列表
     */
    @PreAuthorize("@ss.hasPermi('basicdata:formula:list')")
    @GetMapping("/list")
    public TableDataInfo list(LabtestMethodsFormula labtestMethodsFormula)
    {
        startPage();
        List<LabtestMethodsFormula> list = bsLabtestMethodsFormulaService.selectBsLabtestMethodsFormulaList(labtestMethodsFormula);
        return getDataTable(list);
    }

    /**
     * 导出检测公式列表
     */
    @PreAuthorize("@ss.hasPermi('basicdata:formula:export')")
    @Log(title = "检测公式", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, LabtestMethodsFormula labtestMethodsFormula)
    {
        List<LabtestMethodsFormula> list = bsLabtestMethodsFormulaService.selectBsLabtestMethodsFormulaList(labtestMethodsFormula);
        ExcelUtil<LabtestMethodsFormula> util = new ExcelUtil<LabtestMethodsFormula>(LabtestMethodsFormula.class);
        util.exportExcel(response, list, "检测公式数据");
    }

    /**
     * 获取检测公式详细信息
     */
    @PreAuthorize("@ss.hasPermi('basicdata:formula:query')")
    @GetMapping(value = "/{bsLabtestMethodsFormulaId}")
    public AjaxResult getInfo(@PathVariable("bsLabtestMethodsFormulaId") String bsLabtestMethodsFormulaId)
    {
        return success(bsLabtestMethodsFormulaService.selectBsLabtestMethodsFormulaByBsLabtestMethodsFormulaId(bsLabtestMethodsFormulaId));
    }

    /**
     * 新增检测公式
     */
    @PreAuthorize("@ss.hasPermi('basicdata:formula:add')")
    @Log(title = "检测公式", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody LabtestMethodsFormula labtestMethodsFormula)
    {
        return toAjax(bsLabtestMethodsFormulaService.insertBsLabtestMethodsFormula(labtestMethodsFormula));
    }

    /**
     * 修改检测公式
     */
    @PreAuthorize("@ss.hasPermi('basicdata:formula:edit')")
    @Log(title = "检测公式", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody LabtestMethodsFormula labtestMethodsFormula)
    {
        return toAjax(bsLabtestMethodsFormulaService.updateBsLabtestMethodsFormula(labtestMethodsFormula));
    }

    /**
     * 删除检测公式
     */
    @PreAuthorize("@ss.hasPermi('basicdata:formula:remove')")
    @Log(title = "检测公式", businessType = BusinessType.DELETE)
	@DeleteMapping("/{bsLabtestMethodsFormulaIds}")
    public AjaxResult remove(@PathVariable String[] bsLabtestMethodsFormulaIds)
    {
        return toAjax(bsLabtestMethodsFormulaService.deleteBsLabtestMethodsFormulaByBsLabtestMethodsFormulaIds(bsLabtestMethodsFormulaIds));
    }
}
