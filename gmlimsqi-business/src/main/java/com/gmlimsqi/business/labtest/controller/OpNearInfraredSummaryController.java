package com.gmlimsqi.business.labtest.controller;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.gmlimsqi.business.labtest.bo.BloodResultImportBo;
import com.gmlimsqi.business.labtest.domain.OpNearInfraredSummary;
import com.gmlimsqi.business.labtest.service.IOpNearInfraredSummaryService;
import com.gmlimsqi.business.util.OpNearInfraredImportUtil;
import com.gmlimsqi.common.utils.SecurityUtils;
import com.gmlimsqi.common.utils.StringUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
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
import com.gmlimsqi.common.utils.poi.ExcelUtil;
import com.gmlimsqi.common.core.page.TableDataInfo;
import org.springframework.web.multipart.MultipartFile;

/**
 * 近红外汇总Controller
 * 
 * @author hhy
 * @date 2025-10-29
 */
@RestController
@RequestMapping("/basicdata/summary")
public class OpNearInfraredSummaryController extends BaseController
{
    @Autowired
    private IOpNearInfraredSummaryService opNearInfraredSummaryService;

    /**
     * 导入近红外分析结果
     */
    @PostMapping("/import")
    public AjaxResult importNearInfraredSummary(MultipartFile file) throws IOException {
        if (file == null || file.isEmpty()) {
            return AjaxResult.error("请选择要导入的Excel文件");
        }

        // 检查文件后缀
        String fileName = file.getOriginalFilename();
        if (!StringUtils.endsWithIgnoreCase(fileName, ".xlsx") && !StringUtils.endsWithIgnoreCase(fileName, ".xls")
        && StringUtils.equalsIgnoreCase(fileName, ".csv")) {
            return AjaxResult.error("只能导入Excel文件");
        }

        try {
            // 策略1：数值类型过滤非法字符，String类型不处理
            List<OpNearInfraredSummary> entities = OpNearInfraredImportUtil.importToEntity(file,
                    SecurityUtils.getUserId().toString(), 1);

            // 保存到数据库
            opNearInfraredSummaryService.importNearInfraredSummary(entities);

            System.out.println("导入成功，共" + entities.size() + "条数据");
        } catch (Exception e) {
            System.err.println("导入失败：" + e.getMessage());
        }
        return AjaxResult.success("导入成功");
    }

    /**
     * 查询近红外汇总列表
     */
    @PreAuthorize("@ss.hasPermi('basicdata:summary:list')")
    @GetMapping("/list")
    public TableDataInfo list(OpNearInfraredSummary opNearInfraredSummary)
    {
        startPage();
        List<OpNearInfraredSummary> list = opNearInfraredSummaryService.selectOpNearInfraredSummaryList(opNearInfraredSummary);
        return getDataTable(list);
    }

    /**
     * 导出近红外汇总列表
     */
    @PreAuthorize("@ss.hasPermi('basicdata:summary:export')")
    @Log(title = "近红外汇总", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, OpNearInfraredSummary opNearInfraredSummary)
    {
        List<OpNearInfraredSummary> list = opNearInfraredSummaryService.selectOpNearInfraredSummaryList(opNearInfraredSummary);
        ExcelUtil<OpNearInfraredSummary> util = new ExcelUtil<OpNearInfraredSummary>(OpNearInfraredSummary.class);
        util.exportExcel(response, list, "近红外汇总数据");
    }

    /**
     * 获取近红外汇总详细信息
     */
    @PreAuthorize("@ss.hasPermi('basicdata:summary:query')")
    @GetMapping(value = "/{dairylandId}")
    public AjaxResult getInfo(@PathVariable("dairylandId") String dairylandId)
    {
        return success(opNearInfraredSummaryService.selectOpNearInfraredSummaryByDairylandId(dairylandId));
    }

    /**
     * 新增近红外汇总
     */
    @PreAuthorize("@ss.hasPermi('basicdata:summary:add')")
    @Log(title = "近红外汇总", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody OpNearInfraredSummary opNearInfraredSummary)
    {
        return toAjax(opNearInfraredSummaryService.insertOpNearInfraredSummary(opNearInfraredSummary));
    }

    /**
     * 修改近红外汇总
     */
    @PreAuthorize("@ss.hasPermi('basicdata:summary:edit')")
    @Log(title = "近红外汇总", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody OpNearInfraredSummary opNearInfraredSummary)
    {
        return toAjax(opNearInfraredSummaryService.updateOpNearInfraredSummary(opNearInfraredSummary));
    }

    /**
     * 删除近红外汇总
     */
    @PreAuthorize("@ss.hasPermi('basicdata:summary:remove')")
    @Log(title = "近红外汇总", businessType = BusinessType.DELETE)
	@DeleteMapping("/{dairylandIds}")
    public AjaxResult remove(@PathVariable String[] dairylandIds)
    {
        return toAjax(opNearInfraredSummaryService.deleteOpNearInfraredSummaryByDairylandIds(dairylandIds));
    }
}
