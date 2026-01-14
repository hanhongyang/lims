package com.gmlimsqi.business.basicdata.controller;

import java.util.List;
import javax.servlet.http.HttpServletResponse;
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
import com.gmlimsqi.business.basicdata.domain.BsMilkWarehouse;
import com.gmlimsqi.business.basicdata.service.IBsMilkWarehouseService;
import com.gmlimsqi.common.utils.poi.ExcelUtil;
import com.gmlimsqi.common.core.page.TableDataInfo;

/**
 * 奶仓档案Controller
 * 
 * @author hhy
 * @date 2025-11-05
 */
@RestController
@RequestMapping("/basicdata/warehouse")
public class BsMilkWarehouseController extends BaseController
{
    @Autowired
    private IBsMilkWarehouseService bsMilkWarehouseService;

    /**
     * 查询奶仓档案列表
     */
//    @PreAuthorize("@ss.hasPermi('basicdata:warehouse:list')")
    @GetMapping("/list")
    public TableDataInfo list(BsMilkWarehouse bsMilkWarehouse)
    {
        startPage();
        List<BsMilkWarehouse> list = bsMilkWarehouseService.selectBsMilkWarehouseList(bsMilkWarehouse);
        return getDataTable(list);
    }

    /**
     * 导出奶仓档案列表
     */
//    @PreAuthorize("@ss.hasPermi('basicdata:warehouse:export')")
    @Log(title = "奶仓档案", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, BsMilkWarehouse bsMilkWarehouse)
    {
        List<BsMilkWarehouse> list = bsMilkWarehouseService.selectBsMilkWarehouseList(bsMilkWarehouse);
        ExcelUtil<BsMilkWarehouse> util = new ExcelUtil<BsMilkWarehouse>(BsMilkWarehouse.class);
        util.exportExcel(response, list, "奶仓档案数据");
    }

    /**
     * 获取奶仓档案详细信息
     */
//    @PreAuthorize("@ss.hasPermi('basicdata:warehouse:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") String id)
    {
        return success(bsMilkWarehouseService.selectBsMilkWarehouseById(id));
    }

    /**
     * 新增奶仓档案
     */
//    @PreAuthorize("@ss.hasPermi('basicdata:warehouse:add')")
    @Log(title = "奶仓档案", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody BsMilkWarehouse bsMilkWarehouse)
    {
        return toAjax(bsMilkWarehouseService.insertBsMilkWarehouse(bsMilkWarehouse));
    }

    /**
     * 修改奶仓档案
     */
//    @PreAuthorize("@ss.hasPermi('basicdata:warehouse:edit')")
    @Log(title = "奶仓档案", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody BsMilkWarehouse bsMilkWarehouse)
    {
        return toAjax(bsMilkWarehouseService.updateBsMilkWarehouse(bsMilkWarehouse));
    }

    /**
     * 删除奶仓档案
     */
//    @PreAuthorize("@ss.hasPermi('basicdata:warehouse:remove')")
    @Log(title = "奶仓档案", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable String[] ids)
    {
        return toAjax(bsMilkWarehouseService.deleteBsMilkWarehouseByIds(ids));
    }
}
