package com.gmlimsqi.business.disinfection.controller;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.gmlimsqi.business.disinfection.controller.vo.DisinfectionRespVo;
import lombok.RequiredArgsConstructor;
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
import com.gmlimsqi.business.disinfection.domain.DisinfectionPool;
import com.gmlimsqi.business.disinfection.service.IDisinfectionPoolService;
import com.gmlimsqi.common.utils.poi.ExcelUtil;
import com.gmlimsqi.common.core.page.TableDataInfo;

/**
 * 消毒池管理Controller
 *
 * @author yangjw
 * @date 2026-01-06
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/basicData/disinfection")
public class DisinfectionPoolController extends BaseController {

    private final IDisinfectionPoolService disinfectionPoolService;

    /**
     * 查询消毒池管理列表
     */
//    @PreAuthorize("@ss.hasPermi('basicdata:disinfection:list')")
    @GetMapping("/list")
    public TableDataInfo list(DisinfectionPool disinfectionPool) {
        startPage();
        List<DisinfectionRespVo> list = disinfectionPoolService.selectDisinfectionPoolList(disinfectionPool);
        return getDataTable(list);
    }

    /**
     * 导出消毒池管理列表
     */
//    @PreAuthorize("@ss.hasPermi('basicdata:disinfection:export')")
    @Log(title = "消毒池管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, DisinfectionPool disinfectionPool) {
        List<DisinfectionRespVo> list = disinfectionPoolService.selectDisinfectionPoolList(disinfectionPool);
        ExcelUtil<DisinfectionRespVo> util = new ExcelUtil<>(DisinfectionRespVo.class);
        util.exportExcel(response, list, "消毒池管理数据");
    }

    /**
     * 获取消毒池管理详细信息
     */
//    @PreAuthorize("@ss.hasPermi('basicdata:disinfection:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return success(disinfectionPoolService.selectDisinfectionPoolById(id));
    }

    /**
     * 新增消毒池管理
     */
//    @PreAuthorize("@ss.hasPermi('basicdata:disinfection:add')")
    @Log(title = "消毒池管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody DisinfectionPool disinfectionPool) {
        return toAjax(disinfectionPoolService.insertDisinfectionPool(disinfectionPool));
    }

    /**
     * 修改消毒池
     */
//    @PreAuthorize("@ss.hasPermi('basicdata:disinfection:edit')")
    @Log(title = "消毒池管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody DisinfectionPool disinfectionPool) {
        return toAjax(disinfectionPoolService.updateDisinfectionPool(disinfectionPool));
    }

    /**
     * 删除消毒池
     */
//    @PreAuthorize("@ss.hasPermi('basicdata:disinfection:remove')")
    @Log(title = "消毒池管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable List<Long> ids) {
        return toAjax(disinfectionPoolService.deleteDisinfectionPoolByIds(ids));
    }
}
