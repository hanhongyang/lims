package com.gmlimsqi.business.designateddepartmentinspection.controller;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.gmlimsqi.business.designateddepartmentinspection.domin.entity.OpSamplingTestItemDept;
import com.gmlimsqi.business.designateddepartmentinspection.service.IOpSamplingTestItemDeptService;
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
 * 检测项目指定部门检测Controller
 * 
 * @author hhy
 * @date 2025-11-27
 */
@RestController
@RequestMapping("/basicdata/dept")
public class OpSamplingTestItemDeptController extends BaseController
{
    @Autowired
    private IOpSamplingTestItemDeptService opSamplingTestItemDeptService;

    /**
     * 查询检测项目指定部门检测列表
     * @param opSamplingTestItemDept 检测项目指定部门检测
     * @return {@link List<OpSamplingTestItemDept>}
     */
//    @PreAuthorize("@ss.hasPermi('basicdata:dept:list')")
    @GetMapping("/list")
    public TableDataInfo list(OpSamplingTestItemDept opSamplingTestItemDept)
    {
        startPage();
        List<OpSamplingTestItemDept> list = opSamplingTestItemDeptService.selectOpSamplingTestItemDeptList(opSamplingTestItemDept);
        return getDataTable(list);
    }

    /**
     * 导出检测项目指定部门检测列表
     */
//    @PreAuthorize("@ss.hasPermi('basicdata:dept:export')")
    @Log(title = "检测项目指定部门检测", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, OpSamplingTestItemDept opSamplingTestItemDept)
    {
        List<OpSamplingTestItemDept> list = opSamplingTestItemDeptService.selectOpSamplingTestItemDeptList(opSamplingTestItemDept);
        ExcelUtil<OpSamplingTestItemDept> util = new ExcelUtil<OpSamplingTestItemDept>(OpSamplingTestItemDept.class);
        util.exportExcel(response, list, "检测项目指定部门检测数据");
    }

    /**
     * 获取检测项目指定部门检测详细信息
     * @param opSamplingTestItemDeptId 检测项目指定部门检测主键
     * @return {@link OpSamplingTestItemDept}
     */
//    @PreAuthorize("@ss.hasPermi('basicdata:dept:query')")
    @GetMapping(value = "/{opSamplingTestItemDeptId}")
    public AjaxResult getInfo(@PathVariable("opSamplingTestItemDeptId") String opSamplingTestItemDeptId)
    {
        return success(opSamplingTestItemDeptService.selectOpSamplingTestItemDeptByOpSamplingTestItemDeptId(opSamplingTestItemDeptId));
    }

    /**
     * 新增检测项目指定部门检测
     * @param opSamplingTestItemDept 检测项目指定部门检测
     */
//    @PreAuthorize("@ss.hasPermi('basicdata:dept:add')")
    @Log(title = "检测项目指定部门检测", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody OpSamplingTestItemDept opSamplingTestItemDept)
    {
        return toAjax(opSamplingTestItemDeptService.insertOpSamplingTestItemDept(opSamplingTestItemDept));
    }

    /**
     * 修改检测项目指定部门检测
     */
//    @PreAuthorize("@ss.hasPermi('basicdata:dept:edit')")
    @Log(title = "检测项目指定部门检测", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody OpSamplingTestItemDept opSamplingTestItemDept)
    {
        return toAjax(opSamplingTestItemDeptService.updateOpSamplingTestItemDept(opSamplingTestItemDept));
    }


}
