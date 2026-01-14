package com.gmlimsqi.business.unquality.controller;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.gmlimsqi.business.unquality.domain.YktReadDTO;
import com.gmlimsqi.common.annotation.Anonymous;
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
import com.gmlimsqi.business.unquality.domain.OpSampleUnquality;
import com.gmlimsqi.business.unquality.service.IOpSampleUnqualityService;
import com.gmlimsqi.common.utils.poi.ExcelUtil;
import com.gmlimsqi.common.core.page.TableDataInfo;

/**
 * 样品不合格处理单
 *
 * @author hhy
 * @date 2025-11-28
 */
@RestController
@RequestMapping("/unquality/unquality")
public class OpSampleUnqualityController extends BaseController
{
    @Autowired
    private IOpSampleUnqualityService opSampleUnqualityService;

    /**
     * 查询样品不合格处理单列表
     */
//    @PreAuthorize("@ss.hasPermi('unquality:unquality:list')")
    @GetMapping("/list")
    public TableDataInfo list(OpSampleUnquality opSampleUnquality)
    {
        startPage();
        List<OpSampleUnquality> list = opSampleUnqualityService.selectOpSampleUnqualityList(opSampleUnquality);
        return getDataTable(list);
    }

    /**
     * 导出样品不合格处理单列表
     */
//    @PreAuthorize("@ss.hasPermi('unquality:unquality:export')")
    @Log(title = "样品不合格处理单", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, OpSampleUnquality opSampleUnquality)
    {
        List<OpSampleUnquality> list = opSampleUnqualityService.selectOpSampleUnqualityList(opSampleUnquality);
        ExcelUtil<OpSampleUnquality> util = new ExcelUtil<OpSampleUnquality>(OpSampleUnquality.class);
        util.exportExcel(response, list, "样品不合格处理单数据");
    }

    /**
     * 获取样品不合格处理单详细信息
     */
//    @PreAuthorize("@ss.hasPermi('unquality:unquality:query')")
    @GetMapping(value = "/{opSampleUnqualityId}")
    public AjaxResult getInfo(@PathVariable("opSampleUnqualityId") String opSampleUnqualityId)
    {
        return success(opSampleUnqualityService.selectOpSampleUnqualityByOpSampleUnqualityId(opSampleUnqualityId));
    }

    /**
     * 新增样品不合格处理单
     */
//    @PreAuthorize("@ss.hasPermi('unquality:unquality:add')")
    @Log(title = "样品不合格处理单", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody OpSampleUnquality opSampleUnquality)
    {
        return toAjax(opSampleUnqualityService.insertOpSampleUnquality(opSampleUnquality));
    }

    /**
     * 修改样品不合格处理单
     */
//    @PreAuthorize("@ss.hasPermi('unquality:unquality:edit')")
    @Log(title = "样品不合格处理单", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody OpSampleUnquality opSampleUnquality)
    {
        return toAjax(opSampleUnqualityService.updateOpSampleUnquality(opSampleUnquality));
    }

    /**
     * 一卡通读取不合格处理单
     * @param yktReadDTO 读取样品不合格处理单
     * @return {@link List<OpSampleUnquality>} 样品不合格处理单集合
     */
//    @PreAuthorize("@ss.hasPermi('unquality:unquality:query')")
    @Anonymous
    @GetMapping(value = "/read")
    public TableDataInfo read(YktReadDTO yktReadDTO)
    {
        startPage();
        List<OpSampleUnquality> list = opSampleUnqualityService.selectOpSampleUnqualityBySignId(yktReadDTO);
        return getDataTable(list);
    }

    /**
     * 地磅修改样品不合格处理单
     * @param opSampleUnqualityIds 样品不合格处理单id列表
     * @return {@link AjaxResult} 结果
     */
//    @PreAuthorize("@ss.hasPermi('unquality:unquality:edit')")
    @Log(title = "样品不合格处理单", businessType = BusinessType.UPDATE)
    @PostMapping("/update")
    @Anonymous
    public AjaxResult update(@RequestBody List<String> opSampleUnqualityIds)
    {
        return toAjax(opSampleUnqualityService.updateOpSampleUnqualityByDiBang(opSampleUnqualityIds));
    }

    /**
     * 手动关闭不合格处理单
     * @param opSampleUnqualityId 样品不合格处理单id列表
     * @return {@link AjaxResult} 结果
     */

    @PutMapping("/manualUpdate/{opSampleUnqualityId}")
    public AjaxResult manualUpdate(@PathVariable String opSampleUnqualityId)
    {
        return toAjax(opSampleUnqualityService.updateOpSampleUnqualityManually(opSampleUnqualityId));
    }




}
