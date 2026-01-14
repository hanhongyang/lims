package com.gmlimsqi.business.labtest.controller;

import com.gmlimsqi.business.labtest.domain.OpJczxFeedReportBase;
import com.gmlimsqi.business.labtest.dto.EmailDTO;
import com.gmlimsqi.business.labtest.dto.OpJczxFeedReportDto;
import com.gmlimsqi.business.labtest.service.IOpJczxFeedReportBaseService;
import com.gmlimsqi.business.labtest.vo.OpFeedReportListVo;
import com.gmlimsqi.business.labtest.vo.ReportSendInfoVo;
import com.gmlimsqi.common.annotation.Log;
import com.gmlimsqi.common.core.controller.BaseController;
import com.gmlimsqi.common.core.domain.AjaxResult;
import com.gmlimsqi.common.core.page.TableDataInfo;
import com.gmlimsqi.common.enums.BusinessType;
import com.gmlimsqi.common.utils.StringUtils;
import com.gmlimsqi.common.utils.poi.ExcelUtil;
import com.gmlimsqi.framework.config.ServerConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * 检测中心饲料报告主Controller
 *
 * @author hhy
 * @date 2025-10-14
 */
@RestController
@RequestMapping("/labtest/jczxFeedReport")
public class OpJczxFeedReportBaseController extends BaseController {
    @Autowired
    private IOpJczxFeedReportBaseService opJczxFeedReportBaseService;
    @Autowired
    private ServerConfig serverConfig;

    /**
     * 查询检测中心饲料报告主列表
     */
    // //@PreAuthorize("@ss.hasPermi('labtest:jczxFeedReport:list')")
    @GetMapping("/list")
    public TableDataInfo list(OpJczxFeedReportDto opJczxFeedReportBase) {
        startPage();
        List<OpFeedReportListVo> list = opJczxFeedReportBaseService.selectOpJczxFeedReportBaseList(opJczxFeedReportBase);
        String url = serverConfig.getUrl();
        for (OpFeedReportListVo opFeedReportListVo : list) {
            if (!StringUtils.isEmpty(opFeedReportListVo.getPdfFileInfo())) {
                opFeedReportListVo.setPdfFileInfo(url + opFeedReportListVo.getPdfFileInfo());
            }
        }
        return getDataTable(list);
    }

    @GetMapping("/query")
    public TableDataInfo query(OpJczxFeedReportDto opJczxFeedReportDto) {
        startPage();
        List<OpFeedReportListVo> list = opJczxFeedReportBaseService.queryFeedReport(opJczxFeedReportDto);
        return getDataTable(list);
    }

    /**
     * 导出检测中心饲料报告主列表
     */
    //  //@PreAuthorize("@ss.hasPermi('labtest:jczxFeedReport:export')")
    @Log(title = "检测中心饲料报告主", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, OpJczxFeedReportDto opJczxFeedReportBase) {
        List<OpFeedReportListVo> list = opJczxFeedReportBaseService.selectOpJczxFeedReportBaseList(opJczxFeedReportBase);
        ExcelUtil<OpFeedReportListVo> util = new ExcelUtil<>(OpFeedReportListVo.class);
        util.exportExcel(response, list, "检测中心饲料报告主数据");
    }

    /**
     * 获取检测中心饲料报告主详细信息
     */
    // //@PreAuthorize("@ss.hasPermi('labtest:jczxFeedReport:query')")
    @GetMapping(value = "/{opJczxFeedReportBaseId}")
    public AjaxResult getInfo(@PathVariable("opJczxFeedReportBaseId") String opJczxFeedReportBaseId) {
        return success(opJczxFeedReportBaseService.selectOpJczxFeedReportBaseByOpJczxFeedReportBaseId(opJczxFeedReportBaseId));
    }

    /**
     * 新增检测中心饲料报告主
     */
    // //@PreAuthorize("@ss.hasPermi('labtest:jczxFeedReport:add')")
    @Log(title = "检测中心饲料报告主", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody OpJczxFeedReportBase opJczxFeedReportBase) {
        return toAjax(opJczxFeedReportBaseService.insertOpJczxFeedReportBase(opJczxFeedReportBase));
    }

    /**
     * 修改检测中心饲料报告主
     */
    //@PreAuthorize("@ss.hasPermi('labtest:jczxFeedReport:edit')")
    @Log(title = "检测中心饲料报告主", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody OpJczxFeedReportBase opJczxFeedReportBase) {
        return toAjax(opJczxFeedReportBaseService.updateOpJczxFeedReportBase(opJczxFeedReportBase));
    }


    /**
     * 获取报告制作时的信息
     */
    //@PreAuthorize("@ss.hasPermi('labtest:jczxFeedReport:getMakeinfo')")
    @GetMapping(value = "/getMakeinfo/{entrustOrderSampleId}")
    public AjaxResult getMakeinfo(@PathVariable("entrustOrderSampleId") String entrustOrderSampleId) {
        return success(opJczxFeedReportBaseService.selecReportMakeInfoByEntrustOrderSampleId(entrustOrderSampleId));
    }


    /**
     * 检测中心饲料报制作
     */
    //@PreAuthorize("@ss.hasPermi('labtest:jczxFeedReport:verify')")
    @Log(title = "检测中心饲料报制作", businessType = BusinessType.UPDATE)
    @PutMapping("/verify")
    public AjaxResult verify(@RequestBody OpJczxFeedReportBase reportBase) {
        return toAjax(opJczxFeedReportBaseService.verifyOpJczxFeedReport(reportBase));
    }

    /**
     * 检测中心饲料报保存/提交
     */
    //@PreAuthorize("@ss.hasPermi('labtest:jczxFeedReport:save')")
    @Log(title = "检测中心饲料报制作", businessType = BusinessType.UPDATE)
    @PutMapping("/save")
    public AjaxResult save(@RequestBody OpJczxFeedReportBase reportBase) {
        return success(opJczxFeedReportBaseService.saveOpJczxFeedReport(reportBase));
    }


    /**
     * 检测中心饲料报制作
     */
    //@PreAuthorize("@ss.hasPermi('labtest:jczxFeedReport:commit')")
    @Log(title = "检测中心饲料报制作", businessType = BusinessType.UPDATE)
    @PutMapping("/commit")
    public AjaxResult commit(@RequestBody OpJczxFeedReportBase reportBase) {
        return toAjax(opJczxFeedReportBaseService.commitOpJczxFeedReport(reportBase));
    }

    /**
     * 检测中心饲料报告pdf保存
     */
    //@PreAuthorize("@ss.hasPermi('labtest:jczxFeedReport:savePdf')")
    @Log(title = "检测中心饲料报告pdf保存", businessType = BusinessType.UPDATE)
    @PutMapping("/savePdf")
    public AjaxResult savePdf(@RequestBody OpJczxFeedReportBase reportBase) {
        return toAjax(opJczxFeedReportBaseService.saveOpJczxFeedReportPdf(reportBase));
    }

    /**
     * 检测中心饲料报发送
     */
//     //@PreAuthorize("@ss.hasPermi('labtest:jczxFeedReport:send')")
    @Log(title = "检测中心饲料报告发送邮件", businessType = BusinessType.UPDATE)
    @PostMapping("/send")
    public AjaxResult send(@RequestBody List<EmailDTO> emailDTO) throws IOException {
        return toAjax(opJczxFeedReportBaseService.sendOpJczxFeedReport2(emailDTO));
    }


    /**
     * 查询发送报告邮箱
     */
    //@PreAuthorize("@ss.hasPermi('labtest:jczxFeedReport:emailList')")
    @GetMapping("/emailList/{feedEntrustOrderId}")
    public AjaxResult emailList(@PathVariable("feedEntrustOrderId") String feedEntrustOrderId) {
        ReportSendInfoVo list = opJczxFeedReportBaseService.selectOpJczxFeedReportBaseEmailList(feedEntrustOrderId);
        return success(list);
    }

    /**
     * 查询发送报告邮箱
     */
    //@PreAuthorize("@ss.hasPermi('labtest:jczxFeedReport:list')")
    @GetMapping("/getStatusCount")
    public AjaxResult getStatusCount(OpJczxFeedReportDto opJczxFeedReportBase) {
        Map<String, Integer> list = opJczxFeedReportBaseService.getStatusCount(opJczxFeedReportBase);
        return success(list);
    }

    //@PreAuthorize("@ss.hasPermi('labtest:jczxFeedReport:getReport')")
    @GetMapping("/getReport/{opJczxFeedReportBaseId}")
    public AjaxResult getReport(@PathVariable("opJczxFeedReportBaseId") String opJczxFeedReportBaseId) {
        return success(opJczxFeedReportBaseService.getReport(opJczxFeedReportBaseId));
    }

    // 一键编制
    @PostMapping("/addAll")
    public AjaxResult addAll(@RequestBody OpJczxFeedReportDto opJczxFeedReportBase) {
        opJczxFeedReportBaseService.addAll(opJczxFeedReportBase);
        return success();
    }

    //作废报告，根据报告id
    @PostMapping("/invalidateReport")
    public AjaxResult invalidateReport(@RequestBody OpJczxFeedReportDto opJczxFeedReportBase)
    {
        if(StringUtils.isEmpty(opJczxFeedReportBase.getOpJczxFeedReportBaseId())){
            return error("缺少参数");
        }
        opJczxFeedReportBaseService.invalidateReport(opJczxFeedReportBase);
        return success();
    }


}
