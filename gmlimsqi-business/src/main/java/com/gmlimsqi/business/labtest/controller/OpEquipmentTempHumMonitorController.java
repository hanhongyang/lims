package com.gmlimsqi.business.labtest.controller;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.gmlimsqi.business.labtest.domain.OpDeviceTempHumRecord;
import com.gmlimsqi.business.labtest.domain.OpEquipmentTempHumMonitor;
import com.gmlimsqi.business.labtest.service.IOpEquipmentTempHumMonitorService;
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
import com.gmlimsqi.common.utils.poi.ExcelUtil;
import com.gmlimsqi.common.core.page.TableDataInfo;

/**
 * 设备温湿度监控Controller
 * 
 * @author hhy
 * @date 2025-10-26
 */
@RestController
@RequestMapping("/basicdata/monitor")
public class OpEquipmentTempHumMonitorController extends BaseController {
    @Autowired
    private IOpEquipmentTempHumMonitorService opEquipmentTempHumMonitorService;

    /**
     * 查询设备温湿度监控列表
     */
    @PreAuthorize("@ss.hasPermi('basicdata:monitor:list')")
    @GetMapping("/list")
    public TableDataInfo list(OpEquipmentTempHumMonitor opEquipmentTempHumMonitor) {
        startPage();
        List<OpEquipmentTempHumMonitor> list = opEquipmentTempHumMonitorService.selectOpEquipmentTempHumMonitorList(opEquipmentTempHumMonitor);
        return getDataTable(list);
    }

    /**
     * 导出设备温湿度监控列表
     */
    @PreAuthorize("@ss.hasPermi('basicdata:monitor:export')")
    @Log(title = "设备温湿度监控", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, OpEquipmentTempHumMonitor opEquipmentTempHumMonitor) {
        List<OpEquipmentTempHumMonitor> list = opEquipmentTempHumMonitorService.selectOpEquipmentTempHumMonitorList(opEquipmentTempHumMonitor);
        ExcelUtil<OpEquipmentTempHumMonitor> util = new ExcelUtil<OpEquipmentTempHumMonitor>(OpEquipmentTempHumMonitor.class);
        util.exportExcel(response, list, "设备温湿度监控数据");
    }

    /**
     * 获取设备温湿度监控详细信息
     */
    @PreAuthorize("@ss.hasPermi('basicdata:monitor:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") String id) {
        return success(opEquipmentTempHumMonitorService.selectOpEquipmentTempHumMonitorById(id));
    }

    /**
     * 新增设备温湿度监控
     */
    @PreAuthorize("@ss.hasPermi('basicdata:monitor:add')")
    @Log(title = "设备温湿度监控", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody OpEquipmentTempHumMonitor opEquipmentTempHumMonitor) {
        return toAjax(opEquipmentTempHumMonitorService.insertOpEquipmentTempHumMonitor(opEquipmentTempHumMonitor));
    }

    /**
     * 修改设备温湿度监控
     */
    @PreAuthorize("@ss.hasPermi('basicdata:monitor:edit')")
    @Log(title = "设备温湿度监控", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody OpEquipmentTempHumMonitor opEquipmentTempHumMonitor) {
        return toAjax(opEquipmentTempHumMonitorService.updateOpEquipmentTempHumMonitor(opEquipmentTempHumMonitor));
    }

    /**
     * 删除设备温湿度监控
     */
    @PreAuthorize("@ss.hasPermi('basicdata:monitor:remove')")
    @Log(title = "设备温湿度监控", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable String[] ids) {
        return toAjax(opEquipmentTempHumMonitorService.deleteOpEquipmentTempHumMonitorByIds(ids));
    }

    /**
     * 查询设备温湿度监控修改记录列表
     */
    @PreAuthorize("@ss.hasPermi('basicdata:monitor:list')")
    @GetMapping("/editRecord/list")
    public TableDataInfo editRecordList(String parentId) {
        startPage();
        List<OpDeviceTempHumRecord> list = opEquipmentTempHumMonitorService.selectOpDeviceTempHumRecordList(parentId);
        return getDataTable(list);
    }

    /**
     * 导出设备温湿度监控修改记录列表
     */
    @PreAuthorize("@ss.hasPermi('basicdata:monitor:export')")
    @Log(title = "设备温湿度监控修改记录", businessType = BusinessType.EXPORT)
    @PostMapping("/editRecord/export")
    public void exportEditRecord(HttpServletResponse response, String parentId) {
        List<OpDeviceTempHumRecord> list = opEquipmentTempHumMonitorService.selectOpDeviceTempHumRecordList(parentId);
        ExcelUtil<OpDeviceTempHumRecord> util = new ExcelUtil<OpDeviceTempHumRecord>(OpDeviceTempHumRecord.class);
        util.exportExcel(response, list, "设备温湿度监控修改记录数据");
    }

}
