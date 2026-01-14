package com.gmlimsqi.business.leadsealsheet.controller;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.gmlimsqi.business.leadsealsheet.domain.OpLeadSealSheet;
import com.gmlimsqi.business.leadsealsheet.service.IOpLeadSealSheetService;
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
 * 铅封单Controller
 * 
 * @author hhy
 * @date 2025-11-10
 */
@RestController
@RequestMapping("/leadsealsheet/sheet")
public class OpLeadSealSheetController extends BaseController
{
    @Autowired
    private IOpLeadSealSheetService opLeadSealSheetService;

    /**
     * 查询铅封单列表
     */
//    @PreAuthorize("@ss.hasPermi('leadsealsheet:sheet:list')")
    @GetMapping("/list")
    public TableDataInfo list(OpLeadSealSheet opLeadSealSheet)
    {
        startPage();
        List<OpLeadSealSheet> list = opLeadSealSheetService.selectOpLeadSealSheetList(opLeadSealSheet);
        return getDataTable(list);
    }

    /**
     * 导出铅封单列表
     */
//    @PreAuthorize("@ss.hasPermi('leadsealsheet:sheet:export')")
    @Log(title = "铅封单", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, OpLeadSealSheet opLeadSealSheet)
    {
        List<OpLeadSealSheet> list = opLeadSealSheetService.selectOpLeadSealSheetList(opLeadSealSheet);
        ExcelUtil<OpLeadSealSheet> util = new ExcelUtil<OpLeadSealSheet>(OpLeadSealSheet.class);
        util.exportExcel(response, list, "铅封单数据");
    }

    /**
     * 获取铅封单详细信息
     */
//    @PreAuthorize("@ss.hasPermi('leadsealsheet:sheet:query')")
    @GetMapping(value = "/{opLeadSealSheetId}")
    public AjaxResult getInfo(@PathVariable("opLeadSealSheetId") String opLeadSealSheetId)
    {
        return success(opLeadSealSheetService.selectOpLeadSealSheetByOpLeadSealSheetId(opLeadSealSheetId));
    }

    /**
     * 新增铅封单
     */
//    @PreAuthorize("@ss.hasPermi('leadsealsheet:sheet:add')")
    @Log(title = "铅封单", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody OpLeadSealSheet opLeadSealSheet)
    {
        return toAjax(opLeadSealSheetService.insertOpLeadSealSheet(opLeadSealSheet));
    }

    /**
     * 修改铅封单
     */
//    @PreAuthorize("@ss.hasPermi('leadsealsheet:sheet:edit')")
    @Log(title = "铅封单", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody OpLeadSealSheet opLeadSealSheet)
    {
        return toAjax(opLeadSealSheetService.updateOpLeadSealSheet(opLeadSealSheet));
    }

    /**
     * 审核铅封单
     */
//    @PreAuthorize("@ss.hasPermi('leadsealsheet:sheet:edit')")
    @Log(title = "审核铅封单", businessType = BusinessType.UPDATE)
    @PutMapping("/audit")
    public AjaxResult audit(@RequestBody String opLeadSealSheetId)
    {
        return toAjax(opLeadSealSheetService.audit(opLeadSealSheetId));
    }

    /**
     * 手动推送奶源
     */
    @Log(title = "铅封单", businessType = BusinessType.UPDATE)
    @PutMapping("/pushMilkSource")
    public AjaxResult pushMilkSource(@RequestBody String opLeadSealSheetId) {
        return toAjax(opLeadSealSheetService.pushMilkSource(opLeadSealSheetId));
    }
}
