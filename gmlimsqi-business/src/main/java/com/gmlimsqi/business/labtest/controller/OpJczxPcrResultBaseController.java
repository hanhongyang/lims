package com.gmlimsqi.business.labtest.controller;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import cn.hutool.core.collection.CollectionUtil;
import com.gmlimsqi.business.labtest.domain.OpJczxPcrResultInfo;
import com.gmlimsqi.business.labtest.domain.OpPcrEntrustOrderItem;
import com.gmlimsqi.business.labtest.domain.OpPcrEntrustOrderSample;
import com.gmlimsqi.business.labtest.dto.OpJczxPcrResultDto;
import com.gmlimsqi.business.labtest.service.IOpPcrEntrustOrderItemService;
import com.gmlimsqi.common.utils.StringUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.gmlimsqi.common.annotation.Log;
import com.gmlimsqi.common.core.controller.BaseController;
import com.gmlimsqi.common.core.domain.AjaxResult;
import com.gmlimsqi.common.enums.BusinessType;
import com.gmlimsqi.business.labtest.domain.OpJczxPcrResultBase;
import com.gmlimsqi.business.labtest.service.IOpJczxPcrResultBaseService;
import com.gmlimsqi.common.utils.poi.ExcelUtil;
import com.gmlimsqi.common.core.page.TableDataInfo;
import org.springframework.web.multipart.MultipartFile;

/**
 * 样品化验PCRController
 * 
 * @author hhy
 * @date 2025-10-13
 */
@RestController
@RequestMapping("/labtest/jczxPcrResult")
public class OpJczxPcrResultBaseController extends BaseController
{
    @Autowired
    private IOpJczxPcrResultBaseService opJczxPcrResultBaseService;
    @Autowired
    private IOpPcrEntrustOrderItemService opPcrEntrustOrderItemService;

    /**
     * 查询样品化验PCR列表
     */
    //@PreAuthoriz("@ss.hasPermi('labtest:jczxPcrResult:list')")
    @GetMapping("/list")
    public TableDataInfo list(OpJczxPcrResultBase opJczxPcrResultBase)
    {
        startPage();
        List<OpJczxPcrResultBase> list = opJczxPcrResultBaseService.selectOpJczxPcrResultBaseList(opJczxPcrResultBase);
        return getDataTable(list);
    }
    /**
     * 查询样品化验PCR列表
     */
    //@PreAuthoriz("@ss.hasPermi('labtest:jczxPcrResult:query')")
    @GetMapping("/getBaseByResultNo")
    public AjaxResult getBaseByResultNo(@RequestParam("resultNo") String resultNo )
    {
        List<OpPcrEntrustOrderItem> list= opPcrEntrustOrderItemService.getBaseByResultNo(resultNo);
        return success(list);
    }

    /**
     * 导出样品化验PCR列表
     */
    //@PreAuthoriz("@ss.hasPermi('labtest:jczxPcrResult:export')")
    @Log(title = "样品化验PCR", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, OpJczxPcrResultBase opJczxPcrResultBase)
    {
        List<OpJczxPcrResultBase> list = opJczxPcrResultBaseService.selectOpJczxPcrResultBaseList(opJczxPcrResultBase);
        ExcelUtil<OpJczxPcrResultBase> util = new ExcelUtil<OpJczxPcrResultBase>(OpJczxPcrResultBase.class);
        util.exportExcel(response, list, "样品化验PCR数据");
    }
    /**
     * 【新】审核项目结果（更新备注和流转状态）
     */
    //@PreAuthoriz("@ss.hasPermi('labtest:jczxPcrResult:examine')")
    @Log(title = "PCR结果项目审核", businessType = BusinessType.UPDATE)
    @PostMapping("/examineBase") // Matches FE's /examineBase
    public AjaxResult examinePcrResultBase(@RequestBody OpJczxPcrResultDto param)
    {
        if (CollectionUtil.isEmpty(param.getTestItem())) {
            return AjaxResult.error("未接收到审核项目数据");
        }
        return toAjax(opJczxPcrResultBaseService.examinePcrResultBase(param.getTestItem(),param.getExaminePassFlag(),param.getResultNo()));
    }

    //@PreAuthoriz("@ss.hasPermi('labtest:jczxPcrResult:examine')")
    @PostMapping("/cancelExamine")
    public AjaxResult cancelExamine(@RequestBody OpJczxPcrResultDto dto)
    {
        String resultNo = dto.getResultNo();

        if (StringUtils.isAnyBlank(resultNo)) {
            return AjaxResult.error("缺少必要的参数：委托单号或项目类型");
        }

        try {
            int rows = opJczxPcrResultBaseService.cancelExaminePcrResult(resultNo);
            return toAjax(rows);
        } catch (RuntimeException e) {
            return AjaxResult.error(e.getMessage());
        }

    }
    /**
     * 获取样品化验PCR详细信息
     */
    //@PreAuthoriz("@ss.hasPermi('labtest:jczxPcrResult:query')")
    @GetMapping(value = "/{opJczxPcrResultBaseId}")
    public AjaxResult getInfo(@PathVariable("opJczxPcrResultBaseId") String opJczxPcrResultBaseId)
    {
        return success(opJczxPcrResultBaseService.selectOpJczxPcrResultBaseByOpJczxPcrResultBaseId(opJczxPcrResultBaseId));
    }

    /**
     * 新增样品化验PCR
     */
    //@PreAuthoriz("@ss.hasPermi('labtest:jczxPcrResult:add')")
    @Log(title = "样品化验PCR", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody OpJczxPcrResultBase opJczxPcrResultBase)
    {
        return toAjax(opJczxPcrResultBaseService.insertOpJczxPcrResultBase(opJczxPcrResultBase));
    }

    /**
     * 修改样品化验PCR
     */
    //@PreAuthoriz("@ss.hasPermi('labtest:jczxPcrResult:edit')")
    @Log(title = "样品化验PCR", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody OpJczxPcrResultBase opJczxPcrResultBase)
    {
        return toAjax(opJczxPcrResultBaseService.updateOpJczxPcrResultBase(opJczxPcrResultBase));
    }

    /**
     * 导入PCR检测结果数据
     */
    //@PreAuthoriz("@ss.hasPermi('labtest:base:import')")
    @Log(title = "PCR检测结果", businessType = BusinessType.IMPORT)
    @PostMapping("/importData")
    public AjaxResult importData(MultipartFile file) throws Exception
    {
        String message = opJczxPcrResultBaseService.importPcrResult(file);
        return AjaxResult.success(message);
    }



}
