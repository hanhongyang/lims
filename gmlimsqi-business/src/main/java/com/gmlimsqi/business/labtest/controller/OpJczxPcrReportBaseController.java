package com.gmlimsqi.business.labtest.controller;

import com.gmlimsqi.business.labtest.domain.OpJczxPcrReportBase;
import com.gmlimsqi.business.labtest.domain.OpPcrEntrustOrder;
import com.gmlimsqi.business.labtest.dto.OpJczxFeedReportDto;
import com.gmlimsqi.business.labtest.dto.OpJczxTestTaskDto;
import com.gmlimsqi.business.labtest.dto.PcrReportSendDTO;
import com.gmlimsqi.business.labtest.mapper.OpPcrEntrustOrderMapper;
import com.gmlimsqi.business.labtest.service.IOpJczxPcrReportBaseService;
import com.gmlimsqi.business.labtest.vo.OpFeedReportListVo;
import com.gmlimsqi.business.labtest.vo.OpPcrReportListVo;
import com.gmlimsqi.business.labtest.vo.ReportEmailVo;
import com.gmlimsqi.common.annotation.Log;
import com.gmlimsqi.common.core.controller.BaseController;
import com.gmlimsqi.common.core.domain.AjaxResult;
import com.gmlimsqi.common.core.page.TableDataInfo;
import com.gmlimsqi.common.enums.BusinessType;
import com.gmlimsqi.common.exception.ServiceException;
import com.gmlimsqi.common.utils.StringUtils;
import com.gmlimsqi.common.utils.poi.ExcelUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * pcr报告主Controller
 * 
 * @author hhy
 * @date 2025-10-20
 */
@RestController
@RequestMapping("/labtest/jczxPcrReport")
public class OpJczxPcrReportBaseController extends BaseController
{
    @Autowired
    private IOpJczxPcrReportBaseService opJczxPcrReportBaseService;
    @Autowired
    private OpPcrEntrustOrderMapper opPcrEntrustOrderMapper;

    /**
     * 查询pcr报告主列表
     */
    //@PreAuthoriz("@ss.hasPermi('labtest:jczxPcrReport:list')")
    @GetMapping("/list")
    public TableDataInfo list(OpJczxTestTaskDto opJczxTestTaskDto)
    {
        startPage();
        List<OpPcrReportListVo> list = opJczxPcrReportBaseService.selectOpJczxPcrReportBaseList(opJczxTestTaskDto);
        return getDataTable(list);
    }

    @GetMapping("/query")
    public TableDataInfo query(OpJczxTestTaskDto opJczxTestTaskDto) {
        startPage();
        List<OpPcrReportListVo> list = opJczxPcrReportBaseService.queryPcrReport(opJczxTestTaskDto);
        System.out.println(opJczxTestTaskDto.getEndtestTime());
        System.out.println(opJczxTestTaskDto.getBeginTestDate());
        return getDataTable(list);
    }

    /**
     * 导出pcr报告主列表
     */
    //@PreAuthoriz("@ss.hasPermi('labtest:jczxPcrReport:export')")
    @Log(title = "pcr报告主", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, OpJczxTestTaskDto opJczxTestTaskDto)
    {
        List<OpPcrReportListVo> list = opJczxPcrReportBaseService.selectOpJczxPcrReportBaseList(opJczxTestTaskDto);
        ExcelUtil<OpPcrReportListVo> util = new ExcelUtil<OpPcrReportListVo>(OpPcrReportListVo.class);
        util.exportExcel(response, list, "pcr报告主数据");
    }

    /**
     * 获取pcr报告主详细信息
     */
    //@PreAuthoriz("@ss.hasPermi('labtest:jczxPcrReport:query')")
    @GetMapping("/getInfo")
    public AjaxResult getInfo(OpJczxPcrReportBase opJczxPcrReportBase)
    {
        return success(opJczxPcrReportBaseService.selectOpJczxPcrReportBase(opJczxPcrReportBase));
    }

    /**
     * 新增pcr报告主
     */
    //@PreAuthoriz("@ss.hasPermi('labtest:jczxPcrReport:add')")
    @Log(title = "pcr报告主", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody OpJczxPcrReportBase opJczxPcrReportBase)
    {
        return toAjax(opJczxPcrReportBaseService.insertOpJczxPcrReportBase(opJczxPcrReportBase));
    }

    /**
     * 修改pcr报告主
     */
    //@PreAuthoriz("@ss.hasPermi('labtest:jczxPcrReport:edit')")
    @Log(title = "pcr报告主", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody OpJczxPcrReportBase opJczxPcrReportBase)
    {
        return toAjax(opJczxPcrReportBaseService.updateOpJczxPcrReportBase(opJczxPcrReportBase));
    }

    /**
     * 删除pcr报告主
     */
    //@PreAuthoriz("@ss.hasPermi('labtest:jczxPcrReport:remove')")
    @Log(title = "pcr报告主", businessType = BusinessType.DELETE)
	@DeleteMapping("/{opJczxPcrReportBaseIds}")
    public AjaxResult remove(@PathVariable String[] opJczxPcrReportBaseIds)
    {
        return toAjax(opJczxPcrReportBaseService.deleteOpJczxPcrReportBaseByOpJczxPcrReportBaseIds(opJczxPcrReportBaseIds));
    }


    /**
     * 审核pcr报告主
     */
    //@PreAuthoriz("@ss.hasPermi('labtest:jczxPcrReport:edit')")
    @Log(title = "pcr报告主", businessType = BusinessType.UPDATE)
    @PutMapping("'/verify")
    public AjaxResult verify(@RequestBody OpJczxPcrReportBase opJczxPcrReportBase)
    {
        return toAjax(opJczxPcrReportBaseService.verifyOpJczxPcrReportBase(opJczxPcrReportBase));
    }

    /**
     * 校准pcr报告主
     */
    //@PreAuthoriz("@ss.hasPermi('labtest:jczxPcrReport:edit')")
    @Log(title = "pcr报告主", businessType = BusinessType.UPDATE)
    @PutMapping("'/commit")
    public AjaxResult commit(@RequestBody OpJczxPcrReportBase opJczxPcrReportBase)
    {
        return toAjax(opJczxPcrReportBaseService.commitOpJczxPcrReportBase(opJczxPcrReportBase));
    }



    /**
     * 下载PCR报告Excel
     */
    @Log(title = "下载PCR检测报告", businessType = BusinessType.EXPORT)
    @PostMapping("/downloadPcrReport")
    public void downloadPcrReport(HttpServletResponse response, @RequestBody OpPcrEntrustOrder dto) { // <--- 修改签名

        try {
            if (dto == null || StringUtils.isEmpty(dto.getOpPcrEntrustOrderId())) {
                logger.error("下载PCR报告失败：缺少委托单ID");
                // 抛出异常，让 GlobalExceptionHandler 处理
                throw new ServiceException("缺少委托单ID");
            }
            if (StringUtils.isEmpty(dto.getPcrTaskItemType())) {
                logger.error("下载PCR报告失败：缺少项目类型");
                throw new ServiceException("缺少项目类型");
            }
            // *** 修改点：直接调用 Service，传入 response ***
            opJczxPcrReportBaseService.generatePcrReportExcel(response, dto);

            logger.info("Controller 方法 downloadPcrReport 已成功调用 service.generatePcrReportExcel");

        } catch (Exception e) {
            logger.error("在 Controller 层捕获到生成 Excel 异常", e);
            // 重新抛出，让 GlobalExceptionHandler 统一处理
            // (你之前修改的 GlobalExceptionHandler 会为 /download 请求返回 null)
            throw new RuntimeException("生成报告时发生内部错误: " + e.getMessage(), e);
        }
    }

    /**
     * 发送pcr报告
     */
//    //@PreAuthoriz("@ss.hasPermi('labtest:jczxPcrReport:edit')")
    @Log(title = "发送pcr报告", businessType = BusinessType.UPDATE)
    @PostMapping("/sendPcrReport")
    public AjaxResult sendPcrReport(@RequestBody PcrReportSendDTO pcrReportSendDTO)
    {
        return toAjax(opJczxPcrReportBaseService.sendOpPcrReportBase(pcrReportSendDTO));
    }

    /**
     * 批量发送PCR报告
     * @param pcrReportSendDTOList
     * @return 成功发送数量
     */
    @Log(title = "批量发送pcr报告", businessType = BusinessType.UPDATE)
    @PostMapping("/batchSendPcrReport")
    public AjaxResult batchSendPcrReport(@RequestBody List<PcrReportSendDTO> pcrReportSendDTOList)
    {
        return toAjax(opJczxPcrReportBaseService.batchSendPcrReport(pcrReportSendDTOList));
    }

    /**
     * 查询发送报告邮箱
     */
    //@PreAuthoriz("@ss.hasPermi('labtest:jczxPcrReport:emailList')")
    @GetMapping("/emailList/{opPcrEntrustOrderId}")
    public AjaxResult emailList(@PathVariable("opPcrEntrustOrderId")String opPcrEntrustOrderId)
    {
        List<ReportEmailVo> list = opJczxPcrReportBaseService.selectOpJczxPcrReportBaseEmailList(opPcrEntrustOrderId);
        return success(list);
    }

    /**
     * 获取PCR报告详情（预览弹窗用）
     * 返回 Map:
     * - orderData: 订单详情（包含构造好的虚拟行）
     * - headerList: 排序后的检测项目表头
     */
    //@PreAuthoriz("@ss.hasPermi('labtest:jczxPcrReport:query')")
    @PostMapping("/getReportDetail")
    public AjaxResult getReportDetail(@RequestBody OpPcrEntrustOrder dto) {
        if (StringUtils.isEmpty(dto.getOpPcrEntrustOrderId())) {
            return AjaxResult.error("缺少委托单ID");
        }
        if (StringUtils.isEmpty(dto.getPcrTaskItemType())) {
            return AjaxResult.error("缺少项目类型");
        }
        // 调用 Service，返回 Map
        return AjaxResult.success(opJczxPcrReportBaseService.selectPcrReportDetail2(dto));
    }
}
