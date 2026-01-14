package com.gmlimsqi.business.samplingplan;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.exception.ExcelDataConvertException;
import com.gmlimsqi.business.samplingplan.pojo.dto.SamplingPlanImportExportDTO;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

/**
 * 取样计划Excel导入监听器（含计划类型校验，兼容EasyExcel 2.x/3.x）
 */
@Slf4j
@Getter
public class SamplingPlanImportListener extends AnalysisEventListener<SamplingPlanImportExportDTO> {

    /** 计划类型允许值集合 */
    private static final Set<String> ALLOWED_PLAN_TYPES = Set.of("0", "1", "2");

    /** 收集解析成功的「主表+子表」分组数据 */
    private final List<MainWithSubsDTO> mainWithSubsList = new ArrayList<>();

    /** 收集解析失败的信息 */
    private final List<String> errorMsgList = new ArrayList<>();

    /** 临时存储当前主表的计划时间+计划类型（分组标识） */
    private Date currentMainPlanTime;
    private String currentMainPlanType;
    /** 临时存储当前主表对应的子表列表 */
    private List<SamplingPlanImportExportDTO> currentSubList;

    /**
     * 每行数据解析成功后调用
     */
    @Override
    public void invoke(SamplingPlanImportExportDTO dto, AnalysisContext context) {
        int rowNum = context.readRowHolder().getRowIndex() + 1; // 行号从1开始

        // 1. 处理主表字段（计划时间+计划类型）继承
        if (dto.getPlanTime() != null && dto.getPlanType() != null) {
            // 新的主表：保存上一个主表分组（如果存在）
            if (currentMainPlanTime != null && currentMainPlanType != null && currentSubList != null && !currentSubList.isEmpty()) {
                mainWithSubsList.add(new MainWithSubsDTO(currentMainPlanTime, currentMainPlanType, currentSubList));
            }
            // 校验计划类型合法性
            if (!ALLOWED_PLAN_TYPES.contains(dto.getPlanType().trim())) {
                errorMsgList.add(String.format("第%s行：计划类型非法（仅允许0：成品、1：库存、2：垫料）", rowNum));
                // 非法类型不创建新分组，后续行跳过
                currentMainPlanTime = null;
                currentMainPlanType = null;
                currentSubList = null;
                return;
            }
            // 更新当前主表信息
            currentMainPlanTime = dto.getPlanTime();
            currentMainPlanType = dto.getPlanType().trim();
            currentSubList = new ArrayList<>();
        } else if (dto.getPlanTime() != null || dto.getPlanType() != null) {
            // 主表字段不完整（仅填了计划时间或仅填了计划类型）
            errorMsgList.add(String.format("第%s行：主表字段不完整（计划时间和计划类型需同时填写）", rowNum));
            return;
        } else {
            // 子表行：校验是否有对应的主表
            if (currentMainPlanTime == null || currentMainPlanType == null) {
                errorMsgList.add(String.format("第%s行：无对应主表（需先填写计划时间和计划类型）", rowNum));
                return;
            }
            // 继承主表字段
            dto.setPlanTime(currentMainPlanTime);
            dto.setPlanType(currentMainPlanType);
        }

        // 2. 子表字段校验（必填项）
        if (dto.getMaterialCode() == null || dto.getMaterialCode().trim().isEmpty()) {
            errorMsgList.add(String.format("第%s行：物料编码不能为空", rowNum));
            return;
        }
        if (dto.getPlannedSampleQuantity() == null || dto.getPlannedSampleQuantity().trim().isEmpty()) {
            errorMsgList.add(String.format("第%s行：计划取样份数不能为空", rowNum));
            return;
        }

        // 3. 加入当前主表的子表列表
        currentSubList.add(dto);
    }

    /**
     * 所有数据解析完成后调用
     */
    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        // 保存最后一个未提交的主表分组
        if (currentMainPlanTime != null && currentMainPlanType != null && currentSubList != null && !currentSubList.isEmpty()) {
            mainWithSubsList.add(new MainWithSubsDTO(currentMainPlanTime, currentMainPlanType, currentSubList));
        }
        log.info("Excel解析完成，成功解析{}个主表分组（含{}个子表数据），失败{}条",
                mainWithSubsList.size(),
                mainWithSubsList.stream().mapToInt(m -> m.getSubList().size()).sum(),
                errorMsgList.size());
    }

    /**
     * 解析异常处理
     */
    @Override
    public void onException(Exception exception, AnalysisContext context) {
        int rowNum = context.readRowHolder().getRowIndex() + 1;
        if (exception instanceof ExcelDataConvertException) {
            ExcelDataConvertException convertException = (ExcelDataConvertException) exception;
            int columnIndex = convertException.getColumnIndex() + 1; // 列索引从1开始
            errorMsgList.add(String.format("第%s行第%s列：数据格式错误（计划时间需为yyyy-MM-dd，计划类型需为0/1/2）",
                    rowNum, columnIndex, exception.getMessage()));
        } else {
            errorMsgList.add(String.format("第%s行：解析失败（原因：%s）", rowNum, exception.getMessage()));
        }
        log.error("Excel解析异常（第{}行）", rowNum, exception);
    }

    /**
     * 主子表分组DTO（包含主表计划类型）
     */
    @Getter
    public static class MainWithSubsDTO {
        private final Date mainPlanTime; // 主表-计划时间
        private final String mainPlanType; // 主表-计划类型
        private final List<SamplingPlanImportExportDTO> subList; // 子表列表

        public MainWithSubsDTO(Date mainPlanTime, String mainPlanType, List<SamplingPlanImportExportDTO> subList) {
            this.mainPlanTime = mainPlanTime;
            this.mainPlanType = mainPlanType;
            this.subList = subList;
        }
    }
}