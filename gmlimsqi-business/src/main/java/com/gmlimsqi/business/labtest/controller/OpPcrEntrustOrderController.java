package com.gmlimsqi.business.labtest.controller;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import cn.hutool.core.collection.CollectionUtil;
import com.gmlimsqi.business.labtest.dto.OpJczxTestTaskDto;
import com.gmlimsqi.business.util.CodeGeneratorUtil;
import com.gmlimsqi.common.exception.BusinessException;
import com.gmlimsqi.common.utils.StringUtils;
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
import com.gmlimsqi.business.labtest.domain.OpPcrEntrustOrder;
import com.gmlimsqi.business.labtest.service.IOpPcrEntrustOrderService;
import com.gmlimsqi.common.utils.poi.ExcelUtil;
import com.gmlimsqi.common.core.page.TableDataInfo;

/**
 * PCR样品委托单Controller
 * 
 * @author hhy
 * @date 2025-09-17
 */
@RestController
@RequestMapping("/labtest/pcrEntrustOrder")
public class OpPcrEntrustOrderController extends BaseController
{
    @Autowired
    private IOpPcrEntrustOrderService opPcrEntrustOrderService;

    /**
     * 查询PCR样品委托单列表
     */
    //@PreAuthorize("@ss.hasPermi('labtest:pcrEntrustOrder:list')")
    @GetMapping("/list")
    public TableDataInfo list(OpPcrEntrustOrder opPcrEntrustOrder)
    {
        startPage();
        List<OpPcrEntrustOrder> list = opPcrEntrustOrderService.selectOpPcrEntrustOrderList(opPcrEntrustOrder);
        return getDataTable(list);
    }

    /**
     * 导出PCR样品委托单列表
     */
    //@PreAuthorize("@ss.hasPermi('labtest:pcrEntrustOrder:export')")
    @Log(title = "PCR样品委托单", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, OpPcrEntrustOrder opPcrEntrustOrder)
    {
        List<OpPcrEntrustOrder> list = opPcrEntrustOrderService.selectOpPcrEntrustOrderList(opPcrEntrustOrder);
        ExcelUtil<OpPcrEntrustOrder> util = new ExcelUtil<OpPcrEntrustOrder>(OpPcrEntrustOrder.class);
        util.exportExcel(response, list, "PCR样品委托单数据");
    }

    /**
     * 获取PCR样品委托单详细信息
     */
    //@PreAuthorize("@ss.hasPermi('labtest:pcrEntrustOrder:query')")
    @GetMapping(value = "/{opPcrEntrustOrderId}")
    public AjaxResult getInfo(@PathVariable("opPcrEntrustOrderId") String opPcrEntrustOrderId)
    {
        return success(opPcrEntrustOrderService.selectOpPcrEntrustOrderByOpPcrEntrustOrderId(opPcrEntrustOrderId));
    }

    /**
     * 新增PCR样品委托单
     */
    //@PreAuthorize("@ss.hasPermi('labtest:pcrEntrustOrder:add')")
    @Log(title = "PCR样品委托单", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody OpPcrEntrustOrder opPcrEntrustOrder) throws BusinessException {
        if(CollectionUtil.isEmpty(opPcrEntrustOrder.getSampleList())){
            return AjaxResult.error("请填写样本");
        }
        return toAjax(opPcrEntrustOrderService.insertOpPcrEntrustOrder(opPcrEntrustOrder));
    }

    /**
     * 修改PCR样品委托单
     */
   // @PreAuthorize("@ss.hasPermi('labtest:pcrEntrustOrder:edit')")
    @Log(title = "PCR样品委托单", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody OpPcrEntrustOrder opPcrEntrustOrder)
    {
        if(CollectionUtil.isEmpty(opPcrEntrustOrder.getSampleList())){
            return AjaxResult.error("请填写样本");
        }
        //TODO 如果委托单状态不是待受理，则不允许更新

        return toAjax(opPcrEntrustOrderService.updateOpPcrEntrustOrder(opPcrEntrustOrder));
    }

    /**
     * 下载导入模板
     */
    @PostMapping("/downloadImportModel")
    public void downloadImportModel(HttpServletResponse response) {
        try {
            opPcrEntrustOrderService.downloadImportModel(response);
        } catch (Exception e) {
            logger.error("下载导入模板失败", e);
            throw new RuntimeException("下载导入模板失败");
        }
    }

    /**
     * 撤回委托单（将待受理状态变回待提交）
     */
    @Log(title = "饲料样品委托单", businessType = BusinessType.UPDATE)
    @GetMapping("/withdraw/{opPcrEntrustOrderId}")
    public AjaxResult withdraw(@PathVariable("opPcrEntrustOrderId") String opPcrEntrustOrderId)
    {
        opPcrEntrustOrderService.withdrawOrder(opPcrEntrustOrderId);
        return success();
    }
}
