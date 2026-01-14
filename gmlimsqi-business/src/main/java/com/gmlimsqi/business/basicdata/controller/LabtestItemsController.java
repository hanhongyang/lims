package com.gmlimsqi.business.basicdata.controller;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

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
import com.gmlimsqi.business.basicdata.domain.LabtestItems;
import com.gmlimsqi.business.basicdata.service.ILabtestItemsService;
import com.gmlimsqi.common.utils.poi.ExcelUtil;
import com.gmlimsqi.common.core.page.TableDataInfo;

/**
 * 检测项目Controller
 * 
 * @author hhy
 * @date 2025-08-05
 */
@RestController
@RequestMapping("/basicdata/labTestItems")
public class LabtestItemsController extends BaseController
{
    @Autowired
    private ILabtestItemsService bsLabtestItemsService;

    /**
     * 查询检测项目列表
     */
    @PreAuthorize("@ss.hasPermi('basicdata:items:list')")
    @GetMapping("/list")
    public TableDataInfo list(LabtestItems labtestItems)
    {
        startPage();
        List<LabtestItems> list = bsLabtestItemsService.selectBsLabtestItemsList(labtestItems);
        return getDataTable(list);
    }

    /**
     * 导出检测项目列表
     */
    @PreAuthorize("@ss.hasPermi('basicdata:items:export')")
    @Log(title = "检测项目", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, LabtestItems labtestItems)
    {
        List<LabtestItems> list = bsLabtestItemsService.selectBsLabtestItemsList(labtestItems);
        ExcelUtil<LabtestItems> util = new ExcelUtil<LabtestItems>(LabtestItems.class);
        util.exportExcel(response, list, "检测项目数据");
    }

    /**
     * 获取检测项目详细信息
     */
    @PreAuthorize("@ss.hasPermi('basicdata:items:query')")
    @GetMapping(value = "/{LabtestItemsId}")
    public AjaxResult getInfo(@PathVariable("LabtestItemsId") String LabtestItemsId)
    {
        return success(bsLabtestItemsService.selectBsLabtestItemsByLabtestItemsId(LabtestItemsId));
    }

    /**
     * 新增检测项目
     */
    @PreAuthorize("@ss.hasPermi('basicdata:items:add')")
    @Log(title = "检测项目", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody LabtestItems labtestItems)
    {
        return toAjax(bsLabtestItemsService.insertBsLabtestItems(labtestItems));
    }

    /**
     * 修改检测项目
     */
    @PreAuthorize("@ss.hasPermi('basicdata:items:edit')")
    @Log(title = "检测项目", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody LabtestItems labtestItems)
    {
        return toAjax(bsLabtestItemsService.updateBsLabtestItems(labtestItems));
    }

    /**
     * 修改启用状态
     */
    @PreAuthorize("@ss.hasPermi('basicdata:items:edit')")
    @Log(title = "检测项目", businessType = BusinessType.UPDATE)
    @PutMapping("/editEnable")
    public AjaxResult editEnable(@RequestBody LabtestItems labtestItems)
    {
        if(StringUtils.isEmpty(labtestItems.getLabtestItemsId()) || StringUtils.isEmpty(labtestItems.getIsEnable())){
            return error("缺少参数");
        }
        return toAjax(bsLabtestItemsService.updateEnableById(labtestItems));
    }

    /**
     * 删除检测项目
     */
    @PreAuthorize("@ss.hasPermi('basicdata:items:remove')")
    @Log(title = "检测项目", businessType = BusinessType.DELETE)
	@DeleteMapping("/{LabtestItemsIds}")
    public AjaxResult remove(@PathVariable String[] LabtestItemsIds)
    {
        return toAjax(bsLabtestItemsService.deleteBsLabtestItemsByLabtestItemsIds(LabtestItemsIds));
    }


}
