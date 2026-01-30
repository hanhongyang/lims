package com.gmlimsqi.business.inspectionmilktankers.controller;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.gmlimsqi.business.inspectionmilktankers.domain.OpInspectionMilkTankers;
import com.gmlimsqi.business.inspectionmilktankers.service.IOpInspectionMilkTankersService;
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
 * 奶罐车检查Controller
 *
 * @author hhy
 * @date 2025-11-10
 */
@RestController
@RequestMapping("/inspectionMilk/tankers")
public class OpInspectionMilkTankersController extends BaseController {
    @Autowired
    private IOpInspectionMilkTankersService opInspectionMilkTankersService;

    /**
     * 查询奶罐车检查列表
     */
//    @PreAuthorize("@ss.hasPermi('inspectionMilk:tankers:list')")
    @GetMapping("/list")
    public TableDataInfo list(OpInspectionMilkTankers opInspectionMilkTankers) {
        startPage();
        List<OpInspectionMilkTankers> list = opInspectionMilkTankersService.selectOpInspectionMilkTankersList(opInspectionMilkTankers);
        return getDataTable(list);
    }

    /**
     * 导出奶罐车检查列表
     */
//    @PreAuthorize("@ss.hasPermi('inspectionMilk:tankers:export')")
    @Log(title = "奶罐车检查", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, OpInspectionMilkTankers opInspectionMilkTankers) {
        List<OpInspectionMilkTankers> list = opInspectionMilkTankersService.selectOpInspectionMilkTankersList(opInspectionMilkTankers);
        ExcelUtil<OpInspectionMilkTankers> util = new ExcelUtil<OpInspectionMilkTankers>(OpInspectionMilkTankers.class);
        util.exportExcel(response, list, "奶罐车检查数据");
    }

    /**
     * 获取奶罐车检查详细信息
     */
//    @PreAuthorize("@ss.hasPermi('inspectionMilk:tankers:query')")
    @GetMapping(value = "/{inspectionMilkTankersId}")
    public AjaxResult getInfo(@PathVariable("inspectionMilkTankersId") String inspectionMilkTankersId) {
        return success(opInspectionMilkTankersService.selectOpInspectionMilkTankersByInspectionMilkTankersId(inspectionMilkTankersId));
    }

    /**
     * 新增奶罐车检查
     */
//    @PreAuthorize("@ss.hasPermi('inspectionMilk:tankers:add')")
    @Log(title = "奶罐车检查", businessType = BusinessType.INSERT)
    @PostMapping
    public R<String> add(@RequestBody OpInspectionMilkTankers opInspectionMilkTankers) {
        String id = opInspectionMilkTankersService.insertOpInspectionMilkTankers(opInspectionMilkTankers);
        return R.ok(id, "新增成功");
    }

    /**
     * 修改奶罐车检查
     */
//    @PreAuthorize("@ss.hasPermi('inspectionMilk:tankers:edit')")
    @Log(title = "奶罐车检查", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody OpInspectionMilkTankers opInspectionMilkTankers) {
        return toAjax(opInspectionMilkTankersService.updateOpInspectionMilkTankers(opInspectionMilkTankers));
    }

    /**
     * 审核奶罐车检查单
     */
//     @PreAuthorize("@ss.hasPermi('inspectionMilk:tankers:edit')")
    @Log(title = "奶罐车检查", businessType = BusinessType.UPDATE)
    @PutMapping("/audit/{inspectionMilkTankersId}")
    public AjaxResult audit(@PathVariable("inspectionMilkTankersId") String inspectionMilkTankersId) {
        return toAjax(opInspectionMilkTankersService.auditOpInspectionMilkTankers(inspectionMilkTankersId));
    }

    /**
     * 手动推送奶源
     */
    @Log(title = "奶罐车检查", businessType = BusinessType.UPDATE)
    @PutMapping("/pushMilkSource/{inspectionMilkTankersId}")
    public AjaxResult pushMilkSource(@PathVariable("inspectionMilkTankersId") String inspectionMilkTankersId) {
        return toAjax(opInspectionMilkTankersService.pushMilkSource(inspectionMilkTankersId));
    }

    /**
     * 变更计划单
     * @param opInspectionMilkTankers
     * @return
     */
    @PreAuthorize("@ss.hasPermi('inspectionMilk:tankers:change')")
    @Log(title = "奶罐车检查", businessType = BusinessType.UPDATE)
    @PutMapping("/changePlan")
    public AjaxResult changePlan(@RequestBody OpInspectionMilkTankers opInspectionMilkTankers) {
        return toAjax(opInspectionMilkTankersService.changePlan(opInspectionMilkTankers));
    }

}
