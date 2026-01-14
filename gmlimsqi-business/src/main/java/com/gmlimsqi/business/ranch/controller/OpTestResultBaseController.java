// (文件: com/gmlimsqi/business/ranch/controller/OpTestResultBaseController.java)
// 【请替换您已有的 OpTestResultBaseController.java】
package com.gmlimsqi.business.ranch.controller;

import java.util.List;
import java.util.Map; // (新) 引入
import javax.servlet.http.HttpServletResponse;

import cn.hutool.core.collection.CollectionUtil;
import com.gmlimsqi.business.ranch.domain.OpSamplingPlanSample;
import com.gmlimsqi.business.ranch.dto.GetJCKCTestDTO;
import com.gmlimsqi.business.ranch.dto.OpSamplingPlanSamplePushSapDTO;
import com.gmlimsqi.business.ranch.dto.changelog.ResultChangeSaveDTO;
import com.gmlimsqi.business.ranch.vo.JckcTestVO;
import com.gmlimsqi.business.ranch.vo.SampleTaskVo;
import com.gmlimsqi.business.ranch.vo.SamplingPlanReportVO;
import com.gmlimsqi.common.annotation.Anonymous;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.gmlimsqi.common.annotation.Log;
import com.gmlimsqi.common.core.controller.BaseController;
import com.gmlimsqi.common.core.domain.AjaxResult;
import com.gmlimsqi.common.enums.BusinessType;
import com.gmlimsqi.business.ranch.domain.OpTestResultBase;
import com.gmlimsqi.business.ranch.service.IOpTestResultBaseService;
import com.gmlimsqi.common.utils.poi.ExcelUtil;
import com.gmlimsqi.common.core.page.TableDataInfo;

@RestController
@RequestMapping("/ranch/testResult")
public class OpTestResultBaseController extends BaseController {

    @Autowired
    private IOpTestResultBaseService opTestResultBaseService;

    /**
     * 查询样品化验列表 (用于Tab 2, 3, 4)
     */
//    @PreAuthorize("@ss.hasPermi('ranch:testResult:list')")
    @GetMapping("/list")
    public TableDataInfo list(OpTestResultBase opTestResultBase) {
        startPage();
        List<OpTestResultBase> list = opTestResultBaseService.selectOpTestResultBaseList(opTestResultBase);
        return getDataTable(list);
    }

    /**
     * (新) 查询待化验任务列表 (用于Tab 1)
     */
//    @PreAuthorize("@ss.hasPermi('ranch:testResult:list')")
    @GetMapping("/pendingList")
    public TableDataInfo pendingList(SampleTaskVo queryParams) {
        startPage();
        List<SampleTaskVo> list = opTestResultBaseService.selectPendingTaskList(queryParams);
        return getDataTable(list);
    }

    /**
     * 导出待检任务列表
     */
    @Log(title = "待检任务", businessType = BusinessType.EXPORT)
    @PostMapping("/pendingExport")
    public void pendingExport(HttpServletResponse response, SampleTaskVo queryParams) {
        List<SampleTaskVo> list = opTestResultBaseService.selectPendingTaskList(queryParams);
        ExcelUtil<SampleTaskVo> util = new ExcelUtil<SampleTaskVo>(SampleTaskVo.class);
        util.exportExcel(response, list, "待检任务数据");
    }

    /**
     * (新) 获取所有Tab的角标数量
     */
    @GetMapping("/counts")
    public AjaxResult getCounts(SampleTaskVo pendingParams, OpTestResultBase listParams) {
        Map<String, Long> counts = opTestResultBaseService.getTestResultCounts(pendingParams, listParams);
        return AjaxResult.success(counts);
    }

    /**
     * (新) 开始化验
     */
//    @PreAuthorize("@ss.hasPermi('ranch:testResult:add')")
    @Log(title = "开始化验", businessType = BusinessType.INSERT)
    @PostMapping("/startTest")
    public AjaxResult startTest(@RequestBody List<String> opSamplingPlanItemIds) {
        try {
            String newId = opTestResultBaseService.startTest(opSamplingPlanItemIds);
            return AjaxResult.success("操作成功", newId);
        } catch (Exception e) {
            return AjaxResult.error(e.getMessage());
        }
    }

    /**
     * (新) 提交审核
     */
//    @PreAuthorize("@ss.hasPermi('ranch:testResult:edit')")
    @Log(title = "提交化验单", businessType = BusinessType.UPDATE)
    @PutMapping("/submit")
    public AjaxResult submitTestResult(@RequestBody OpTestResultBase opTestResultBase) {
        return toAjax(opTestResultBaseService.submitTestResult(opTestResultBase));
    }

    /**
     * (新) 审核通过
     */
//    @PreAuthorize("@ss.hasPermi('ranch:testResult:approve')")
    @Log(title = "审核通过", businessType = BusinessType.UPDATE)
    @PutMapping("/approve/{id}")
    public AjaxResult approveTestResult(@PathVariable String id) {
        return toAjax(opTestResultBaseService.approveTestResult(id));
    }

    /**
     * 化验审核通过（手机端使用）
     */
    @Log(title = "化验审核通过", businessType = BusinessType.UPDATE)
    @PostMapping("/approveTest")
    public AjaxResult approveTest(@RequestBody OpTestResultBase opTestResultBase) {
        return toAjax(opTestResultBaseService.approveTest(opTestResultBase));
    }

    /**
     * (新) 审核退回
     */
//    @PreAuthorize("@ss.hasPermi('ranch:testResult:approve')")
    @Log(title = "审核退回", businessType = BusinessType.UPDATE)
    @PutMapping("/reject")
    public AjaxResult rejectTestResult(@RequestBody OpTestResultBase opTestResultBase) {
        return toAjax(opTestResultBaseService.rejectTestResult(opTestResultBase.getId(), opTestResultBase.getReturnReason()));
    }

    /**
     * (新) 取消审核
     */
//    @PreAuthorize("@ss.hasPermi('ranch:testResult:approve')")
    @Log(title = "取消审核", businessType = BusinessType.UPDATE)
    @PutMapping("/cancelApprove/{id}")
    public AjaxResult cancelApprove(@PathVariable String id) {
        return toAjax(opTestResultBaseService.cancelApprove(id));
    }


    /**
     * 导出样品化验列表
     */
//    @PreAuthorize("@ss.hasPermi('ranch:testResult:export')")
    @Log(title = "样品化验", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, OpTestResultBase opTestResultBase) {
        List<OpTestResultBase> list = opTestResultBaseService.selectOpTestResultBaseList(opTestResultBase);
        ExcelUtil<OpTestResultBase> util = new ExcelUtil<OpTestResultBase>(OpTestResultBase.class);
        util.exportExcel(response, list, "样品化验数据");
    }

    /**
     * 获取样品化验详细信息
     */
//    @PreAuthorize("@ss.hasPermi('ranch:testResult:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") String id) {
        return success(opTestResultBaseService.selectOpTestResultBaseById(id));
    }

    /**
     * 新增样品化验
     */
//    @PreAuthorize("@ss.hasPermi('ranch:testResult:add')")
    @Log(title = "样品化验", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody OpTestResultBase opTestResultBase) {
        return toAjax(opTestResultBaseService.insertOpTestResultBase(opTestResultBase));
    }

    /**
     * 修改样品化验
     */
//    @PreAuthorize("@ss.hasPermi('ranch:testResult:edit')")
    @Log(title = "样品化验", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody OpTestResultBase opTestResultBase) {
        return toAjax(opTestResultBaseService.updateOpTestResultBase(opTestResultBase));
    }

    // 复测
    @PostMapping("/retest")
    public AjaxResult retest(@RequestBody List<String> infoId) {
        if (CollectionUtil.isEmpty(infoId)) {
            return error("缺少参数");
        }
        return toAjax(opTestResultBaseService.retest(infoId));
    }

    /**
     * 查询样品未推送SAP的列表
     */
    @GetMapping("/unPushSapList")
    public AjaxResult unPushSapList(OpSamplingPlanSample opSamplingPlanSample) {
        return success(opTestResultBaseService.selectUnPushSapList(opSamplingPlanSample));
    }

    /**
     * 合格判定
     */
    @Log(title = "合格判定", businessType = BusinessType.UPDATE)
    @PutMapping("/judgePass")
    public AjaxResult judgePass(@RequestBody OpSamplingPlanSample opSamplingPlanSample) {
        return toAjax(opTestResultBaseService.judgePass(opSamplingPlanSample));
    }

    /**
     * 查询样品检测详情
     */
    @GetMapping("/sampleTestDetail/{opSamplingPlanSampleId}")
    public AjaxResult sampleTestDetail(@PathVariable("opSamplingPlanSampleId") String opSamplingPlanSampleId) {
        return success(opTestResultBaseService.sampleTestDetail(opSamplingPlanSampleId));
    }

    /**
     * 自动绑定批次
     *
     * @param opSamplingPlanSampleId
     * @return
     */
    @GetMapping("/autoBindBatch/{opSamplingPlanSampleId}")
    public AjaxResult autoBindBatch(@PathVariable("opSamplingPlanSampleId") String opSamplingPlanSampleId) {
        return toAjax(opTestResultBaseService.autoBindBatch(opSamplingPlanSampleId));
    }

    /**
     * 修改样品信息
     */
    @Log(title = "修改样品信息", businessType = BusinessType.UPDATE)
    @PutMapping("/updateSampleInfo")
    public AjaxResult updateSampleInfo(@RequestBody OpSamplingPlanSample opSamplingPlanSample) {
        return toAjax(opTestResultBaseService.updateSampleInfo(opSamplingPlanSample));
    }

    /**
     * 推送SAP
     */
    @Log(title = "推送SAP", businessType = BusinessType.UPDATE)
    @PostMapping("/pushSap")
    public AjaxResult pushSap(@RequestBody OpSamplingPlanSamplePushSapDTO opSamplingPlanSample) {
        return toAjax(opTestResultBaseService.pushSap(opSamplingPlanSample));
    }

    /**
     * 报告打印
     *
     * @param opSamplingPlanSampleId
     * @return {@link SamplingPlanReportVO}
     */
    @Anonymous
    @Log(title = "报告打印", businessType = BusinessType.OTHER)
    @GetMapping("/printReport")
    public AjaxResult printReport(String opSamplingPlanSampleId) {
        return success(opTestResultBaseService.printReport(opSamplingPlanSampleId));
    }

    /**
     * 查询牧场饲料检测记录
     *
     * @param getJCKCTestDTO
     * @return {@link List<JckcTestVO>}
     */
    @GetMapping("/getJCKCTest")
    public TableDataInfo getJCKCTest(GetJCKCTestDTO getJCKCTestDTO) {
        startPage();
        List<JckcTestVO> list = opTestResultBaseService.callProTransGetJCKCTest(getJCKCTestDTO);
        return getDataTable(list);
    }

    /**
     * 检测结果变更
     *
     * @param dto {@link ResultChangeSaveDTO}
     */
    @PutMapping("/resultChange")
    public AjaxResult resultChange(@RequestBody ResultChangeSaveDTO dto) {
        return toAjax(opTestResultBaseService.changeResult(dto));
    }
}
