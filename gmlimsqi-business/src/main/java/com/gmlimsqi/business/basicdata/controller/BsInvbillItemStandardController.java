package com.gmlimsqi.business.basicdata.controller;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.gmlimsqi.business.basicdata.dto.BatchAddStandardDto;
import com.gmlimsqi.business.basicdata.dto.MaterialItemDTO;
import com.gmlimsqi.business.basicdata.vo.BsInvbillItemStandardVo;
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
import com.gmlimsqi.business.basicdata.domain.BsInvbillItemStandard;
import com.gmlimsqi.business.basicdata.service.IBsInvbillItemStandardService;
import com.gmlimsqi.common.utils.poi.ExcelUtil;
import com.gmlimsqi.common.core.page.TableDataInfo;

/**
 * 物料项目标准Controller
 * 
 * @author hhy
 * @date 2025-09-08
 */
@RestController
@RequestMapping("/basicdata/standard")
public class BsInvbillItemStandardController extends BaseController
{
    @Autowired
    private IBsInvbillItemStandardService bsInvbillItemStandardService;


    /**
     * 查询物料项目标准列表（分组展示）
     */
    @PreAuthorize("@ss.hasPermi('basicdata:standard:list')")
    @GetMapping("/list")
    public TableDataInfo list(BsInvbillItemStandard bsInvbillItemStandard) {
        startPage();
        List<BsInvbillItemStandardVo> list = bsInvbillItemStandardService.selectListGroupByInvbill(bsInvbillItemStandard);
        return getDataTable(list);
    }



    /**
     * 导出物料项目标准列表
     */
    @PreAuthorize("@ss.hasPermi('basicdata:standard:export')")
    @Log(title = "物料项目标准", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, BsInvbillItemStandard bsInvbillItemStandard)
    {
        List<BsInvbillItemStandard> list = bsInvbillItemStandardService.selectBsInvbillItemStandardList(bsInvbillItemStandard);
        ExcelUtil<BsInvbillItemStandard> util = new ExcelUtil<BsInvbillItemStandard>(BsInvbillItemStandard.class);
        util.exportExcel(response, list, "物料项目标准数据");
    }

    /**
     * 获取物料项目标准详细信息
     */
    @PreAuthorize("@ss.hasPermi('basicdata:standard:query')")
    @GetMapping(value = "/{bsInvbillItemStandardId}")
    public AjaxResult getInfo(@PathVariable("bsInvbillItemStandardId") String bsInvbillItemStandardId)
    {
        return success(bsInvbillItemStandardService.selectBsInvbillItemStandardByBsInvbillItemStandardId(bsInvbillItemStandardId));
    }

    /**
     * 根据物料id获取物料项目标准详细信息
     */
    @PreAuthorize("@ss.hasPermi('basicdata:standard:query')")
    @GetMapping(value = "/getStandardByInvbillCode/{invbillCode}")
    public AjaxResult getStandardByInvbillCode(@PathVariable("invbillCode") String invbillCode)
    {

        return success(bsInvbillItemStandardService.selectBsInvbillItemStandardByBsInvbillCode(invbillCode));
    }
    /**
     * 根据物料和项目id获取物料项目标准详细信息
     */
    @PreAuthorize("@ss.hasPermi('basicdata:standard:query')")
    @GetMapping(value = "/getStandardByInvbillCode/{invbillCode}/{itemId}")
    public AjaxResult getStandardByInvbillItemId(@PathVariable("invbillCode") String invbillCode,@PathVariable("itemId") String itemId)
    {

        return success(bsInvbillItemStandardService.selectByBsInvbillItemId(invbillCode,itemId));
    }

    /**
     * 新增物料项目标准
     */
    @PreAuthorize("@ss.hasPermi('basicdata:standard:add')")
    @Log(title = "物料项目标准", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody BsInvbillItemStandard bsInvbillItemStandard)
    {
        return toAjax(bsInvbillItemStandardService.insertBsInvbillItemStandard(bsInvbillItemStandard));
    }

    /**
     * 修改物料项目标准
     */
    @PreAuthorize("@ss.hasPermi('basicdata:standard:edit')")
    @Log(title = "物料项目标准", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody BsInvbillItemStandard bsInvbillItemStandard)
    {
        return toAjax(bsInvbillItemStandardService.updateBsInvbillItemStandard(bsInvbillItemStandard));
    }
    /**
     * 删除物料项目标准
     */
    @PreAuthorize("@ss.hasPermi('basicdata:standard:remove')")
    @Log(title = "物料项目标准", businessType = BusinessType.DELETE)
    @DeleteMapping("/{bsInvbillItemStandardId}")
    public AjaxResult remove(@PathVariable String bsInvbillItemStandardId)
    {
        return toAjax(bsInvbillItemStandardService.updateDeleteFlagById(bsInvbillItemStandardId));
    }

    /**
     * 批量新增物料项目标准
     */
    @PreAuthorize("@ss.hasPermi('basicdata:standard:add')")
    @Log(title = "物料项目标准", businessType = BusinessType.INSERT)
    @PostMapping("/batch")
    public AjaxResult batchAdd(@RequestBody BatchAddStandardDto batchAddDto)
    {
        return toAjax(bsInvbillItemStandardService.batchAdd(batchAddDto));
    }

    /**
     * 批量更新物料项目标准
     */
    @PreAuthorize("@ss.hasPermi('basicdata:standard:edit')")
    @Log(title = "物料项目标准", businessType = BusinessType.UPDATE)
    @PutMapping("/batch")
    public AjaxResult batchUpdate(@RequestBody BatchAddStandardDto batchUpdateDto)
    {
        return toAjax(bsInvbillItemStandardService.batchUpdate(batchUpdateDto));
    }

    /**
     * 根据物料编码查询物料检测项目
     * @param materialItemDTO 物料编码
     * @return {@link BsInvbillItemStandardVo}
     */
    @GetMapping(value = "/getItemByInvbillCode")
    public TableDataInfo getItemByInvbillCode(MaterialItemDTO materialItemDTO)
    {
        startPage();
        return getDataTable(bsInvbillItemStandardService.getItemByInvbillCode(materialItemDTO));
    }

}
