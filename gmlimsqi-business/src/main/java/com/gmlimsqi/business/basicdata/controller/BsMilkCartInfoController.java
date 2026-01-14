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
import com.gmlimsqi.business.basicdata.domain.BsMilkCartInfo;
import com.gmlimsqi.business.basicdata.service.IBsMilkCartInfoService;
import com.gmlimsqi.common.utils.poi.ExcelUtil;
import com.gmlimsqi.common.core.page.TableDataInfo;

/**
 * 奶车信息Controller
 * 
 * @author hhy
 * @date 2025-11-05
 */
@RestController
@RequestMapping("/basicdata/info")
public class BsMilkCartInfoController extends BaseController
{
    @Autowired
    private IBsMilkCartInfoService bsMilkCartInfoService;

    /**
     * 查询奶车信息列表
     */
    @PreAuthorize("@ss.hasPermi('basicdata:info:list')")
    @GetMapping("/list")
    public TableDataInfo list(BsMilkCartInfo bsMilkCartInfo)
    {
        startPage();
        List<BsMilkCartInfo> list = bsMilkCartInfoService.selectBsMilkCartInfoList(bsMilkCartInfo);
        return getDataTable(list);
    }

    /**
     * 导出奶车信息列表
     */
    @PreAuthorize("@ss.hasPermi('basicdata:info:export')")
    @Log(title = "奶车信息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, BsMilkCartInfo bsMilkCartInfo)
    {
        List<BsMilkCartInfo> list = bsMilkCartInfoService.selectBsMilkCartInfoList(bsMilkCartInfo);
        ExcelUtil<BsMilkCartInfo> util = new ExcelUtil<BsMilkCartInfo>(BsMilkCartInfo.class);
        util.exportExcel(response, list, "奶车信息数据");
    }

    /**
     * 获取奶车信息详细信息
     * @param id 奶车信息id
     * @return {@link BsMilkCartInfo}
     */
//    @PreAuthorize("@ss.hasPermi('basicdata:info:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") String id)
    {
        return success(bsMilkCartInfoService.selectBsMilkCartInfoById(id));
    }

    /**
     * 根据车牌号查询奶车信息详情
     * @param driverCode 车牌号
     * @return {@link BsMilkCartInfo}
     */
//    @PreAuthorize("@ss.hasPermi('basicdata:info:query')")
    @GetMapping("/readByDriverCode/{driverCode}")
    public AjaxResult readByDriverCode(@PathVariable("driverCode") String driverCode)
    {
        return success(bsMilkCartInfoService.selectBsMilkCartInfoByDriverCode(driverCode));
    }

    /**
     * 新增奶车信息
     */
    @PreAuthorize("@ss.hasPermi('basicdata:info:add')")
    @Log(title = "奶车信息", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody BsMilkCartInfo bsMilkCartInfo)
    {
        return toAjax(bsMilkCartInfoService.insertBsMilkCartInfo(bsMilkCartInfo));
    }

    /**
     * 修改奶车信息
     */
    @PreAuthorize("@ss.hasPermi('basicdata:info:edit')")
    @Log(title = "奶车信息", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody BsMilkCartInfo bsMilkCartInfo)
    {
        return toAjax(bsMilkCartInfoService.updateBsMilkCartInfo(bsMilkCartInfo));
    }

    /**
     * 删除奶车信息
     */
    @PreAuthorize("@ss.hasPermi('basicdata:info:remove')")
    @Log(title = "奶车信息", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable String[] ids)
    {
        return toAjax(bsMilkCartInfoService.deleteBsMilkCartInfoByIds(ids));
    }
}
