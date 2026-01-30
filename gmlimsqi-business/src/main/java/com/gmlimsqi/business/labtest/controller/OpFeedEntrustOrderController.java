package com.gmlimsqi.business.labtest.controller;

import cn.hutool.core.collection.CollectionUtil;
import com.gmlimsqi.business.labtest.domain.OpFeedEntrustOrder;
import com.gmlimsqi.business.labtest.service.IOpFeedEntrustOrderService;
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
 * 饲料样品委托单Controller
 * 
 * @author hhy
 * @date 2025-09-13
 */
@RestController
@RequestMapping("/labtest/feedEntrustOrder")
public class OpFeedEntrustOrderController extends BaseController
{
    @Autowired
    private IOpFeedEntrustOrderService opFeedEntrustOrderService;

    /**
     * 查询饲料样品委托单列表
     */
    //@PreAuthorize("@ss.hasPermi('labtest:feedEntrustOrder:list')")
    @GetMapping("/list")
    public TableDataInfo list(OpFeedEntrustOrder opFeedEntrustOrder)
    {
        startPage();
        List<OpFeedEntrustOrder> list = opFeedEntrustOrderService.selectOpFeedEntrustOrderList(opFeedEntrustOrder);
        return getDataTable(list);
    }

    /**
     * 导出饲料样品委托单列表
     */
    //@PreAuthorize("@ss.hasPermi('labtest:feedEntrustOrder:export')")
    @Log(title = "饲料样品委托单", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, OpFeedEntrustOrder opFeedEntrustOrder)
    {
        List<OpFeedEntrustOrder> list = opFeedEntrustOrderService.selectOpFeedEntrustOrderList(opFeedEntrustOrder);
        ExcelUtil<OpFeedEntrustOrder> util = new ExcelUtil<OpFeedEntrustOrder>(OpFeedEntrustOrder.class);
        util.exportExcel(response, list, "饲料样品委托单数据");
    }

    /**
     * 获取饲料样品委托单详细信息
     */
    //@PreAuthorize("@ss.hasPermi('labtest:feedEntrustOrder:query')")
    @GetMapping(value = "/{opFeedEntrustOrderId}")
    public AjaxResult getInfo(@PathVariable("opFeedEntrustOrderId") String opFeedEntrustOrderId)
    {
        return success(opFeedEntrustOrderService.selectOpFeedEntrustOrderByOpFeedEntrustOrderId(opFeedEntrustOrderId));
    }

    /**
     * 获取饲料样品委托单打印（化学法）详细信息
     */
    //@PreAuthorize("@ss.hasPermi('labtest:feedEntrustOrder:print')")
    @GetMapping(value = "/print/{opFeedEntrustOrderId}")
    public AjaxResult getPrintInfo(@PathVariable("opFeedEntrustOrderId") String opFeedEntrustOrderId)
    {
        return success(opFeedEntrustOrderService.selectPrintOpFeedEntrustOrderByOpFeedEntrustOrderId(opFeedEntrustOrderId));
    }



    /**
     * 新增饲料样品委托单
     */
    //@PreAuthorize("@ss.hasPermi('labtest:feedEntrustOrder:add')")
    @Log(title = "饲料样品委托单", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody OpFeedEntrustOrder opFeedEntrustOrder)
    {
        if(CollectionUtil.isEmpty(opFeedEntrustOrder.getSampleList())){
            return AjaxResult.error("请填写样本");
        }
        return toAjax(opFeedEntrustOrderService.insertOpFeedEntrustOrder(opFeedEntrustOrder));
    }

    /**
     * 修改饲料样品委托单
     */
    //@PreAuthorize("@ss.hasPermi('labtest:feedEntrustOrder:edit')")
    @Log(title = "饲料样品委托单", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody OpFeedEntrustOrder opFeedEntrustOrder)
    {
        if(CollectionUtil.isEmpty(opFeedEntrustOrder.getSampleList())){
            return AjaxResult.error("请填写样本");
        }
        return toAjax(opFeedEntrustOrderService.updateOpFeedEntrustOrder3(opFeedEntrustOrder));
    }

    /**
     * 撤回委托单（将待受理状态变回待提交）
     */
    @Log(title = "饲料样品委托单", businessType = BusinessType.UPDATE)
    @GetMapping("/withdraw/{opFeedEntrustOrderId}")
    public AjaxResult withdraw(@PathVariable("opFeedEntrustOrderId") String opFeedEntrustOrderId)
    {
        opFeedEntrustOrderService.withdrawOrder(opFeedEntrustOrderId);
        return success();
    }

    @PostMapping("/jhwItemList")
    public AjaxResult selectJhwItemList(@RequestBody List<String> sampleIdList)
    {
        return success(opFeedEntrustOrderService.selectJhwItemList(sampleIdList));
    }
}
