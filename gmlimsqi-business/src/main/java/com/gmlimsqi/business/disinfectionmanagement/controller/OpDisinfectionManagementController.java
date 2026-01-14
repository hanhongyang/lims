package com.gmlimsqi.business.disinfectionmanagement.controller;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.gmlimsqi.common.utils.SecurityUtils;
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
import com.gmlimsqi.business.disinfectionmanagement.domain.OpDisinfectionManagement;
import com.gmlimsqi.business.disinfectionmanagement.service.IOpDisinfectionManagementService;
import com.gmlimsqi.common.utils.poi.ExcelUtil;
import com.gmlimsqi.common.core.page.TableDataInfo;

/**
 * 消毒管理Controller
 * 
 * @author hhy
 * @date 2025-11-06
 */
@RestController
@RequestMapping("/disinfectionmanagement/management")
public class OpDisinfectionManagementController extends BaseController
{
    @Autowired
    private IOpDisinfectionManagementService opDisinfectionManagementService;

    /**
     * 查询消毒管理列表
     */
//    @PreAuthorize("@ss.hasPermi('disinfectionmanagement:management:list')")
    @GetMapping("/list")
    public TableDataInfo list(OpDisinfectionManagement opDisinfectionManagement)
    {
        startPage();
        List<OpDisinfectionManagement> list = opDisinfectionManagementService.selectOpDisinfectionManagementList(opDisinfectionManagement);
        return getDataTable(list);
    }

    /**
     * 导出消毒管理列表
     */
//    @PreAuthorize("@ss.hasPermi('disinfectionmanagement:management:export')")
    @Log(title = "消毒管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, OpDisinfectionManagement opDisinfectionManagement)
    {
        List<OpDisinfectionManagement> list = opDisinfectionManagementService.selectOpDisinfectionManagementList(opDisinfectionManagement);
        ExcelUtil<OpDisinfectionManagement> util = new ExcelUtil<OpDisinfectionManagement>(OpDisinfectionManagement.class);
        util.exportExcel(response, list, "消毒管理数据");
    }

    /**
     * 获取消毒管理详细信息
     */
//    @PreAuthorize("@ss.hasPermi('disinfectionmanagement:management:query')")
    @GetMapping(value = "/{disinfectionManagementId}")
    public AjaxResult getInfo(@PathVariable("disinfectionManagementId") String disinfectionManagementId)
    {
        return success(opDisinfectionManagementService.selectOpDisinfectionManagementByDisinfectionManagementId(disinfectionManagementId));
    }

    /**
     * 获取当前最新的消毒信息
     */
    @GetMapping("/getCurrent")
    public AjaxResult getCurrent() {
        Long deptId = SecurityUtils.getDeptId();
        return success(opDisinfectionManagementService.selectOnePassedByDeptId(deptId));
    }

    /**
     * 新增消毒管理
     */
//    @PreAuthorize("@ss.hasPermi('disinfectionmanagement:management:add')")
    @Log(title = "消毒管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody OpDisinfectionManagement opDisinfectionManagement)
    {
        return toAjax(opDisinfectionManagementService.insertOpDisinfectionManagement(opDisinfectionManagement));
    }

    /**
     * 修改消毒管理
     */
//    @PreAuthorize("@ss.hasPermi('disinfectionmanagement:management:edit')")
    @Log(title = "消毒管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody OpDisinfectionManagement opDisinfectionManagement)
    {
        return toAjax(opDisinfectionManagementService.updateOpDisinfectionManagement(opDisinfectionManagement));
    }

    /**
     * 删除消毒管理
     */
//    @PreAuthorize("@ss.hasPermi('disinfectionmanagement:management:remove')")
    @Log(title = "消毒管理", businessType = BusinessType.DELETE)
	@DeleteMapping("/{disinfectionManagementIds}")
    public AjaxResult remove(@PathVariable String[] disinfectionManagementIds)
    {
        return toAjax(opDisinfectionManagementService.deleteOpDisinfectionManagementByDisinfectionManagementIds(disinfectionManagementIds));
    }
}
