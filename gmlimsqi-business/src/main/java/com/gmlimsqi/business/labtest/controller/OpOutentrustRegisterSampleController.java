package com.gmlimsqi.business.labtest.controller;

import com.gmlimsqi.business.labtest.domain.OpOutentrustRegisterSample;
import com.gmlimsqi.business.labtest.service.IOpOutentrustRegisterSampleService;
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
 * 外部委托检测单样品子Controller
 *
 * @author wgq
 * @date 2025-09-17
 */
@RestController
@RequestMapping("/labtest/sample")
public class OpOutentrustRegisterSampleController extends BaseController
{
    @Autowired
    private IOpOutentrustRegisterSampleService opOutentrustRegisterSampleService;

    /**
     * 查询外部委托检测单样品子列表
     */
    //@PreAuthorize("@ss.hasPermi('labtest:sample:list')")
    @GetMapping("/list")
    public TableDataInfo list(OpOutentrustRegisterSample opOutentrustRegisterSample)
    {
        startPage();
        List<OpOutentrustRegisterSample> list = opOutentrustRegisterSampleService.selectOpOutentrustRegisterSampleList(opOutentrustRegisterSample);
        return getDataTable(list);
    }

    /**
     * 导出外部委托检测单样品子列表
     */
    //@PreAuthorize("@ss.hasPermi('labtest:sample:export')")
    @Log(title = "外部委托检测单样品子", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, OpOutentrustRegisterSample opOutentrustRegisterSample)
    {
        List<OpOutentrustRegisterSample> list = opOutentrustRegisterSampleService.selectOpOutentrustRegisterSampleList(opOutentrustRegisterSample);
        ExcelUtil<OpOutentrustRegisterSample> util = new ExcelUtil<OpOutentrustRegisterSample>(OpOutentrustRegisterSample.class);
        util.exportExcel(response, list, "外部委托检测单样品子数据");
    }

    /**
     * 获取外部委托检测单样品子详细信息
     */
    //@PreAuthorize("@ss.hasPermi('labtest:sample:query')")
    @GetMapping(value = "/{outentrustRegisterSampleId}")
    public AjaxResult getInfo(@PathVariable("outentrustRegisterSampleId") String outentrustRegisterSampleId)
    {
        return success(opOutentrustRegisterSampleService.selectOpOutentrustRegisterSampleByOutentrustRegisterSampleId(outentrustRegisterSampleId));
    }

    /**
     * 新增外部委托检测单样品子
     */
    //@PreAuthorize("@ss.hasPermi('labtest:sample:add')")
    @Log(title = "外部委托检测单样品子", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody OpOutentrustRegisterSample opOutentrustRegisterSample)
    {
        return toAjax(opOutentrustRegisterSampleService.insertOpOutentrustRegisterSample(opOutentrustRegisterSample));
    }

    /**
     * 修改外部委托检测单样品子
     */
    //@PreAuthorize("@ss.hasPermi('labtest:sample:edit')")
    @Log(title = "外部委托检测单样品子", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody OpOutentrustRegisterSample opOutentrustRegisterSample)
    {
        return toAjax(opOutentrustRegisterSampleService.updateOpOutentrustRegisterSample(opOutentrustRegisterSample));
    }

    /**
     * 删除外部委托检测单样品子
     */
    //@PreAuthorize("@ss.hasPermi('labtest:sample:remove')")
    @Log(title = "外部委托检测单样品子", businessType = BusinessType.DELETE)
    @DeleteMapping("/{outentrustRegisterSampleIds}")
    public AjaxResult remove(@PathVariable String[] outentrustRegisterSampleIds)
    {
        return toAjax(opOutentrustRegisterSampleService.deleteOpOutentrustRegisterSampleByOutentrustRegisterSampleIds(outentrustRegisterSampleIds));
    }
}
