package com.gmlimsqi.business.basicdata.controller;

import com.gmlimsqi.business.basicdata.domain.BsContact;
import com.gmlimsqi.business.basicdata.dto.BsContactDto;
import com.gmlimsqi.business.basicdata.service.IBsContactService;
import com.gmlimsqi.common.annotation.Log;
import com.gmlimsqi.common.core.controller.BaseController;
import com.gmlimsqi.common.core.domain.AjaxResult;
import com.gmlimsqi.common.core.page.TableDataInfo;
import com.gmlimsqi.common.enums.BusinessType;
import com.gmlimsqi.common.utils.poi.ExcelUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;

/**
 * 通讯方式Controller
 *
 * @author wgq
 * @date 2025-09-15
 */
@RestController
@RequestMapping("/basicdata/contact")
public class BsContactController extends BaseController
{
    @Autowired
    private IBsContactService bsContactService;

    /**
     * 查询通讯方式列表
     */
    @PreAuthorize("@ss.hasPermi('basicdata:contact:list')")
    @GetMapping("/list")
    public TableDataInfo list(BsContact bsContact)
    {
        startPage();
        List<BsContact> list = bsContactService.selectBsContactList(bsContact);
        return getDataTable(list);
    }

    /**
     * 导出通讯方式列表
     */
    @PreAuthorize("@ss.hasPermi('basicdata:contact:export')")
    @Log(title = "通讯方式", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, BsContact bsContact)
    {
        List<BsContact> list = bsContactService.selectBsContactList(bsContact);
        ExcelUtil<BsContact> util = new ExcelUtil<BsContact>(BsContact.class);
        util.exportExcel(response, list, "通讯方式数据");
    }

    /**
     * 获取通讯方式详细信息
     */
    @PreAuthorize("@ss.hasPermi('basicdata:contact:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") String id)
    {
        return success(bsContactService.selectBsContactById(id));
    }


    /**
     * 获取通讯方式详细信息
     */
    @GetMapping(value = "/getInfoByDeptId/{deptId}")
    public AjaxResult getInfoByDeptId(@PathVariable("deptId") String deptId)
    {

        return success(bsContactService.getInfoByDeptId(deptId));
    }

    /**
     * 新增通讯方式
     */
    @PreAuthorize("@ss.hasPermi('basicdata:contact:add')")
    @Log(title = "通讯方式", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody @Valid BsContactDto bsContact)
    {
        return toAjax(bsContactService.insertBsContact(bsContact));
    }

    /**
     * 修改通讯方式
     */
    @PreAuthorize("@ss.hasPermi('basicdata:contact:edit')")
    @Log(title = "通讯方式", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody BsContactDto bsContact)
    {
        return toAjax(bsContactService.updateBsContact(bsContact));
    }

    /**
     * 删除通讯方式
     */
    @PreAuthorize("@ss.hasPermi('basicdata:contact:remove')")
    @Log(title = "通讯方式", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable String[] ids)
    {
        return toAjax(bsContactService.deleteBsContactByIds(ids));
    }

    /**
     * 启用/弃用通讯方式
     */
    @PreAuthorize("@ss.hasPermi('basicdata:contact:enable')")
    @Log(title = "启用/弃用通讯方式", businessType = BusinessType.UPDATE)
    @PutMapping("/enable")
    public AjaxResult enable(@RequestBody BsContact bsContact)
    {
        return toAjax(bsContactService.enableBsContact(bsContact));
    }

}
