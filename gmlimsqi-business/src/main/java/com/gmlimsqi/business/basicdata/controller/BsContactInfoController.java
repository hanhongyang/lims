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
import com.gmlimsqi.business.basicdata.domain.BsContactInfo;
import com.gmlimsqi.business.basicdata.service.IBsContactInfoService;
import com.gmlimsqi.common.utils.poi.ExcelUtil;
import com.gmlimsqi.common.core.page.TableDataInfo;

/**
 * 通讯方式联系人子Controller
 *
 * @author wgq
 * @date 2025-09-15
 */
@RestController
@RequestMapping("/basicdata/contactInfo")
public class BsContactInfoController extends BaseController
{
    @Autowired
    private IBsContactInfoService bsContactInfoService;

    /**
     * 查询通讯方式联系人子列表
     */
    @PreAuthorize("@ss.hasPermi('basicdata:contactInfo:list')")
    @GetMapping("/list")
    public TableDataInfo list(BsContactInfo bsContactInfo)
    {
        startPage();
        List<BsContactInfo> list = bsContactInfoService.selectBsContactInfoList(bsContactInfo);
        return getDataTable(list);
    }

    /**
     * 导出通讯方式联系人子列表
     */
    @PreAuthorize("@ss.hasPermi('basicdata:contactInfo:export')")
    @Log(title = "通讯方式联系人子", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, BsContactInfo bsContactInfo)
    {
        List<BsContactInfo> list = bsContactInfoService.selectBsContactInfoList(bsContactInfo);
        ExcelUtil<BsContactInfo> util = new ExcelUtil<BsContactInfo>(BsContactInfo.class);
        util.exportExcel(response, list, "通讯方式联系人子数据");
    }

    /**
     * 获取通讯方式联系人子详细信息
     */
    @PreAuthorize("@ss.hasPermi('basicdata:contactInfo:query')")
    @GetMapping(value = "/{bsContactInfoId}")
    public AjaxResult getInfo(@PathVariable("bsContactInfoId") String bsContactInfoId)
    {
        return success(bsContactInfoService.selectBsContactInfoByBsContactInfoId(bsContactInfoId));
    }

    /**
     * 新增通讯方式联系人子
     */
    @PreAuthorize("@ss.hasPermi('basicdata:contactInfo:add')")
    @Log(title = "通讯方式联系人子", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody BsContactInfo bsContactInfo)
    {
        return toAjax(bsContactInfoService.insertBsContactInfo(bsContactInfo));
    }

    /**
     * 修改通讯方式联系人子
     */
    @PreAuthorize("@ss.hasPermi('basicdata:contactInfo:edit')")
    @Log(title = "通讯方式联系人子", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody BsContactInfo bsContactInfo)
    {
        return toAjax(bsContactInfoService.updateBsContactInfo(bsContactInfo));
    }

    /**
     * 删除通讯方式联系人子
     */
    @PreAuthorize("@ss.hasPermi('basicdata:contactInfo:remove')")
    @Log(title = "通讯方式联系人子", businessType = BusinessType.DELETE)
    @DeleteMapping("/{bsContactInfoIds}")
    public AjaxResult remove(@PathVariable String[] bsContactInfoIds)
    {
        return toAjax(bsContactInfoService.deleteBsContactInfoByBsContactInfoIds(bsContactInfoIds));
    }
}
