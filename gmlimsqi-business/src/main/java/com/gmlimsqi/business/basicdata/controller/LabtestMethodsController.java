package com.gmlimsqi.business.basicdata.controller;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.gmlimsqi.business.util.LabtestMethodsValidator;
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
import com.gmlimsqi.business.basicdata.domain.LabtestMethods;
import com.gmlimsqi.business.basicdata.service.ILabtestMethodsService;
import com.gmlimsqi.common.utils.poi.ExcelUtil;
import com.gmlimsqi.common.core.page.TableDataInfo;

/**
 * 检测方法Controller
 * 
 * @author ruoyi
 * @date 2025-08-06
 */
@RestController
@RequestMapping("/basicdata/labtestMethods")
public class LabtestMethodsController extends BaseController
{
    @Autowired
    private ILabtestMethodsService bsLabtestMethodsService;

    /**
     * 查询检测方法列表
     */
    @PreAuthorize("@ss.hasPermi('basicdata:labtestMethods:list')")
    @GetMapping("/list")
    public TableDataInfo list(LabtestMethods labtestMethods)
    {
        startPage();
        List<LabtestMethods> list = bsLabtestMethodsService.selectBsLabtestMethodsList(labtestMethods);
        return getDataTable(list);
    }

    /**
     * 导出检测方法列表
     */
    @PreAuthorize("@ss.hasPermi('basicdata:labtestMethods:export')")
    @Log(title = "检测方法", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, LabtestMethods labtestMethods)
    {
        List<LabtestMethods> list = bsLabtestMethodsService.selectBsLabtestMethodsList(labtestMethods);
        ExcelUtil<LabtestMethods> util = new ExcelUtil<LabtestMethods>(LabtestMethods.class);
        util.exportExcel(response, list, "检测方法数据");
    }

    /**
     * 获取检测方法详细信息
     */
    @PreAuthorize("@ss.hasPermi('basicdata:labtestMethods:query')")
    @GetMapping(value = "/{bsLabtestMethodsId}")
    public AjaxResult getInfo(@PathVariable("bsLabtestMethodsId") String bsLabtestMethodsId)
    {
        return success(bsLabtestMethodsService.selectBsLabtestMethodsByBsLabtestMethodsId(bsLabtestMethodsId));
    }

    /**
     * 新增检测方法
     */
    @PreAuthorize("@ss.hasPermi('basicdata:labtestMethods:add')")
    @Log(title = "检测方法", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody LabtestMethods labtestMethods)
    {
        //TODO 如何校验唯一性
        //校验合法性
        String error = LabtestMethodsValidator.validate(labtestMethods);
        if (!error.isEmpty()){
            return error(error);
        }
        return toAjax(bsLabtestMethodsService.insertBsLabtestMethods(labtestMethods));
    }

    /**
     * 修改检测方法
     */
    @PreAuthorize("@ss.hasPermi('basicdata:labtestMethods:edit')")
    @Log(title = "检测方法", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody LabtestMethods labtestMethods)
    {
        //TODO 如何校验唯一性
        //校验合法性
        String error = LabtestMethodsValidator.validate(labtestMethods);
        if (!error.isEmpty()){
            return error(error);
        }
        return toAjax(bsLabtestMethodsService.updateBsLabtestMethods(labtestMethods));
    }
    /**
     * 修改启用状态
     */
    @PreAuthorize("@ss.hasPermi('basicdata:labTestItems:edit')")
    @Log(title = "检测项目", businessType = BusinessType.UPDATE)
    @PutMapping("/editEnable")
    public AjaxResult editEnable(@RequestBody LabtestMethods labtestMethods)
    {
        if(StringUtils.isEmpty(labtestMethods.getBsLabtestMethodsId()) || StringUtils.isEmpty(labtestMethods.getIsEnable())){
            return error("缺少参数");
        }
        return toAjax(bsLabtestMethodsService.updateEnableById(labtestMethods));
    }
    /**
     * 删除检测方法
     */
    @PreAuthorize("@ss.hasPermi('basicdata:labtestMethods:remove')")
    @Log(title = "检测方法", businessType = BusinessType.DELETE)
	@DeleteMapping("/{bsLabtestMethodsIds}")
    public AjaxResult remove(@PathVariable String[] bsLabtestMethodsIds)
    {
        return toAjax(bsLabtestMethodsService.deleteBsLabtestMethodsByBsLabtestMethodsIds(bsLabtestMethodsIds));
    }
}
