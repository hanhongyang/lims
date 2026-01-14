package com.gmlimsqi.business.labtest.controller;

import com.gmlimsqi.business.labtest.domain.OpOutentrustRegister;
import com.gmlimsqi.business.labtest.service.IOpOutentrustRegisterService;
import com.gmlimsqi.common.annotation.Log;
import com.gmlimsqi.common.core.controller.BaseController;
import com.gmlimsqi.common.core.domain.AjaxResult;
import com.gmlimsqi.common.core.page.TableDataInfo;
import com.gmlimsqi.common.enums.BusinessType;
import com.gmlimsqi.common.utils.poi.ExcelUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 外部委托检测单Controller
 *
 * @author wgq
 * @date 2025-09-17
 */
@RestController
@RequestMapping("/labtest/outEntrustRegister")
public class OpOutentrustRegisterController extends BaseController
{
    @Autowired
    private IOpOutentrustRegisterService opOutentrustRegisterService;

    /**
     * 查询外部委托检测单列表
     */
    //@PreAuthoriz("@ss.hasPermi('labtest:register:list')")
    @GetMapping("/list")
    public TableDataInfo list(OpOutentrustRegister opOutentrustRegister)
    {
        startPage();
        List<OpOutentrustRegister> list = opOutentrustRegisterService.selectOpOutentrustRegisterList(opOutentrustRegister);
        return getDataTable(list);
    }

    /**
     * 导出外部委托检测单列表
     */
    //@PreAuthoriz("@ss.hasPermi('labtest:register:export')")
    @Log(title = "外部委托检测单", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, OpOutentrustRegister opOutentrustRegister)
    {
        List<OpOutentrustRegister> list = opOutentrustRegisterService.selectOpOutentrustRegisterList(opOutentrustRegister);
        ExcelUtil<OpOutentrustRegister> util = new ExcelUtil<OpOutentrustRegister>(OpOutentrustRegister.class);
        util.exportExcel(response, list, "外部委托检测单数据");
    }

    /**
     * 获取外部委托检测单详细信息
     */
    //@PreAuthoriz("@ss.hasPermi('labtest:register:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") String id)
    {
        return success(opOutentrustRegisterService.selectOpOutentrustRegisterById(id));
    }

    /**
     * 新增外部委托检测单
     */
    //@PreAuthoriz("@ss.hasPermi('labtest:register:add')")
    @Log(title = "外部委托检测单", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody OpOutentrustRegister opOutentrustRegister)
    {
        return toAjax(opOutentrustRegisterService.insertOpOutentrustRegister(opOutentrustRegister));
    }

    /**
     * 修改外部委托检测单
     */
    //@PreAuthoriz("@ss.hasPermi('labtest:register:edit')")
    @Log(title = "外部委托检测单", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody OpOutentrustRegister opOutentrustRegister)
    {
        return toAjax(opOutentrustRegisterService.updateOpOutentrustRegister(opOutentrustRegister));
    }

    /**
     * 删除外部委托检测单
     */
    //@PreAuthoriz("@ss.hasPermi('labtest:register:remove')")
    @Log(title = "外部委托检测单", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable String[] ids)
    {
        return toAjax(opOutentrustRegisterService.deleteOpOutentrustRegisterByIds(ids));
    }


    /**
     * 弃审/审核外部委托检测单
     */
    //@PreAuthoriz("@ss.hasPermi('labtest:register:enable')")
    @Log(title = "外部委托检测单", businessType = BusinessType.UPDATE)
    @PutMapping("/enable")
    public AjaxResult enable(@RequestBody OpOutentrustRegister opOutentrustRegister)
    {
        return toAjax(opOutentrustRegisterService.enableOpOutentrustRegister(opOutentrustRegister));
    }



}
