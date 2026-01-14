package com.gmlimsqi.business.basicdata.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.gmlimsqi.common.core.domain.AjaxResult;
import com.gmlimsqi.common.utils.StringUtils;
import com.gmlimsqi.common.utils.http.HttpUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
import com.gmlimsqi.common.enums.BusinessType;
import com.gmlimsqi.business.basicdata.domain.BsWorkdayConfig;
import com.gmlimsqi.business.basicdata.service.IBsWorkdayConfigService;
import com.gmlimsqi.common.utils.poi.ExcelUtil;
import com.gmlimsqi.common.core.page.TableDataInfo;
import org.springframework.web.client.RestTemplate;

/**
 * 工作日配置Controller
 *
 * @author hhy
 * @date 2025-09-09
 */
@RestController
@RequestMapping("/basicdata/workdayConfig")
public class BsWorkdayConfigController extends BaseController
{
    @Autowired
    private IBsWorkdayConfigService bsWorkdayConfigService;

    /**
     * 查询工作日配置列表
     */
    @PreAuthorize("@ss.hasPermi('basicdata:workdayConfig:list')")
    @GetMapping("/list")
    public TableDataInfo list(BsWorkdayConfig bsWorkdayConfig)
    {
        startPage();
        List<BsWorkdayConfig> list = bsWorkdayConfigService.selectBsWorkdayConfigList(bsWorkdayConfig);
        return getDataTable(list);
    }

    /**
     * 根据年月查询工作日配置
     */
    @PreAuthorize("@ss.hasPermi('basicdata:workdayConfig:list')")
    @GetMapping("/listByYearMonth/{year}/{month}")
    public AjaxResult listByYearMonth(@PathVariable("year") Integer year, @PathVariable("month") Integer month)
    {
        List<BsWorkdayConfig> list = bsWorkdayConfigService.selectWorkdayConfigByYearMonth(year, month);
        return success(list);
    }

    /**
     * 导出工作日配置列表
     */
    @PreAuthorize("@ss.hasPermi('basicdata:workdayConfig:export')")
    @Log(title = "工作日配置", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, BsWorkdayConfig bsWorkdayConfig)
    {
        List<BsWorkdayConfig> list = bsWorkdayConfigService.selectBsWorkdayConfigList(bsWorkdayConfig);
        ExcelUtil<BsWorkdayConfig> util = new ExcelUtil<BsWorkdayConfig>(BsWorkdayConfig.class);
        util.exportExcel(response, list, "工作日配置数据");
    }

    /**
     * 获取工作日配置详细信息
     */
    @PreAuthorize("@ss.hasPermi('basicdata:workdayConfig:query')")
    @GetMapping(value = "/{bsWorkdayConfigId}")
    public AjaxResult getInfo(@PathVariable("bsWorkdayConfigId") String bsWorkdayConfigId)
    {
        return success(bsWorkdayConfigService.selectBsWorkdayConfigByBsWorkdayConfigId(bsWorkdayConfigId));
    }

    /**
     * 根据日期获取工作日配置
     */
    @PreAuthorize("@ss.hasPermi('basicdata:workdayConfig:query')")
    @GetMapping(value = "/date/{dateStr}")
    public AjaxResult getInfoByDate(@PathVariable("dateStr") String dateStr)
    {
        return success(bsWorkdayConfigService.selectBsWorkdayConfigByDateStr(dateStr));
    }

    /**
     * 新增工作日配置
     */
    @PreAuthorize("@ss.hasPermi('basicdata:workdayConfig:add')")
    @Log(title = "工作日配置", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody BsWorkdayConfig bsWorkdayConfig)
    {
        if (bsWorkdayConfigService.checkDateStrUnique(bsWorkdayConfig.getDateStr()) > 0) {
            return error("日期 " + bsWorkdayConfig.getDateStr() + " 已存在");
        }
        return toAjax(bsWorkdayConfigService.insertBsWorkdayConfig(bsWorkdayConfig));
    }

    /**
     * 修改工作日配置
     */
    @PreAuthorize("@ss.hasPermi('basicdata:workdayConfig:edit')")
    @Log(title = "工作日配置", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody BsWorkdayConfig bsWorkdayConfig)
    {
        return toAjax(bsWorkdayConfigService.updateBsWorkdayConfig(bsWorkdayConfig));
    }

    /**
     * 更新工作日状态
     */
    @PreAuthorize("@ss.hasPermi('basicdata:workdayConfig:edit')")
    @Log(title = "更新工作日状态", businessType = BusinessType.UPDATE)
    @PutMapping("/updateWorkdayStatus")
    public AjaxResult updateWorkdayStatus(@RequestBody BsWorkdayConfig bsWorkdayConfig)
    {
        return toAjax(bsWorkdayConfigService.updateWorkdayStatus(
                bsWorkdayConfig.getDateStr(),
                bsWorkdayConfig.getIsWorkday(),
                getUsername()
        ));
    }

    /**
     * 删除工作日配置
     */
    @PreAuthorize("@ss.hasPermi('basicdata:workdayConfig:remove')")
    @Log(title = "工作日配置", businessType = BusinessType.DELETE)
    @DeleteMapping("/{bsWorkdayConfigIds}")
    public AjaxResult remove(@PathVariable String[] bsWorkdayConfigIds)
    {
        return toAjax(bsWorkdayConfigService.deleteBsWorkdayConfigByBsWorkdayConfigIds(bsWorkdayConfigIds));
    }




    /**
     * 从节假日API初始化数据
     */
    @PreAuthorize("@ss.hasPermi('basicdata:workdayConfig:add')")
    @Log(title = "从API初始化工作日配置", businessType = BusinessType.INSERT)
    @PostMapping("/initFromHoliday")
    public AjaxResult initFromHoliday(@RequestBody Map<String, Object> params) {
        try {
            Integer year = (Integer) params.get("year");
            if (year == null) {
                return AjaxResult.error("年份不能为空");
            }

            // 验证年份范围
            int currentYear = LocalDate.now().getYear();
            if (year < currentYear - 10 || year > currentYear + 10) {
                return AjaxResult.error("年份范围必须在" + (currentYear - 10) + "到" + (currentYear + 10) + "之间");
            }

            int count = bsWorkdayConfigService.initFromHolidayData(year, getUsername());
            return AjaxResult.success("初始化成功，共处理 " + count + " 条数据");
        } catch (IllegalArgumentException e) {
            return AjaxResult.error(e.getMessage());
        } catch (Exception e) {
            logger.error("初始化节假日数据失败", e);
            return AjaxResult.error("初始化失败: " + e.getMessage());
        }
    }
}