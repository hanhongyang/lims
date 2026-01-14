package com.gmlimsqi.business.labtest.controller;

import com.gmlimsqi.business.labtest.domain.OpJczxBloodReportBase;
import com.gmlimsqi.business.labtest.dto.BloodEmailDTO;
import com.gmlimsqi.business.labtest.service.IOpJczxBloodReportBaseService;
import com.gmlimsqi.business.labtest.vo.ReportEmailVo;
import com.gmlimsqi.common.annotation.Log;
import com.gmlimsqi.common.core.controller.BaseController;
import com.gmlimsqi.common.core.domain.AjaxResult;
import com.gmlimsqi.common.core.page.TableDataInfo;
import com.gmlimsqi.common.enums.BusinessType;
import com.gmlimsqi.common.utils.StringUtils;
import com.gmlimsqi.common.utils.poi.ExcelUtil;
import com.gmlimsqi.framework.config.ServerConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * 检测中心血样报告主Controller
 * 
 * @author hhy
 * @date 2025-10-22
 */
@RestController
@RequestMapping("/labtest/jczxBloodReport")
public class OpJczxBloodReportBaseController extends BaseController
{
    @Autowired
    private IOpJczxBloodReportBaseService opJczxBloodReportBaseService;
    @Autowired
    private ServerConfig serverConfig;
    /**
     * 查询检测中心血样报告主列表
     */
   // @PreAuthorize("@ss.hasPermi('labtest:base:list')")
    @GetMapping("/list")
    public TableDataInfo list(OpJczxBloodReportBase opJczxBloodReportBase)
    {
        startPage();
        List<OpJczxBloodReportBase> list = opJczxBloodReportBaseService.selectOpJczxBloodReportBaseList(opJczxBloodReportBase);
        String url = serverConfig.getUrl();
        for (OpJczxBloodReportBase jczxBloodReportBase : list) {
            if(!StringUtils.isEmpty(jczxBloodReportBase.getPdfFileInfo())){
                jczxBloodReportBase.setPdfFileInfo(url + jczxBloodReportBase.getPdfFileInfo());
            }

        }
        return getDataTable(list);
    }

    /**
     * 疾病查询
     * @param opJczxBloodReportBase 搜索条件
     * @return 分页结果
     */
    @GetMapping("/diseaseQuery")
    public TableDataInfo diseaseQuery(OpJczxBloodReportBase opJczxBloodReportBase){
        startPage();
        List<OpJczxBloodReportBase> list = opJczxBloodReportBaseService.queryDiseaseReport(opJczxBloodReportBase);
        return getDataTable(list);
    }

    /**
     * 查询早孕报告
     * @param opJczxBloodReportBase 搜索条件
     * @return 分页结果
     */
    @GetMapping("/earlyPregnancyQuery")
    public TableDataInfo earlyPregnancyQuery(OpJczxBloodReportBase opJczxBloodReportBase){
        startPage();
        List<OpJczxBloodReportBase> list = opJczxBloodReportBaseService.queryEarlyPregnancyReport(opJczxBloodReportBase);
        return getDataTable(list);
    }

    /**
     * 查询生化报告
     * @param opJczxBloodReportBase 查询条件
     * @return 分页结果
     */
    @GetMapping("/biochemistryQuery")
    public TableDataInfo biochemistryQuery(OpJczxBloodReportBase opJczxBloodReportBase){
        startPage();
        List<OpJczxBloodReportBase> list = opJczxBloodReportBaseService.queryBiochemistryReport(opJczxBloodReportBase);
        return getDataTable(list);
    }


    /**
     * 导出检测中心血样报告主列表
     */
   // @PreAuthorize("@ss.hasPermi('labtest:base:export')")
    @Log(title = "检测中心血样报告主", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, OpJczxBloodReportBase opJczxBloodReportBase)
    {
        List<OpJczxBloodReportBase> list = opJczxBloodReportBaseService.selectOpJczxBloodReportBaseList(opJczxBloodReportBase);
        ExcelUtil<OpJczxBloodReportBase> util = new ExcelUtil<OpJczxBloodReportBase>(OpJczxBloodReportBase.class);
        util.exportExcel(response, list, "检测中心血样报告主数据");
    }

    /**
     * 获取检测中心血样报告主详细信息
     */
  //  @PreAuthorize("@ss.hasPermi('labtest:base:query')")
    @GetMapping("/getInfo")
    public AjaxResult getInfo(OpJczxBloodReportBase opJczxBloodReportBase)
    {
        return success(opJczxBloodReportBaseService.selectOpJczxBloodReportBaseByOpJczxBloodReportBaseId(opJczxBloodReportBase));
    }


    /**
     * 检测中心血样报告主 保存/提交
     */
   // @PreAuthorize("@ss.hasPermi('labtest:base:add')")
    @Log(title = "检测中心血样报告主", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody OpJczxBloodReportBase opJczxBloodReportBase)
    {
        return success(opJczxBloodReportBaseService.insertOpJczxBloodReportBase(opJczxBloodReportBase));
    }

    /**
     * 修改检测中心血样报告主
     */
  //  @PreAuthorize("@ss.hasPermi('labtest:base:edit')")
    @Log(title = "检测中心血样报告主", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody OpJczxBloodReportBase opJczxBloodReportBase)
    {
        return toAjax(opJczxBloodReportBaseService.updateOpJczxBloodReportBase(opJczxBloodReportBase));
    }


    /**
     * 审核检测中心血样报告主
     */
  //  @PreAuthorize("@ss.hasPermi('labtest:base:verify')")
    @Log(title = "检测中心血样报告主", businessType = BusinessType.UPDATE)
    @PutMapping("/verify")
    public AjaxResult verify(@RequestBody OpJczxBloodReportBase opJczxBloodReportBase)
    {
        return toAjax(opJczxBloodReportBaseService.verifyOpJczxBloodReportBase(opJczxBloodReportBase));
    }

    /**
     * 校准检测中心血样报告主
     */
    //@PreAuthorize("@ss.hasPermi('labtest:base:commit')")
    @Log(title = "检测中心血样报告主", businessType = BusinessType.UPDATE)
    @PutMapping("/commit")
    public AjaxResult commit(@RequestBody OpJczxBloodReportBase opJczxBloodReportBase)
    {
        return toAjax(opJczxBloodReportBaseService.commitOpJczxBloodReportBase(opJczxBloodReportBase));
    }

    /**
     * 删除检测中心血样报告主
     */
   // @PreAuthorize("@ss.hasPermi('labtest:base:remove')")
    @Log(title = "检测中心血样报告主", businessType = BusinessType.DELETE)
	@DeleteMapping("/{opJczxBloodReportBaseIds}")
    public AjaxResult remove(@PathVariable String[] opJczxBloodReportBaseIds)
    {
        return toAjax(opJczxBloodReportBaseService.deleteOpJczxBloodReportBaseByOpJczxBloodReportBaseIds(opJczxBloodReportBaseIds));
    }

    /**
     * 下载早孕报告excel
     */
    @Log(title = "下载早孕报告excel", businessType = BusinessType.EXPORT)
    @PostMapping("/downloadZaoyunReportExcel")
    public void downloadZaoyunReportExcel(HttpServletResponse response, @RequestBody OpJczxBloodReportBase opJczxBloodReportBase) {

        try {
            opJczxBloodReportBaseService.downloadZaoyunReportExcel(response, opJczxBloodReportBase);
        } catch (Exception e) {
            logger.error("生成早孕报告Excel失败", e);
            throw new RuntimeException("生成早孕报告表格失败");
        }
    }

    /**
     * 下载生化报告excel
     */
    @Log(title = "下载生化报告excel", businessType = BusinessType.EXPORT)
    @PostMapping("/downloadBiochemistryReportExcel")
    public void downloadBiochemistryReportExcel(HttpServletResponse response, @RequestBody OpJczxBloodReportBase opJczxBloodReportBase) {


        try {
            opJczxBloodReportBaseService.downloadBiochemistryReportExcel(response, opJczxBloodReportBase);
        } catch (Exception e) {
            logger.error("生成生化报告Excel失败", e);
            throw new RuntimeException("生成生化报告表格失败");
        }
    }

    /**
     * 查询发送报告邮箱
     */
  //  @PreAuthorize("@ss.hasPermi('labtest:jczxBloodReport:emailList')")
    @GetMapping("/emailList/{bloodEntrustOrderId}")
    public TableDataInfo emailList(@PathVariable("bloodEntrustOrderId")String bloodEntrustOrderId)
    {
        startPage();
        List<ReportEmailVo> list = opJczxBloodReportBaseService.selectOpJczxBloodReportBaseEmailList(bloodEntrustOrderId);
        return getDataTable(list);
    }

    /**
     * 保存pdf文件信息
     */
 //   @PreAuthorize("@ss.hasPermi('labtest:base:add')")
    @Log(title = "检测中心血样报告", businessType = BusinessType.INSERT)
    @PostMapping("/savePdfFileInfo")
    public AjaxResult savePdfFileInfo(@RequestBody OpJczxBloodReportBase opJczxBloodReportBase)
    {
        return success(opJczxBloodReportBaseService.updateOpJczxBloodReportBase(opJczxBloodReportBase));
    }

    /**
     * 检测中心报告发送邮件
     */
//     @PreAuthorize("@ss.hasPermi('labtest:jczxBloodReport:send')")
    @Log(title = "检测中心报告发送邮件", businessType = BusinessType.UPDATE)
    @PostMapping("/send")
    public AjaxResult send(@RequestBody List<BloodEmailDTO> emailDTOList) throws IOException {
        return toAjax(opJczxBloodReportBaseService.sendOpJczxBloodReport(emailDTOList));
    }

    /**
     * 查询发送报告邮箱
     */
  //  @PreAuthorize("@ss.hasPermi('labtest:jczxBloodReport:list')")
    @GetMapping("/getStatusCount")
    public AjaxResult getStatusCount(OpJczxBloodReportBase opJczxBloodReportBase)
    {
        Map<String,Integer> list = opJczxBloodReportBaseService.getStatusCount(opJczxBloodReportBase);
        return success(list);
    }

    /**
     * 获取检测中心血样报告主详细信息
     */
  //  @PreAuthorize("@ss.hasPermi('labtest:jczxBloodReport:getReport')")
    @GetMapping("/getReport/{opJczxBloodReportBaseId}")
    public AjaxResult getReport(@PathVariable("opJczxBloodReportBaseId") String opJczxBloodReportBaseId)
    {
        return success(opJczxBloodReportBaseService.getReport(opJczxBloodReportBaseId));
    }

    //一键编制
    @PostMapping("/addAll")
    public AjaxResult addAll(@RequestBody OpJczxBloodReportBase opJczxBloodReportBase)
    {
        opJczxBloodReportBaseService.addAll(opJczxBloodReportBase);
        return success();
    }

    //早孕报告退回
    @PostMapping("/zyBack")
    public AjaxResult zyBack(@RequestBody OpJczxBloodReportBase opJczxBloodReportBase)
    {
        opJczxBloodReportBaseService.zyBack(opJczxBloodReportBase);
        return success();
    }

}
