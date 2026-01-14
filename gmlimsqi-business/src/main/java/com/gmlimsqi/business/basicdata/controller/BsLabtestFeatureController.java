package com.gmlimsqi.business.basicdata.controller;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.gmlimsqi.business.basicdata.domain.LabtestItems;
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
import com.gmlimsqi.business.basicdata.domain.BsLabtestFeature;
import com.gmlimsqi.business.basicdata.service.IBsLabtestFeatureService;
import com.gmlimsqi.common.utils.poi.ExcelUtil;
import com.gmlimsqi.common.core.page.TableDataInfo;

/**
 * 检测特性Controller
 * 
 * @author hhy
 * @date 2025-09-05
 */
@RestController
@RequestMapping("/basicdata/feature")
public class BsLabtestFeatureController extends BaseController
{
    @Autowired
    private IBsLabtestFeatureService bsLabtestFeatureService;

    /**
     * 查询检测特性列表
     */
    @PreAuthorize("@ss.hasPermi('basicdata:feature:list')")
    @GetMapping("/list")
    public TableDataInfo list(BsLabtestFeature bsLabtestFeature)
    {
        startPage();
        List<BsLabtestFeature> list = bsLabtestFeatureService.selectBsLabtestFeatureList(bsLabtestFeature);
        return getDataTable(list);
    }

    /**
     * 导出检测特性列表
     */
    @PreAuthorize("@ss.hasPermi('basicdata:feature:export')")
    @Log(title = "检测特性", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, BsLabtestFeature bsLabtestFeature)
    {
        List<BsLabtestFeature> list = bsLabtestFeatureService.selectBsLabtestFeatureList(bsLabtestFeature);
        ExcelUtil<BsLabtestFeature> util = new ExcelUtil<BsLabtestFeature>(BsLabtestFeature.class);
        util.exportExcel(response, list, "检测特性数据");
    }

    /**
     * 获取检测特性详细信息
     */
    @PreAuthorize("@ss.hasPermi('basicdata:feature:query')")
    @GetMapping(value = "/{bsLabtestFeatureId}")
    public AjaxResult getInfo(@PathVariable("bsLabtestFeatureId") String bsLabtestFeatureId)
    {
        return success(bsLabtestFeatureService.selectBsLabtestFeatureByBsLabtestFeatureId(bsLabtestFeatureId));
    }

    /**
     * 新增检测特性
     */
    @PreAuthorize("@ss.hasPermi('basicdata:feature:add')")
    @Log(title = "检测特性", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody BsLabtestFeature bsLabtestFeature)
    {
        return toAjax(bsLabtestFeatureService.insertBsLabtestFeature(bsLabtestFeature));
    }

    /**
     * 修改检测特性
     */
    @PreAuthorize("@ss.hasPermi('basicdata:feature:edit')")
    @Log(title = "检测特性", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody BsLabtestFeature bsLabtestFeature)
    {
        return toAjax(bsLabtestFeatureService.updateBsLabtestFeature(bsLabtestFeature));
    }

    /**
     * 删除检测特性
     */
    @PreAuthorize("@ss.hasPermi('basicdata:feature:remove')")
    @Log(title = "检测特性", businessType = BusinessType.DELETE)
	@DeleteMapping("/{bsLabtestFeatureIds}")
    public AjaxResult remove(@PathVariable String[] bsLabtestFeatureIds)
    {
        return toAjax(bsLabtestFeatureService.deleteBsLabtestFeatureByBsLabtestFeatureIds(bsLabtestFeatureIds));
    }

    /**
     * 修改启用状态
     */
    @PreAuthorize("@ss.hasPermi('basicdata:feature:edit')")
    @Log(title = "检测特性", businessType = BusinessType.UPDATE)
    @PutMapping("/editEnable")
    public AjaxResult editEnable(@RequestBody BsLabtestFeature bsLabtestFeature)
    {
        if(StringUtils.isEmpty(bsLabtestFeature.getBsLabtestFeatureId()) || StringUtils.isEmpty(bsLabtestFeature.getIsEnable())){
            return error("缺少参数");
        }
        return toAjax(bsLabtestFeatureService.updateEnableById(bsLabtestFeature));
    }
}
