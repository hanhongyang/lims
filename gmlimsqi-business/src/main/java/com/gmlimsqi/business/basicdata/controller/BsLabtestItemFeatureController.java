package com.gmlimsqi.business.basicdata.controller;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.gmlimsqi.business.basicdata.dto.BatchAddItemFeatureDto;
import com.gmlimsqi.business.basicdata.vo.BsLabtestItemFeatureVo;
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
import com.gmlimsqi.business.basicdata.domain.BsLabtestItemFeature;
import com.gmlimsqi.business.basicdata.service.IBsLabtestItemFeatureService;
import com.gmlimsqi.common.utils.poi.ExcelUtil;
import com.gmlimsqi.common.core.page.TableDataInfo;

/**
 * 检测项目特性对应Controller
 *
 * @author hhy
 * @date 2025-09-05
 */
@RestController
@RequestMapping("/basicdata/itemFeature")
public class BsLabtestItemFeatureController extends BaseController
{
    @Autowired
    private IBsLabtestItemFeatureService bsLabtestItemFeatureService;

    /**
     * 查询检测项目特性对应列表
     */
    @PreAuthorize("@ss.hasPermi('basicdata:itemFeature:list')")
    @GetMapping("/list")
    public TableDataInfo list(BsLabtestItemFeature bsLabtestItemFeature)
    {
        startPage();
        List<BsLabtestItemFeatureVo> itemFeatureList = bsLabtestItemFeatureService.selectListGroupByItem(bsLabtestItemFeature);
        TableDataInfo dataTable = getDataTable(itemFeatureList);
        return dataTable;
    }

    /**
     * 导出检测项目特性对应列表
     */
    @PreAuthorize("@ss.hasPermi('basicdata:itemFeature:export')")
    @Log(title = "检测项目特性对应", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, BsLabtestItemFeature bsLabtestItemFeature)
    {
        List<BsLabtestItemFeature> list = bsLabtestItemFeatureService.selectBsLabtestItemFeatureList(bsLabtestItemFeature);
        ExcelUtil<BsLabtestItemFeature> util = new ExcelUtil<BsLabtestItemFeature>(BsLabtestItemFeature.class);
        util.exportExcel(response, list, "检测项目特性对应数据");
    }

    /**
     * 获取检测项目特性对应详细信息
     */
    @PreAuthorize("@ss.hasPermi('basicdata:itemFeature:query')")
    @GetMapping(value = "/{bsLabtestItemFeatureId}")
    public AjaxResult getInfo(@PathVariable("bsLabtestItemFeatureId") String bsLabtestItemFeatureId)
    {
        return success(bsLabtestItemFeatureService.selectBsLabtestItemFeatureByBsLabtestItemFeatureId(bsLabtestItemFeatureId));
    }

    /**
     * 获取检测项目特性对应详细信息
     */
    @PreAuthorize("@ss.hasPermi('basicdata:itemFeature:query')")
    @GetMapping(value = "/getInfoByItemId/{itemId}")
    public AjaxResult getInfoByItemId(@PathVariable("itemId") String itemId)
    {
        return success(bsLabtestItemFeatureService.selectBsLabtestItemFeatureByItemId(itemId));
    }

    /**
     * 新增检测项目特性对应
     */
    @PreAuthorize("@ss.hasPermi('basicdata:itemFeature:add')")
    @Log(title = "检测项目特性对应", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody BsLabtestItemFeature bsLabtestItemFeature)
    {
        return toAjax(bsLabtestItemFeatureService.insertBsLabtestItemFeature(bsLabtestItemFeature));
    }

    /**
     * 修改检测项目特性对应
     */
    @PreAuthorize("@ss.hasPermi('basicdata:itemFeature:edit')")
    @Log(title = "检测项目特性对应", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody BsLabtestItemFeature bsLabtestItemFeature)
    {
        return toAjax(bsLabtestItemFeatureService.updateBsLabtestItemFeature(bsLabtestItemFeature));
    }

    /**
     * 删除检测项目特性对应
     */
    @PreAuthorize("@ss.hasPermi('basicdata:itemFeature:remove')")
    @Log(title = "检测项目特性对应", businessType = BusinessType.DELETE)
    @DeleteMapping("/{bsLabtestItemFeatureId}")
    public AjaxResult remove(@PathVariable String bsLabtestItemFeatureId)
    {
        return toAjax(bsLabtestItemFeatureService.updateDeleteFlagById(bsLabtestItemFeatureId));
    }

    /**
     * 批量添加项目特性关联
     */
    @PreAuthorize("@ss.hasPermi('basicdata:itemFeature:add')")
    @Log(title = "批量添加项目特性", businessType = BusinessType.INSERT)
    @PostMapping("/batchAdd")
    public AjaxResult batchAdd(@RequestBody BatchAddItemFeatureDto batchAddDto) {
        return toAjax(bsLabtestItemFeatureService.batchAdd(batchAddDto));
    }

    /**
     * 批量更新项目特性关联（先删除再新增）
     */
    @PreAuthorize("@ss.hasPermi('basicdata:itemFeature:edit')")
    @Log(title = "批量更新项目特性", businessType = BusinessType.UPDATE)
    @PostMapping("/batchUpdate")
    public AjaxResult batchUpdate(@RequestBody BatchAddItemFeatureDto batchUpdateDto) {
        return toAjax(bsLabtestItemFeatureService.batchUpdate(batchUpdateDto));
    }
}