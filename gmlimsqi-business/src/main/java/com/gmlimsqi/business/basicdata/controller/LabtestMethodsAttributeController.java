package com.gmlimsqi.business.basicdata.controller;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.gmlimsqi.business.basicdata.domain.LabtestMethodsAttribute;
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
import com.gmlimsqi.business.basicdata.service.ILabtestMethodsAttributeService;
import com.gmlimsqi.common.utils.poi.ExcelUtil;
import com.gmlimsqi.common.core.page.TableDataInfo;

/**
 * 检测属性Controller
 * 
 * @author ruoyi
 * @date 2025-08-06
 */
@RestController
@RequestMapping("/basicdata/attribute")
public class LabtestMethodsAttributeController extends BaseController
{
    @Autowired
    private ILabtestMethodsAttributeService bsLabtestMethodsAttributeService;

    /**
     * 查询检测属性列表
     */
    @PreAuthorize("@ss.hasPermi('basicdata:attribute:list')")
    @GetMapping("/list")
    public TableDataInfo list(LabtestMethodsAttribute labtestMethodsAttribute)
    {
        startPage();
        List<LabtestMethodsAttribute> list = bsLabtestMethodsAttributeService.selectBsLabtestMethodsAttributeList(labtestMethodsAttribute);
        return getDataTable(list);
    }

    /**
     * 导出检测属性列表
     */
    @PreAuthorize("@ss.hasPermi('basicdata:attribute:export')")
    @Log(title = "检测属性", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, LabtestMethodsAttribute labtestMethodsAttribute)
    {
        List<LabtestMethodsAttribute> list = bsLabtestMethodsAttributeService.selectBsLabtestMethodsAttributeList(labtestMethodsAttribute);
        ExcelUtil<LabtestMethodsAttribute> util = new ExcelUtil<LabtestMethodsAttribute>(LabtestMethodsAttribute.class);
        util.exportExcel(response, list, "检测属性数据");
    }

    /**
     * 获取检测属性详细信息
     */
    @PreAuthorize("@ss.hasPermi('basicdata:attribute:query')")
    @GetMapping(value = "/{bsLabtestMethodsAttributeId}")
    public AjaxResult getInfo(@PathVariable("bsLabtestMethodsAttributeId") String bsLabtestMethodsAttributeId)
    {
        return success(bsLabtestMethodsAttributeService.selectBsLabtestMethodsAttributeByBsLabtestMethodsAttributeId(bsLabtestMethodsAttributeId));
    }

    /**
     * 新增检测属性
     */
    @PreAuthorize("@ss.hasPermi('basicdata:attribute:add')")
    @Log(title = "检测属性", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody LabtestMethodsAttribute labtestMethodsAttribute)
    {
        return toAjax(bsLabtestMethodsAttributeService.insertBsLabtestMethodsAttribute(labtestMethodsAttribute));
    }

    /**
     * 修改检测属性
     */
    @PreAuthorize("@ss.hasPermi('basicdata:attribute:edit')")
    @Log(title = "检测属性", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody LabtestMethodsAttribute labtestMethodsAttribute)
    {
        return toAjax(bsLabtestMethodsAttributeService.updateBsLabtestMethodsAttribute(labtestMethodsAttribute));
    }

    /**
     * 删除检测属性
     */
    @PreAuthorize("@ss.hasPermi('basicdata:attribute:remove')")
    @Log(title = "检测属性", businessType = BusinessType.DELETE)
	@DeleteMapping("/{bsLabtestMethodsAttributeIds}")
    public AjaxResult remove(@PathVariable String[] bsLabtestMethodsAttributeIds)
    {
        return toAjax(bsLabtestMethodsAttributeService.deleteBsLabtestMethodsAttributeByBsLabtestMethodsAttributeIds(bsLabtestMethodsAttributeIds));
    }
}
