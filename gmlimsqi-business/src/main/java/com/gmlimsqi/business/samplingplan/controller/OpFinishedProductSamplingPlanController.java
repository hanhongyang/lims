package com.gmlimsqi.business.samplingplan.controller;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.excel.EasyExcel;
import com.gmlimsqi.business.ranch.domain.OpSamplingPlanSample;
import com.gmlimsqi.business.samplingplan.SamplingPlanImportListener;
import com.gmlimsqi.business.samplingplan.pojo.dto.SamplingPlanImportExportDTO;
import com.gmlimsqi.business.samplingplan.pojo.entity.OpFinishedProductSamplingPlan;
import com.gmlimsqi.business.samplingplan.pojo.entity.OpFinishedProductSamplingPlanDetail;
import com.gmlimsqi.business.samplingplan.pojo.vo.ImportResultVO;
import com.gmlimsqi.business.samplingplan.pojo.vo.OpSamplingPlanSampleInfoVO;
import com.gmlimsqi.business.samplingplan.pojo.vo.OpSamplingPlanSampleVO;
import com.gmlimsqi.business.samplingplan.service.IOpFinishedProductSamplingPlanService;
import com.gmlimsqi.common.core.domain.R;
import com.gmlimsqi.common.exception.ServiceException;
import com.gmlimsqi.common.utils.SecurityUtils;
import com.gmlimsqi.common.utils.uuid.UUID;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.gmlimsqi.common.annotation.Log;
import com.gmlimsqi.common.core.controller.BaseController;
import com.gmlimsqi.common.core.domain.AjaxResult;
import com.gmlimsqi.common.enums.BusinessType;
import com.gmlimsqi.common.utils.poi.ExcelUtil;
import com.gmlimsqi.common.core.page.TableDataInfo;
import org.springframework.web.multipart.MultipartFile;

/**
 * 成品，库存，垫料取样计划Controller
 *
 * @author hhy
 * @date 2025-11-24
 */
@RestController
@RequestMapping("/sampling/plan")
public class OpFinishedProductSamplingPlanController extends BaseController
{
    @Autowired
    private IOpFinishedProductSamplingPlanService opFinishedProductSamplingPlanService;

    /**
     * 查询成品，库存，垫料取样计划列表
     * @param opFinishedProductSamplingPlan 成品，库存，垫料取样计划
     * @return {@link List<OpFinishedProductSamplingPlan>}
     */
//    @PreAuthorize("@ss.hasPermi('sampling:plan:list')")
    @GetMapping("/list")
    public TableDataInfo list(OpFinishedProductSamplingPlan opFinishedProductSamplingPlan)
    {
        startPage();
        List<OpFinishedProductSamplingPlan> list = opFinishedProductSamplingPlanService.selectOpFinishedProductSamplingPlanList(opFinishedProductSamplingPlan);
        return getDataTable(list);
    }

    /**
     * 导出成品，库存，垫料取样计划列表
     */
//    @PreAuthorize("@ss.hasPermi('sampling:plan:export')")
    @Log(title = "成品，库存，垫料取样计划", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, OpFinishedProductSamplingPlan opFinishedProductSamplingPlan)
    {
        List<OpFinishedProductSamplingPlan> list = opFinishedProductSamplingPlanService.selectOpFinishedProductSamplingPlanList(opFinishedProductSamplingPlan);
        ExcelUtil<OpFinishedProductSamplingPlan> util = new ExcelUtil<OpFinishedProductSamplingPlan>(OpFinishedProductSamplingPlan.class);
        util.exportExcel(response, list, "成品，库存，垫料取样计划数据");
    }

    /**
     * 获取成品，库存，垫料取样计划详细信息
     */
//    @PreAuthorize("@ss.hasPermi('sampling:plan:query')")
    @GetMapping(value = "/{finishedProductSamplingPlanId}")
    public AjaxResult getInfo(@PathVariable("finishedProductSamplingPlanId") String finishedProductSamplingPlanId)
    {
        return success(opFinishedProductSamplingPlanService.selectOpFinishedProductSamplingPlanByFinishedProductSamplingPlanId(finishedProductSamplingPlanId));
    }

    /**
     * 新增成品，库存，垫料取样计划
     */
//    @PreAuthorize("@ss.hasPermi('sampling:plan:add')")
    @Log(title = "成品，库存，垫料取样计划", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody OpFinishedProductSamplingPlan opFinishedProductSamplingPlan)
    {
        return toAjax(opFinishedProductSamplingPlanService.insertOpFinishedProductSamplingPlan(opFinishedProductSamplingPlan));
    }

    /**
     * 修改成品，库存，垫料取样计划
     */
//    @PreAuthorize("@ss.hasPermi('sampling:plan:edit')")
    @Log(title = "成品，库存，垫料取样计划", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody OpFinishedProductSamplingPlan opFinishedProductSamplingPlan)
    {
        return toAjax(opFinishedProductSamplingPlanService.updateOpFinishedProductSamplingPlan(opFinishedProductSamplingPlan));
    }

    /**
     * 修改子表数据
     */
    //    @PreAuthorize("@ss.hasPermi('sampling:plan:editDetail')")
    @Log(title = "成品，库存，垫料取样计划子表", businessType = BusinessType.UPDATE)
    @PutMapping("/updateDetail")
    public AjaxResult updateDetail(@RequestBody OpFinishedProductSamplingPlanDetail detail)
    {
        return toAjax(opFinishedProductSamplingPlanService.updateOpFinishedProductSamplingPlanDetail(detail));
    }

    /**
     * 查询子表详情
     */
    //    @PreAuthorize("@ss.hasPermi('sampling:plan:queryDetail')")
    @GetMapping(value = "/detail/{finishedProductSamplingPlanDetailId}")
    public AjaxResult getDetailInfo(@PathVariable("finishedProductSamplingPlanDetailId") String finishedProductSamplingPlanDetailId)
    {
        return success(opFinishedProductSamplingPlanService.
                selectOpFinishedProductSamplingPlanDetailByFinishedProductSamplingPlanDetailId(finishedProductSamplingPlanDetailId));
    }

    /**
     * 模板下载
     * @param response
     * @throws IOException
     */
    @RequestMapping("/exportTemplate")
    public void exportTemplate(HttpServletResponse response) throws IOException {
        // 关键修复：1. 精确设置 Content-Type（xlsx 标准 MIME 类型）
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("UTF-8");

        // 关键修复：2. 明确文件名带 .xlsx 后缀，优化 Content-Disposition 语法
        String fileName = URLEncoder.encode("取样计划导入模板", "UTF-8").replaceAll("\\+", "%20");
        // 兼容所有浏览器的写法：filename 直接指定后缀，filename* 处理中文
        response.setHeader("Content-Disposition",
                String.format("attachment;filename=%s.xlsx;filename*=UTF-8''%s.xlsx", fileName, fileName));

        // 关键修复：3. 禁用缓存（避免浏览器缓存旧模板）
        response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
        response.setHeader("Pragma", "no-cache");
        response.setDateHeader("Expires", 0);

        // 生成模板（EasyExcel 3.x 写法不变，确保 DTO 注解兼容）
        EasyExcel.write(response.getOutputStream(), SamplingPlanImportExportDTO.class)
                .sheet("取样计划数据")
                .doWrite(List.of());
    }

    /**
     * 数据导入
     * @param file
     * @return
     * @throws IOException
     */
    @PostMapping(value = "/importData", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public R<ImportResultVO> importData(
            @RequestParam("file") MultipartFile file) throws IOException {
        // 1. 基础校验
        if (file.isEmpty()) {
            return R.fail("文件不能为空");
        }
        String fileName = file.getOriginalFilename();
        if (fileName == null || (!fileName.endsWith(".xlsx") && !fileName.endsWith(".xls"))) {
            return R.fail("文件格式错误，仅支持.xlsx/.xls");
        }

        // 2. 解析Excel
        SamplingPlanImportListener listener = new SamplingPlanImportListener();
        EasyExcel.read(file.getInputStream(), SamplingPlanImportExportDTO.class, listener)
                .sheet()
                .doRead();

        // 3. 处理解析结果
        List<SamplingPlanImportListener.MainWithSubsDTO> successGroups = listener.getMainWithSubsList();
        List<String> errorMsgList = listener.getErrorMsgList();

        if (!errorMsgList.isEmpty()) {
            int totalSubCount = successGroups.stream().mapToInt(g -> g.getSubList().size()).sum();
            return R.ok(new ImportResultVO(
                    false,
                    "部分数据导入失败",
                    successGroups.size(),
                    totalSubCount,
                    errorMsgList
            ));
        }

        // 4. 构建主子表实体（1:N 映射，计划类型从模板读取）
        List<OpFinishedProductSamplingPlan> planList = successGroups.stream()
                .map(group -> {
                    // 主表
                    OpFinishedProductSamplingPlan mainPlan = new OpFinishedProductSamplingPlan();
                    mainPlan.setPlanTime(group.getMainPlanTime()); // 模板字段：计划时间
                    mainPlan.setPlanType(group.getMainPlanType()); // 模板字段：计划类型（用户填写）
                    // 非模板字段默认值
                    mainPlan.setFinishedProductSamplingPlanId(UUID.randomUUID().toString().replace("-", ""));
                    mainPlan.setSamplingOrderNumber("SAM-" + System.currentTimeMillis() + "-" + UUID.randomUUID().toString().substring(0, 8));
                    mainPlan.setStatus("0"); // 待执行
                    mainPlan.setIsDelete("0"); // 未删除
                    mainPlan.setDeptId(SecurityUtils.getDeptId().toString()); // 默认部门ID（可根据实际修改）

                    // 子表列表
                    List<OpFinishedProductSamplingPlanDetail> subList = group.getSubList().stream()
                            .map(dto -> {
                                OpFinishedProductSamplingPlanDetail sub = new OpFinishedProductSamplingPlanDetail();
                                // 模板字段
                                sub.setMaterialCode(dto.getMaterialCode());
                                sub.setMaterialName(dto.getMaterialName());
                                sub.setProductionOrderNumber(dto.getProductionOrderNumber());
                                sub.setPlannedProductionVolume(dto.getPlannedProductionVolume());
                                sub.setPlannedSampleQuantity(dto.getPlannedSampleQuantity());
                                // 非模板字段默认值
                                sub.setFinishedProductSamplingPlanDetailId(UUID.randomUUID().toString().replace("-", ""));
                                sub.setFinishedProductSamplingPlanId(mainPlan.getFinishedProductSamplingPlanId()); // 关联主表
                                sub.setHaveSampleCopies("0"); // 已取样0份
                                return sub;
                            })
                            .collect(Collectors.toList());

                    mainPlan.setOpFinishedProductSamplingPlanDetailList(subList);
                    return mainPlan;
                })
                .collect(Collectors.toList());

        // 5. 批量保存（事务控制）
        try {
            opFinishedProductSamplingPlanService.batchSaveMainAndSub(planList);
        } catch (Exception e) {
            throw new ServiceException("数据保存失败：" + e.getMessage());
        }

        // 6. 返回结果
        int mainCount = planList.size();
        int subCount = planList.stream().mapToInt(p -> p.getOpFinishedProductSamplingPlanDetailList().size()).sum();
        return R.ok(new ImportResultVO(true, "全部导入成功", mainCount, subCount, List.of()));
    }

    /**
     * 审核
     */
    @PostMapping("/audit")
    public R<?> audit(@RequestBody String finishedProductSamplingPlanId) {
        opFinishedProductSamplingPlanService.audit(finishedProductSamplingPlanId);
        return R.ok();
    }

    /**
     * 查询取样计划子表列表
     */
    @GetMapping("/detailList")
    public TableDataInfo detailList(OpFinishedProductSamplingPlanDetail detail) {
        startPage();
        List<OpFinishedProductSamplingPlanDetail> detailList = opFinishedProductSamplingPlanService.
                selectOpFinishedProductSamplingPlanDetailList(detail);
        return getDataTable(detailList);
    }

    /**
     * 导出取样计划子表列表
     */
    @Log(title = "成品，库存，垫料取样计划详情", businessType = BusinessType.EXPORT)
    @PostMapping("/detailExport")
    public void detailExport(HttpServletResponse response, OpFinishedProductSamplingPlanDetail detail) {
        List<OpFinishedProductSamplingPlanDetail> list = opFinishedProductSamplingPlanService.selectOpFinishedProductSamplingPlanDetailList(detail);
        ExcelUtil<OpFinishedProductSamplingPlanDetail> util = new ExcelUtil<OpFinishedProductSamplingPlanDetail>(OpFinishedProductSamplingPlanDetail.class);
        util.exportExcel(response, list, "成品，库存，垫料取样计划详情数据");
    }

    /**
     * 查询样品列表
     */
    @GetMapping("/sampleList")
    public TableDataInfo sampleList(OpSamplingPlanSample sample) {
        startPage();
        List<OpSamplingPlanSampleVO> sampleList = opFinishedProductSamplingPlanService.
                selectOpSamplingPlanSampleList(sample);
        return getDataTable(sampleList);
    }

    /**
     * 导出样品列表
     */
    @Log(title = "样品列表", businessType = BusinessType.EXPORT)
    @PostMapping("/sampleExport")
    public void sampleExport(HttpServletResponse response, OpSamplingPlanSample sample) {
        List<OpSamplingPlanSampleVO> list = opFinishedProductSamplingPlanService.selectOpSamplingPlanSampleList(sample);
        ExcelUtil<OpSamplingPlanSampleVO> util = new ExcelUtil<OpSamplingPlanSampleVO>(OpSamplingPlanSampleVO.class);
        util.exportExcel(response, list, "样品列表数据");
    }

    /**
     * 查询取样单详情
     */
    @GetMapping("/sampleInfo")
    public R<?> sampleInfo(String opSamplingPlanSampleId) {
        OpSamplingPlanSample sample = opFinishedProductSamplingPlanService.
                selectOpSamplingPlanSampleDetail(opSamplingPlanSampleId);
        return R.ok(sample);
    }

    /**
     * 新增取样单
     */
    @PostMapping("/addSample")
    public AjaxResult addSample(@RequestBody OpSamplingPlanSample sample) {
        return toAjax(opFinishedProductSamplingPlanService.addOpSamplingPlanSample(sample));
    }

    /**
     * 修改取样单
     */
    @PostMapping("/updateSample")
    public AjaxResult updateSample(@RequestBody OpSamplingPlanSample sample) {
        return toAjax(opFinishedProductSamplingPlanService.updateOpSamplingPlanSample(sample));
    }

    /**
     * 库存和成品取样单作废
     * */
    @PutMapping("/cancelfinishedProduct/{finishedProductSamplingPlanId}")
    public AjaxResult cancel(@PathVariable String finishedProductSamplingPlanId)
    {
        return toAjax(opFinishedProductSamplingPlanService.cancelOpSamplingPlan(finishedProductSamplingPlanId));
    }
}
