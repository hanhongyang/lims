package com.gmlimsqi.business.basicdata.controller;

import com.gmlimsqi.business.basicdata.domain.BaseInvbill;
import com.gmlimsqi.business.basicdata.domain.BusinessInvbill;
import com.gmlimsqi.business.basicdata.service.IBaseInvbillService;
import com.gmlimsqi.business.basicdata.service.IBusinessInvbillService;
import com.gmlimsqi.common.annotation.Anonymous;
import com.gmlimsqi.common.core.controller.BaseController;
import com.gmlimsqi.common.core.domain.AjaxResult;
import com.gmlimsqi.common.core.page.TableDataInfo;
import com.gmlimsqi.sap.accept.service.MaterialService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;

/**
 * 物料档案
 */
@Slf4j
@RestController
@RequestMapping("/business/invbill")
public class InvbillController extends BaseController {
    
    @Autowired
    private IBaseInvbillService baseInvbillService;
    @Autowired
    private IBusinessInvbillService invbillService;
    @Autowired
    private MaterialService materialService;
    
    /**
     * 查询物料档案列表
     */
    @GetMapping("/listInfo")
    public TableDataInfo listInfo(BusinessInvbill baseInvbill) {
        startPage();
        List<BusinessInvbill> list =
                invbillService.selectBusinessInvbillList(baseInvbill);
        return getDataTable(list);
    }
    
    /**
     * 查询物料档案列表
     */
    @GetMapping("/list")
    public TableDataInfo list(BaseInvbill baseInvbill) {
        startPage();
        List<BaseInvbill> list =
                baseInvbillService.selectInvbillList(baseInvbill);
        return getDataTable(list);
    }

    /**
     * 查询物料档案列表（查询SAP系统中的）
     */
     @GetMapping("/listSap")
    public TableDataInfo listSap(BaseInvbill baseInvbill) {
        startPage();
        List<BaseInvbill> list =
                baseInvbillService.selectInvbillListSap(baseInvbill);
        return getDataTable(list);
    }

    /**
     * 同步物料档案
     */
    @GetMapping("/syncBaseInvbill")
    @Anonymous
    public AjaxResult syncBaseInvbill() {
        log.info("同步物料档案");
        try {
            materialService.syncMateical();
        } catch (ParseException e) {
            return AjaxResult.error(e.getMessage());
        }
        return AjaxResult.success();
    }
    /**
     * 获取检测项目详细信息
     */
    //@PreAuthorize("@ss.hasPermi('basicdata:labTestItems:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") String id)
    {
        return success(baseInvbillService.selectInvbillById(id));
    }
    /**
     * 修改物料档案
     *
     * @return {@link AjaxResult}
     */
    @PutMapping
    public AjaxResult updateInvbill(
            @RequestBody BusinessInvbill baseInvbill)
    {
        return toAjax(baseInvbillService.updateInvbill(baseInvbill));
    }

    /**
     * 手动新增物料档案
     */
     @PostMapping
    public AjaxResult addInvbill(
            @RequestBody BusinessInvbill baseInvbill)
    {
        return toAjax(invbillService.insertInvbill(baseInvbill));
    }

}
