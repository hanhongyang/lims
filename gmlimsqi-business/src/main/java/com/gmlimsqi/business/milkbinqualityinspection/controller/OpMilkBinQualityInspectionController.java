package com.gmlimsqi.business.milkbinqualityinspection.controller;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.gmlimsqi.business.milkbinqualityinspection.domain.OpMilkBinQualityInspection;
import com.gmlimsqi.business.milkbinqualityinspection.service.IOpMilkBinQualityInspectionService;
import com.gmlimsqi.common.core.domain.R;
import org.springframework.security.access.prepost.PreAuthorize;
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
import com.gmlimsqi.common.utils.poi.ExcelUtil;
import com.gmlimsqi.common.core.page.TableDataInfo;

/**
 * 奶仓质检单Controller
 * 
 * @author hhy
 * @date 2025-11-10
 */
@RestController
@RequestMapping("/milkbinqualityinspection")
public class OpMilkBinQualityInspectionController extends BaseController
{
    @Autowired
    private IOpMilkBinQualityInspectionService opMilkBinQualityInspectionService;

    /**
     * 查询奶仓质检单列表
     */
//    @PreAuthorize("@ss.hasPermi('milkbin:inspection:list')")
    @GetMapping("/list")
    public TableDataInfo list(OpMilkBinQualityInspection opMilkBinQualityInspection)
    {
        startPage();
        List<OpMilkBinQualityInspection> list = opMilkBinQualityInspectionService.selectOpMilkBinQualityInspectionList(opMilkBinQualityInspection);
        return getDataTable(list);
    }

//    @PreAuthorize("@ss.hasPermi('milkbin:inspection:list')")
    @GetMapping("/listDay")
    public TableDataInfo listDay(OpMilkBinQualityInspection opMilkBinQualityInspection)
    {
        startPage();
        List<OpMilkBinQualityInspection> list = opMilkBinQualityInspectionService.listDay(opMilkBinQualityInspection);
        return getDataTable(list);
    }

    /**
     * 导出奶仓质检单列表
     */
//    @PreAuthorize("@ss.hasPermi('milkbin:inspection:export')")
    @Log(title = "奶仓质检单", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, OpMilkBinQualityInspection opMilkBinQualityInspection)
    {
        List<OpMilkBinQualityInspection> list = opMilkBinQualityInspectionService.selectOpMilkBinQualityInspectionList(opMilkBinQualityInspection);
        ExcelUtil<OpMilkBinQualityInspection> util = new ExcelUtil<OpMilkBinQualityInspection>(OpMilkBinQualityInspection.class);
        util.exportExcel(response, list, "奶仓质检单数据");
    }

    /**
     * 获取奶仓质检单详细信息
     */
//    @PreAuthorize("@ss.hasPermi('milkbin:inspection:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") String id)
    {
        return success(opMilkBinQualityInspectionService.selectOpMilkBinQualityInspectionById(id));
    }

    /**
     * 新增奶仓质检单
     */
//    @PreAuthorize("@ss.hasPermi('milkbin:inspection:add')")
    @Log(title = "奶仓质检单", businessType = BusinessType.INSERT)
    @PostMapping
    public R<String> add(@RequestBody OpMilkBinQualityInspection opMilkBinQualityInspection)
    {
        String id = opMilkBinQualityInspectionService.insertOpMilkBinQualityInspection(opMilkBinQualityInspection);
        return R.ok(id, "新增成功");
    }

    /**
     * 修改奶仓质检单
     */
//    @PreAuthorize("@ss.hasPermi('milkbin:inspection:edit')")
    @Log(title = "奶仓质检单", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody OpMilkBinQualityInspection opMilkBinQualityInspection)
    {
        return toAjax(opMilkBinQualityInspectionService.updateOpMilkBinQualityInspection(opMilkBinQualityInspection));
    }

    /**
     * 审核奶仓质检单
     */
//    @PreAuthorize("@ss.hasPermi('milkbin:inspection:edit')")
    @Log(title = "奶仓质检单", businessType = BusinessType.UPDATE)
    @PostMapping("/audit")
    public AjaxResult audit(@RequestBody String id)
    {
        return toAjax(opMilkBinQualityInspectionService.auditOpMilkBinQualityInspection(id));
    }


}
