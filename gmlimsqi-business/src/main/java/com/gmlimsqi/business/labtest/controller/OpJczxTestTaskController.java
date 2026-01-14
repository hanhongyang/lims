package com.gmlimsqi.business.labtest.controller;

import cn.hutool.core.collection.CollectionUtil;
import com.gmlimsqi.business.labtest.domain.*;
import com.gmlimsqi.business.labtest.dto.OpJczxBloodResultDto;
import com.gmlimsqi.business.labtest.dto.OpJczxPcrResultDto;
import com.gmlimsqi.business.labtest.dto.OpJczxTestTaskDto;
import com.gmlimsqi.business.labtest.service.IOpFeedEntrustOrderSampleService;
import com.gmlimsqi.business.labtest.service.IOpJczxTestTaskService;
import com.gmlimsqi.business.labtest.vo.OpJczxTestTaskVo;
import com.gmlimsqi.common.annotation.Anonymous;
import com.gmlimsqi.common.annotation.Log;
import com.gmlimsqi.common.core.controller.BaseController;
import com.gmlimsqi.common.core.domain.AjaxResult;
import com.gmlimsqi.common.core.page.TableDataInfo;
import com.gmlimsqi.common.enums.BusinessType;
import com.gmlimsqi.common.utils.StringUtils;
import com.gmlimsqi.common.utils.poi.ExcelUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 检测中心检测任务Controller
 * 
 * @author hhy
 * @date 2025-09-22
 */
@RestController
@RequestMapping("/labtest/task")
public class OpJczxTestTaskController extends BaseController
{
    @Autowired
    private IOpJczxTestTaskService opJczxTestTaskService;
    @Autowired
    private IOpFeedEntrustOrderSampleService feedEntrustOrderSampleService;
    /**
     * 查询检测中心检测任务列表
     */
    //@PreAuthorize("@ss.hasPermi('labtest:task:list')")
    @GetMapping("/list")
    public TableDataInfo list(OpJczxTestTaskDto opJczxTestTaskDto)
    {
        startPage();
        List<OpJczxTestTaskVo> list = opJczxTestTaskService.selectOpJczxTestTaskList(opJczxTestTaskDto);
        return getDataTable(list);
    }

    /**
     * 近红外检测任务
     */
    //@PreAuthorize("@ss.hasPermi('labtest:task:list')")
    @Anonymous
    @GetMapping("/jhwList")
    public TableDataInfo jhwList(OpJczxTestTaskDto opJczxTestTaskDto)
    {
        startPage();
        List<OpJczxTestTaskVo> list = opJczxTestTaskService.selectJhwTaskList(opJczxTestTaskDto);
        return getDataTable(list);
    }

    /**
     * 导出检测中心检测任务列表
     */
    //@PreAuthorize("@ss.hasPermi('labtest:task:export')")
    @Log(title = "检测中心检测任务", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, OpJczxTestTaskDto opJczxTestTaskDto)
    {
        List<OpJczxTestTaskVo> list = opJczxTestTaskService.selectOpJczxTestTaskList(opJczxTestTaskDto);
        ExcelUtil<OpJczxTestTaskVo> util = new ExcelUtil<OpJczxTestTaskVo>(OpJczxTestTaskVo.class);
        util.exportExcel(response, list, "检测中心检测任务数据");
    }

    /**
     * 获取检测中心检测任务详细信息
     */
   // @PreAuthorize("@ss.hasPermi('labtest:task:query')")
    @GetMapping(value = "/{opJczxTestTaskId}")
    public AjaxResult getInfo(@PathVariable("opJczxTestTaskId") String opJczxTestTaskId)
    {
        return success(opJczxTestTaskService.selectOpJczxTestTaskByOpJczxTestTaskId(opJczxTestTaskId));
    }


    /**
     * 开始PCR化验
     */
    @PostMapping("/beginPcrTask")
    public AjaxResult beginPcrTask(@RequestBody OpJczxTestTaskDto dto) {
        if(CollectionUtil.isEmpty(dto.getEntrustOrderNoList())){
            throw new RuntimeException("未选择委托单");
        }

        try {
            String resultNo = opJczxTestTaskService.beginPcrTask(dto);
            Map<String,String> data = new HashMap<>();
            data.put("resultNo",resultNo);
            return AjaxResult.success(data);
        } catch (Exception e) {
            logger.error("开始PCR化验失败", e);
            return AjaxResult.error("开始化验失败");
        }
    }

    /**
     * 开始血样化验化验
     */
    @PostMapping("/beginBloodTask")
    public AjaxResult beginBloodTask(@RequestBody List<String> entrustOrderIdList) {
        if(CollectionUtil.isEmpty(entrustOrderIdList)){
            throw new RuntimeException("未选择委托单");
        }

        try {
            String resultNo = opJczxTestTaskService.beginBloodTask(entrustOrderIdList);
            Map<String,String> data = new HashMap<>();
            data.put("resultNo",resultNo);
            return AjaxResult.success(data);
        } catch (Exception e) {
            logger.error("开始血样化验失败", e);
            return AjaxResult.error("开始化验失败");
        }
    }

    /**
     * 下载PCR检测Excel
     */
    @Log(title = "下载PCR检测表格", businessType = BusinessType.EXPORT)
    @PostMapping("/downloadPcrTask")
    public void downloadPcrTask(HttpServletResponse response, @RequestBody OpJczxTestTaskDto dto) {


        try {
            opJczxTestTaskService.generatePcrTestExcel(response, dto);
        } catch (Exception e) {
            logger.error("生成PCR检测Excel失败", e);
            throw new RuntimeException("生成检测表格失败");
        }
    }

    /**
     * 下载血样检测Excel
     */
    @Log(title = "下载血样检测表格", businessType = BusinessType.EXPORT)
    @PostMapping("/downloadBloodTask")
    public void downloadBloodTask(HttpServletResponse response, @RequestBody OpJczxTestTaskDto dto) {


        try {
            opJczxTestTaskService.generateBloodTestExcel(response, dto);
        } catch (Exception e) {
            logger.error("生成PCR检测Excel失败", e);
            throw new RuntimeException("生成检测表格失败");
        }
    }

    /**
     * 修改样品化验PCR
     */
 //   @PreAuthorize("@ss.hasPermi('labtest:task:edit')")
    @PostMapping("/savePcrResultInfoList")
    public AjaxResult savePcrResultInfoList(@RequestBody OpJczxPcrResultDto param)
    {
        opJczxTestTaskService.savePcrResultInfoList(param);
        return success();
    }

    /**
     * 修改样品化验PCR
     */
  //  @PreAuthorize("@ss.hasPermi('labtest:task:edit')")
    @PostMapping("/saveBloodResultInfoList")
    public AjaxResult saveBloodResultInfoList(@RequestBody OpJczxBloodResultDto param)
    {
        opJczxTestTaskService.saveBloodResultInfoList(param);
        return success();
    }



    @Log(title = "下载近红外检测表格", businessType = BusinessType.EXPORT)
    @PostMapping("/downloadNirTask")
    public void downloadNirTask(HttpServletResponse response, @RequestBody List<String> entrustOrderIds) {
        if(CollectionUtil.isEmpty(entrustOrderIds)){
            throw new RuntimeException("未选择委托单");
        }

        try {
            opJczxTestTaskService.generateNirTestExcel(response, entrustOrderIds);
        } catch (Exception e) {
            logger.error("生成近红外检测Excel失败", e);
            throw new RuntimeException("生成检测表格失败");
        }
    }


    /**
     * 上传近红外报告
     * (修改：按委托单ID上传)
     */
    @PostMapping("/uploadNirReport")
    public AjaxResult uploadNirReport(@RequestBody OpFeedEntrustOrderSample feedEntrustOrderSample)
    {
        if(StringUtils.isEmpty(feedEntrustOrderSample.getFileId()) ||
                (StringUtils.isEmpty(feedEntrustOrderSample.getFeedEntrustOrderId()))){
            return error("缺少参数");
        }

        // 调用新的Service方法
        opJczxTestTaskService.uploadNirReportForOrder(feedEntrustOrderSample.getFeedEntrustOrderId(), feedEntrustOrderSample.getFileId());
        return success();
    }
}
