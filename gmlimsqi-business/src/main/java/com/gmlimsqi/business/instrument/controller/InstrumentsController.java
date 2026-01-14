package com.gmlimsqi.business.instrument.controller;

import java.util.List;
import javax.servlet.http.HttpServletResponse;
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
import com.gmlimsqi.business.instrument.domain.Instruments;
import com.gmlimsqi.business.instrument.service.IInstrumentsService;
import com.gmlimsqi.common.utils.poi.ExcelUtil;
import com.gmlimsqi.common.core.page.TableDataInfo;

/**
 * 设备档案Controller
 * 
 * @author ruoyi
 * @date 2025-08-06
 */
@RestController
@RequestMapping("/instrument/instruments")
public class InstrumentsController extends BaseController
{
    @Autowired
    private IInstrumentsService bsInstrumentsService;

    /**
     * 查询设备档案列表
     */
    @PreAuthorize("@ss.hasPermi('instrument:instruments:list')")
    @GetMapping("/list")
    public TableDataInfo list(Instruments instruments)
    {
        startPage();
        List<Instruments> list = bsInstrumentsService.selectBsInstrumentsList(instruments);
        return getDataTable(list);
    }

    /**
     * 查询设备档案列表(不分页)
     * @param instruments 查询条件
     * @return 设备列表
     */
    @PreAuthorize("@ss.hasPermi('instrument:instruments:list')")
    @GetMapping("/list/all")
    public AjaxResult listAll(Instruments instruments)
    {
        instruments.setIsEnable("1");
        List<Instruments> list = bsInstrumentsService.selectBsInstrumentsList(instruments);
        return AjaxResult.success(list);
    }

    /**
     * 导出设备档案列表
     */
    @PreAuthorize("@ss.hasPermi('instrument:instruments:export')")
    @Log(title = "设备档案", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, Instruments instruments)
    {
        List<Instruments> list = bsInstrumentsService.selectBsInstrumentsList(instruments);
        ExcelUtil<Instruments> util = new ExcelUtil<Instruments>(Instruments.class);
        util.exportExcel(response, list, "设备档案数据");
    }

    /**
     * 获取设备档案详细信息
     */
    @PreAuthorize("@ss.hasPermi('instrument:instruments:query')")
    @GetMapping(value = "/{bsInstrumentsId}")
    public AjaxResult getInfo(@PathVariable("bsInstrumentsId") String bsInstrumentsId)
    {
        return success(bsInstrumentsService.selectBsInstrumentsByBsInstrumentsId(bsInstrumentsId));
    }

    /**
     * 新增设备档案
     */
    @PreAuthorize("@ss.hasPermi('instrument:instruments:add')")
    @Log(title = "设备档案", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Instruments instruments)
    {
        return toAjax(bsInstrumentsService.insertBsInstruments(instruments));
    }

    /**
     * 修改设备档案
     */
    @PreAuthorize("@ss.hasPermi('instrument:instruments:edit')")
    @Log(title = "设备档案", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Instruments instruments)
    {
        return toAjax(bsInstrumentsService.updateBsInstruments(instruments));
    }

    /**
     * 删除设备档案
     */
    @PreAuthorize("@ss.hasPermi('instrument:instruments:remove')")
    @Log(title = "设备档案", businessType = BusinessType.DELETE)
	@DeleteMapping("/{bsInstrumentsIds}")
    public AjaxResult remove(@PathVariable String[] bsInstrumentsIds)
    {
        return toAjax(bsInstrumentsService.deleteBsInstrumentsByBsInstrumentsIds(bsInstrumentsIds));
    }
}
