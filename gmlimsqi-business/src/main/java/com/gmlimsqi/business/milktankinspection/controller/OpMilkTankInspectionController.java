package com.gmlimsqi.business.milktankinspection.controller;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.gmlimsqi.business.milktankinspection.domain.OpMilkTankInspection;
import com.gmlimsqi.business.milktankinspection.service.IOpMilkTankInspectionService;
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
 * 奶罐车质检Controller
 * 
 * @author hhy
 * @date 2025-11-12
 */
@RestController
@RequestMapping("/milkTankInspection/inspection")
public class OpMilkTankInspectionController extends BaseController
{
    @Autowired
    private IOpMilkTankInspectionService opMilkTankInspectionService;

    /**
     * 查询奶罐车质检列表
     */
//    @PreAuthorize("@ss.hasPermi('milkTankInspection:inspection:list')")
    @GetMapping("/list")
    public TableDataInfo list(OpMilkTankInspection opMilkTankInspection)
    {
        startPage();
        List<OpMilkTankInspection> list = opMilkTankInspectionService.selectOpMilkTankInspectionList(opMilkTankInspection);
        return getDataTable(list);
    }

    /**
     * 导出奶罐车质检列表
     */
//    @PreAuthorize("@ss.hasPermi('milkTankInspection:inspection:export')")
    @Log(title = "奶罐车质检", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, OpMilkTankInspection opMilkTankInspection)
    {
        List<OpMilkTankInspection> list = opMilkTankInspectionService.selectOpMilkTankInspectionList(opMilkTankInspection);
        ExcelUtil<OpMilkTankInspection> util = new ExcelUtil<OpMilkTankInspection>(OpMilkTankInspection.class);
        util.exportExcel(response, list, "奶罐车质检数据");
    }

    /**
     * 获取奶罐车质检详细信息
     */
//    @PreAuthorize("@ss.hasPermi('milkTankInspection:inspection:query')")
    @GetMapping(value = "/{opMilkTankInspectionId}")
    public AjaxResult getInfo(@PathVariable("opMilkTankInspectionId") String opMilkTankInspectionId)
    {
        return success(opMilkTankInspectionService.selectOpMilkTankInspectionByOpMilkTankInspectionId(opMilkTankInspectionId));
    }

    /**
     * 新增奶罐车质检
     */
//    @PreAuthorize("@ss.hasPermi('milkTankInspection:inspection:add')")
    @Log(title = "奶罐车质检", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody OpMilkTankInspection opMilkTankInspection)
    {
        return toAjax(opMilkTankInspectionService.insertOpMilkTankInspection(opMilkTankInspection));
    }

    /**
     * 修改奶罐车质检
     */
//    @PreAuthorize("@ss.hasPermi('milkTankInspection:inspection:edit')")
    @Log(title = "奶罐车质检", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody OpMilkTankInspection opMilkTankInspection)
    {
        return toAjax(opMilkTankInspectionService.updateOpMilkTankInspection(opMilkTankInspection));
    }

    /**
     * 审核奶罐车质检
     */
//     @PreAuthorize("@ss.hasPermi('milkTankInspection:inspection:edit')")
    @Log(title = "奶罐车质检", businessType = BusinessType.UPDATE)
    @PutMapping("/audit/{opMilkTankInspectionId}")
    public AjaxResult audit(@PathVariable("opMilkTankInspectionId") String opMilkTankInspectionId)
    {
        return toAjax(opMilkTankInspectionService.audit(opMilkTankInspectionId));
    }

}
