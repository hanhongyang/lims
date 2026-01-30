package com.gmlimsqi.business.milksamplequalityinspection.controller;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.gmlimsqi.business.milksamplequalityinspection.domain.ExitInspectionReportDTO;
import com.gmlimsqi.business.milksamplequalityinspection.domain.ExitInspectionReportVO;
import com.gmlimsqi.business.milksamplequalityinspection.domain.OpMilkSampleQIException;
import com.gmlimsqi.business.milksamplequalityinspection.domain.OpMilkSampleQualityInspection;
import com.gmlimsqi.business.milksamplequalityinspection.service.IOpMilkSampleQualityInspectionService;
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
 * 奶样质检Controller
 *
 * @author hhy
 * @date 2025-11-10
 */
@RestController
@RequestMapping("/milkSampleQualityInspection/inspection")
public class OpMilkSampleQualityInspectionController extends BaseController {
    @Autowired
    private IOpMilkSampleQualityInspectionService opMilkSampleQualityInspectionService;

    /**
     * 查询奶样质检列表
     */
//    @PreAuthorize("@ss.hasPermi('milkSampleQualityInspection:inspection:list')")
    @GetMapping("/list")
    public TableDataInfo list(OpMilkSampleQualityInspection opMilkSampleQualityInspection) {
        startPage();
        List<OpMilkSampleQualityInspection> list = opMilkSampleQualityInspectionService.selectOpMilkSampleQualityInspectionList(opMilkSampleQualityInspection);
        return getDataTable(list);
    }

    /**
     * 导出奶样质检列表
     */
//    @PreAuthorize("@ss.hasPermi('milkSampleQualityInspection:inspection:export')")
    @Log(title = "奶样质检", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, OpMilkSampleQualityInspection opMilkSampleQualityInspection) {
        List<OpMilkSampleQualityInspection> list = opMilkSampleQualityInspectionService.selectOpMilkSampleQualityInspectionList(opMilkSampleQualityInspection);
        ExcelUtil<OpMilkSampleQualityInspection> util = new ExcelUtil<OpMilkSampleQualityInspection>(OpMilkSampleQualityInspection.class);
        util.exportExcel(response, list, "奶样质检数据");
    }

    /**
     * 获取奶样质检详细信息
     */
//    @PreAuthorize("@ss.hasPermi('milkSampleQualityInspection:inspection:query')")
    @GetMapping(value = "/{opMilkSampleQualityInspectionId}")
    public AjaxResult getInfo(@PathVariable("opMilkSampleQualityInspectionId") String opMilkSampleQualityInspectionId) {
        return success(opMilkSampleQualityInspectionService.selectOpMilkSampleQualityInspectionByOpMilkSampleQualityInspectionId(opMilkSampleQualityInspectionId));
    }

    /**
     * 新增奶样质检
     */
//    @PreAuthorize("@ss.hasPermi('milkSampleQualityInspection:inspection:add')")
    @Log(title = "奶样质检", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody OpMilkSampleQualityInspection opMilkSampleQualityInspection) {
        return toAjax(opMilkSampleQualityInspectionService.insertOpMilkSampleQualityInspection(opMilkSampleQualityInspection));
    }

    /**
     * 手动新增奶样质检
     */
//    @PreAuthorize("@ss.hasPermi('milkSampleQualityInspection:inspection:add')")
    @Log(title = "奶样质检", businessType = BusinessType.INSERT)
    @PostMapping("/manuallyAdd")
    public AjaxResult manuallyAdd(@RequestBody OpMilkSampleQualityInspection opMilkSampleQualityInspection) {
        return toAjax(opMilkSampleQualityInspectionService.manuallyAdd(opMilkSampleQualityInspection));
    }

    /**
     * 修改奶样质检
     */
//    @PreAuthorize("@ss.hasPermi('milkSampleQualityInspection:inspection:edit')")
    @Log(title = "奶样质检", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody OpMilkSampleQualityInspection opMilkSampleQualityInspection) {
        return AjaxResult.success("修改成功",opMilkSampleQualityInspectionService.updateOpMilkSampleQualityInspection(opMilkSampleQualityInspection));
    }

    /**
     * 奶样质检单取样
     */
//    @PreAuthorize("@ss.hasPermi('milkSampleQualityInspection:inspection:edit')")
    @Log(title = "奶样质检", businessType = BusinessType.UPDATE)
    @PutMapping("/sampling")
    public AjaxResult sampling(@RequestBody OpMilkSampleQualityInspection opMilkSampleQualityInspection) {
        return toAjax(opMilkSampleQualityInspectionService.sampling(opMilkSampleQualityInspection));
    }

    /**
     * 审核奶样质检单
     */
//    @PreAuthorize("@ss.hasPermi('milkSampleQualityInspection:inspection:edit')")
    @Log(title = "奶样质检", businessType = BusinessType.UPDATE)
    @PutMapping("/audit")
    public AjaxResult audit(@RequestBody String inspectionMilkTankersId) {
        return toAjax(opMilkSampleQualityInspectionService.auditOpMilkSampleQualityInspection(inspectionMilkTankersId));
    }

    /**
     * 手动推送奶源
     */
    @Log(title = "奶样质检", businessType = BusinessType.UPDATE)
    @PutMapping("/pushMilkSource/{opMilkSampleQualityInspectionId}")
    public AjaxResult pushMilkSource(@PathVariable String opMilkSampleQualityInspectionId) {
        return toAjax(opMilkSampleQualityInspectionService.pushMilkSource(opMilkSampleQualityInspectionId));
    }

    /**
     * 查询奶样质检异常列表
     */
//    @PreAuthorize("@ss.hasPermi('milkSampleQualityInspection:inspection:list')")
    @GetMapping("/exception/list")
    public TableDataInfo exceptionList(OpMilkSampleQIException opMilkSampleQIException) {
        startPage();
        List<OpMilkSampleQIException> list = opMilkSampleQualityInspectionService.selectOpMilkSampleQIExceptionList(opMilkSampleQIException);
        return getDataTable(list);
    }

    /**
     * 提交异常
     */
//    @PreAuthorize("@ss.hasPermi('milkSampleQualityInspection:inspection:edit')")
    @Log(title = "奶样质检异常", businessType = BusinessType.INSERT)
    @PostMapping("/exception/submit")
    public AjaxResult addException(@RequestBody OpMilkSampleQIException opMilkSampleQIException) {
        return toAjax(opMilkSampleQualityInspectionService.submitOpMilkSampleQIException(opMilkSampleQIException));
    }

    /**
     * 出场检测报表查询
     *
     * @return {@link List<ExitInspectionReportVO>}
     */
//    @PreAuthorize("@ss.hasPermi('milkSampleQualityInspection:inspection:list')")
    @GetMapping("/exitInspectionReport/list")
    public TableDataInfo exitInspectionReport(ExitInspectionReportDTO exitInspectionReportDTO) {
        startPage();
        List<ExitInspectionReportVO> list = opMilkSampleQualityInspectionService.exitInspectionReport(exitInspectionReportDTO);
        return getDataTable(list);
    }

}
