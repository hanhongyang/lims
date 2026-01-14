package com.gmlimsqi.business.bsweighbridge.controller;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.gmlimsqi.business.bsweighbridge.domain.BsWeighBridge;
import com.gmlimsqi.business.bsweighbridge.service.IBsWeighBridgeService;
import com.gmlimsqi.common.annotation.Anonymous;
import lombok.extern.slf4j.Slf4j;
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
 * 过磅单Controller
 * 
 * @author hhy
 * @date 2025-11-13
 */
@RestController
@RequestMapping("/weighBridge")
@Slf4j
public class BsWeighBridgeController extends BaseController
{
    @Autowired
    private IBsWeighBridgeService bsWeighBridgeService;

    /**
     * 查询过磅单列表
     */
    @GetMapping("/list")
    public TableDataInfo list(BsWeighBridge bsWeighBridge)
    {
        startPage();
        List<BsWeighBridge> list = bsWeighBridgeService.selectBsWeighBridgeList(bsWeighBridge);
        return getDataTable(list);
    }

    /**
     * 导出过磅单列表
     */
    @Log(title = "过磅单", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, BsWeighBridge bsWeighBridge)
    {
        List<BsWeighBridge> list = bsWeighBridgeService.selectBsWeighBridgeList(bsWeighBridge);
        ExcelUtil<BsWeighBridge> util = new ExcelUtil<BsWeighBridge>(BsWeighBridge.class);
        util.exportExcel(response, list, "过磅单数据");
    }

    /**
     * 获取过磅单详细信息
     */
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") String id)
    {
        return success(bsWeighBridgeService.selectBsWeighBridgeById(id));
    }

    /**
     * 新增过磅单
     */
    @Log(title = "过磅单", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody BsWeighBridge bsWeighBridge)
    {
        return toAjax(bsWeighBridgeService.insertBsWeighBridge(bsWeighBridge));
    }

    /**
     * 修改过磅单
     */
    @Log(title = "过磅单", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody BsWeighBridge bsWeighBridge)
    {
        return toAjax(bsWeighBridgeService.updateBsWeighBridge(bsWeighBridge));
    }

    /**
     * 地磅上传过磅单
     */
    @Anonymous
    @Log(title = "地磅上传过磅单", register = false, businessType =
            BusinessType.WEIGHT)
    @PostMapping("/upload")
    public AjaxResult upload(@RequestBody BsWeighBridge bsWeighBridge)
    {
        log.info("上传过磅信息" + bsWeighBridge);
        return toAjax(bsWeighBridgeService.uploadBsWeighBridge(bsWeighBridge));
    }


}
