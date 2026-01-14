package com.gmlimsqi.business.milkfillingorder.controller;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.gmlimsqi.business.milkfillingorder.domain.OpMilkFillingOrder;
import com.gmlimsqi.business.milkfillingorder.service.IOpMilkFillingOrderService;
import com.gmlimsqi.common.utils.StringUtils;
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
 * 装奶单Controller
 *
 * @author hhy
 * @date 2025-11-10
 */
@RestController
@RequestMapping("/milkFilling/order")
public class OpMilkFillingOrderController extends BaseController
{
    @Autowired
    private IOpMilkFillingOrderService opMilkFillingOrderService;

    /**
     * 查询装奶单列表
     */
//    @PreAuthorize("@ss.hasPermi('milkFilling:order:list')")
    @GetMapping("/list")
    public TableDataInfo list(OpMilkFillingOrder opMilkFillingOrder)
    {
        startPage();
        List<OpMilkFillingOrder> list = opMilkFillingOrderService.selectOpMilkFillingOrderList(opMilkFillingOrder);
        return getDataTable(list);
    }

    /**
     * 导出装奶单列表
     */
//    @PreAuthorize("@ss.hasPermi('milkFilling:order:export')")
    @Log(title = "装奶单", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, OpMilkFillingOrder opMilkFillingOrder)
    {
        List<OpMilkFillingOrder> list = opMilkFillingOrderService.selectOpMilkFillingOrderList(opMilkFillingOrder);
        ExcelUtil<OpMilkFillingOrder> util = new ExcelUtil<OpMilkFillingOrder>(OpMilkFillingOrder.class);
        util.exportExcel(response, list, "装奶单数据");
    }

    /**
     * 获取装奶单详细信息
     */
//    @PreAuthorize("@ss.hasPermi('milkFilling:order:query')")
    @GetMapping(value = "/{opMilkFillingOrderId}")
    public AjaxResult getInfo(@PathVariable("opMilkFillingOrderId") String opMilkFillingOrderId)
    {
        return success(opMilkFillingOrderService.selectOpMilkFillingOrderByOpMilkFillingOrderId(opMilkFillingOrderId));
    }

    /**
     * 新增装奶单
     */
//    @PreAuthorize("@ss.hasPermi('milkFilling:order:add')")
    @Log(title = "装奶单", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody OpMilkFillingOrder opMilkFillingOrder)
    {
        String opMilkFillingOrderId = opMilkFillingOrderService.insertOpMilkFillingOrder(opMilkFillingOrder);
        if (StringUtils.isEmpty(opMilkFillingOrderId)) {
            return AjaxResult.error("新增装奶单失败");
        }
        return toAjax(true);
    }

    /**
     * 修改装奶单
     */
//    @PreAuthorize("@ss.hasPermi('milkFilling:order:edit')")
    @Log(title = "装奶单", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody OpMilkFillingOrder opMilkFillingOrder)
    {
        return toAjax(opMilkFillingOrderService.updateOpMilkFillingOrder(opMilkFillingOrder));
    }

    /**
     * 审核装奶单
     */
//    @PreAuthorize("@ss.hasPermi('milkFilling:order:edit')")
    @Log(title = "装奶单", businessType = BusinessType.UPDATE)
    @PutMapping("/audit/{opMilkFillingOrderId}")
    public AjaxResult audit(@PathVariable("opMilkFillingOrderId") String opMilkFillingOrderId)
    {
        return toAjax(opMilkFillingOrderService.audit(opMilkFillingOrderId));
    }

    /**
     * 根据车牌号读取磅单数据
     * @param opMilkFillingOrderId 装奶单主键
     * @return {@link OpMilkFillingOrder}
     */
//    @PreAuthorize("@ss.hasPermi('milkFilling:order:query')")
    @GetMapping("/readByDriverCode")
    public AjaxResult readByDriverCode(String opMilkFillingOrderId)
    {
        return success(opMilkFillingOrderService.readByDriverCode(opMilkFillingOrderId));
    }

    /**
     * 查询未关联磅单的装奶单
     */
    @GetMapping("/unassociated")
    public TableDataInfo unassociated(OpMilkFillingOrder opMilkFillingOrder)
    {
        startPage();
        List<OpMilkFillingOrder> list = opMilkFillingOrderService.selectUnassociatedOpMilkFillingOrderList(opMilkFillingOrder);
        return getDataTable(list);
    }

    /**
     * 装奶单关联磅单
     */
    @Log(title = "装奶单关联磅单", businessType = BusinessType.UPDATE)
    @PutMapping("/associate")
    public AjaxResult associate(OpMilkFillingOrder opMilkFillingOrder)
    {
        return toAjax(opMilkFillingOrderService.associate(opMilkFillingOrder));
    }

    /**
     * 装奶单手动关联磅单
     */
    @Log(title = "装奶单手动关联磅单", businessType = BusinessType.UPDATE)
    @PutMapping("/manualAssociate")
    public AjaxResult manualAssociate(OpMilkFillingOrder opMilkFillingOrder)
    {
        return toAjax(opMilkFillingOrderService.manualAssociate(opMilkFillingOrder));
    }

    /**
     * 装奶单推送奶源系统
     */
    @Log(title = "装奶单推送奶源系统", businessType = BusinessType.UPDATE)
    @PutMapping("/pushMilkSource/{opMilkFillingOrderId}")
    public AjaxResult pushMilkSource(@PathVariable("opMilkFillingOrderId") String opMilkFillingOrderId)
    {
        return toAjax(opMilkFillingOrderService.pushMilkSource(opMilkFillingOrderId));
    }

}
