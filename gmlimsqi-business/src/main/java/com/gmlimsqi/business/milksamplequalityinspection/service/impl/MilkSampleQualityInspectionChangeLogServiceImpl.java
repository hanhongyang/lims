package com.gmlimsqi.business.milksamplequalityinspection.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gmlimsqi.business.milksamplequalityinspection.domain.MilkSampleQualityInspectionChangeLogDO;
import com.gmlimsqi.business.milksamplequalityinspection.domain.OpMilkSampleQualityInspection;
import com.gmlimsqi.business.milksamplequalityinspection.mapper.MilkSampleQualityInspectionChangeLogMapper;
import com.gmlimsqi.business.milksamplequalityinspection.service.MilkSampleQualityInspectionChangeLogService;
import com.gmlimsqi.common.change.base.BaseChangeLogService;
import com.gmlimsqi.common.change.domain.ChangeCompareResult;
import com.gmlimsqi.common.exception.BusinessException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class MilkSampleQualityInspectionChangeLogServiceImpl
        extends BaseChangeLogService<OpMilkSampleQualityInspection, MilkSampleQualityInspectionChangeLogDO>
        implements MilkSampleQualityInspectionChangeLogService {

    // mapper
    private final MilkSampleQualityInspectionChangeLogMapper changeLogMapper;

    public MilkSampleQualityInspectionChangeLogServiceImpl(MilkSampleQualityInspectionChangeLogMapper changeLogMapper,
                                                           ObjectMapper objectMapper) {
        super(objectMapper);
        this.changeLogMapper = changeLogMapper;
    }

    /**
     * 获取业务主键
     */
    @Override
    protected String getBizId(OpMilkSampleQualityInspection data) {
        return data.getOpMilkSampleQualityInspectionId();
    }

    /**
     * 构建日志对象
     */
    @Override
    protected MilkSampleQualityInspectionChangeLogDO buildLog(
            String bizId,
            String opType,
            String changeReason,
            String oldDataJson,
            String newDataJson,
            String diffDataJson
    ) {
        MilkSampleQualityInspectionChangeLogDO log = new MilkSampleQualityInspectionChangeLogDO();

        // 1. 设置业务主键（每个表字段不同）
        log.setOpMilkSampleQualityInspectionId(bizId);

        // 2. 复用通用模板构建字段
        return buildBaseLog(log, opType, changeReason, oldDataJson, newDataJson, diffDataJson);
    }

    /**
     * 比对业务字段（只记录变化字段）
     */
    @Override
    protected ChangeCompareResult compare(OpMilkSampleQualityInspection oldDO,
                                          OpMilkSampleQualityInspection newDO) {

        // 1. 只记录变化字段的 old/new/diff
        Map<String, Object> oldData = new LinkedHashMap<>();
        Map<String, Object> newData = new LinkedHashMap<>();
        Map<String, Object> diffData = new LinkedHashMap<>();

        // ==================== 单据基础信息 ====================
        // 奶源计划单号
        compareField("milk_source_plan_order_number",
                oldDO.getMilkSourcePlanOrderNumber(), newDO.getMilkSourcePlanOrderNumber(),
                oldData, newData, diffData);
        // 装奶单主键
        compareField("op_milk_filling_order_id",
                oldDO.getOpMilkFillingOrderId(), newDO.getOpMilkFillingOrderId(),
                oldData, newData, diffData);
        // 奶罐车检查表主键
        compareField("inspection_milk_tankers_id",
                oldDO.getInspectionMilkTankersId(), newDO.getInspectionMilkTankersId(),
                oldData, newData, diffData);
        // 状态：0待取样 1已取样 2已审核
        compareField("status",
                oldDO.getStatus(), newDO.getStatus(),
                oldData, newData, diffData);
        // 前置是否完成
        compareField("pre_step_completed",
                oldDO.getPreStepCompleted(), newDO.getPreStepCompleted(),
                oldData, newData, diffData);
        // 车牌号
        compareField("license_plate_number",
                oldDO.getLicensePlateNumber(), newDO.getLicensePlateNumber(),
                oldData, newData, diffData);
        // 奶样质检单号
        compareField("milk_sample_quality_inspection_number",
                oldDO.getMilkSampleQualityInspectionNumber(), newDO.getMilkSampleQualityInspectionNumber(),
                oldData, newData, diffData);
        // 目的地
        compareField("destination",
                oldDO.getDestination(), newDO.getDestination(),
                oldData, newData, diffData);
        // 进场时间
        compareField("entry_time",
                oldDO.getEntryTime(), newDO.getEntryTime(),
                oldData, newData, diffData);
        // 是否取样：0否 1是
        compareField("is_sampling",
                oldDO.getIsSampling(), newDO.getIsSampling(),
                oldData, newData, diffData);
        // ==================== 取样信息 ====================
        // 取样人
        compareField("sampler_id",
                oldDO.getSamplerId(), newDO.getSamplerId(),
                oldData, newData, diffData);
        compareField("sampler",
                oldDO.getSampler(), newDO.getSampler(),
                oldData, newData, diffData);
        // 取样时间
        compareField("sampling_time",
                oldDO.getSamplingTime(), newDO.getSamplingTime(),
                oldData, newData, diffData);

        // ==================== 检测信息 ====================
        // 检测人
        compareField("tester_id",
                oldDO.getTesterId(), newDO.getTesterId(),
                oldData, newData, diffData);
        compareField("tester",
                oldDO.getTester(), newDO.getTester(),
                oldData, newData, diffData);
        // 检测日期
        compareField("test_time",
                oldDO.getTestTime(), newDO.getTestTime(),
                oldData, newData, diffData);
        // ==================== 审核信息 ====================
        // 审核人
        compareField("reviewer_id",
                oldDO.getReviewerId(), newDO.getReviewerId(),
                oldData, newData, diffData);
        compareField("reviewer",
                oldDO.getReviewer(), newDO.getReviewer(),
                oldData, newData, diffData);
        // 审核日期
        compareField("review_time",
                oldDO.getReviewTime(), newDO.getReviewTime(),
                oldData, newData, diffData);
        // ==================== 异常信息 ====================
        // 异常类型
        compareField("remark",
                oldDO.getRemark(), newDO.getRemark(),
                oldData, newData, diffData);
        // 异常类型
        compareField("exception_type",
                oldDO.getExceptionType(), newDO.getExceptionType(),
                oldData, newData, diffData);
        // 异常描述
        compareField("exception_desc",
                oldDO.getExceptionDesc(), newDO.getExceptionDesc(),
                oldData, newData, diffData);
        // 是否提交异常
        compareField("is_exception_submit",
                oldDO.getIsExceptionSubmit(), newDO.getIsExceptionSubmit(),
                oldData, newData, diffData);
        // ==================== 附件 ====================
        compareField("file",
                oldDO.getFile(), newDO.getFile(),
                oldData, newData, diffData);
        // ==================== 奶温 ====================
        compareField("milk_temperature",
                oldDO.getMilkTemperature(), newDO.getMilkTemperature(),
                oldData, newData, diffData);
        compareField("milk_temp_photo",
                oldDO.getMilkTempPhoto(), newDO.getMilkTempPhoto(),
                oldData, newData, diffData);
        // ==================== 脂肪/蛋白 ====================
        compareField("fat_percent",
                oldDO.getFatPercent(), newDO.getFatPercent(),
                oldData, newData, diffData);
        compareField("protein_percent",
                oldDO.getProteinPercent(), newDO.getProteinPercent(),
                oldData, newData, diffData);
        // ==================== 酒精 ====================
        compareField("alcohol_type",
                oldDO.getAlcoholType(), newDO.getAlcoholType(),
                oldData, newData, diffData);
        compareField("alcohol_value",
                oldDO.getAlcoholValue(), newDO.getAlcoholValue(),
                oldData, newData, diffData);
        compareField("alcohol_photo",
                oldDO.getAlcoholPhoto(), newDO.getAlcoholPhoto(),
                oldData, newData, diffData);
        // ==================== 抗生素（基础） ====================
        compareField("beta_lactam",
                oldDO.getBetaLactam(), newDO.getBetaLactam(),
                oldData, newData, diffData);
        compareField("tetracyclines",
                oldDO.getTetracyclines(), newDO.getTetracyclines(),
                oldData, newData, diffData);
        compareField("ceftiofur",
                oldDO.getCeftiofur(), newDO.getCeftiofur(),
                oldData, newData, diffData);
        compareField("cephalexin",
                oldDO.getCephalexin(), newDO.getCephalexin(),
                oldData, newData, diffData);
        compareField("antibiotic_photo",
                oldDO.getAntibioticPhoto(), newDO.getAntibioticPhoto(),
                oldData, newData, diffData);
        // ==================== 磷酸盐/酸度 ====================
        compareField("phosphate_test",
                oldDO.getPhosphateTest(), newDO.getPhosphateTest(),
                oldData, newData, diffData);
        compareField("phosphate_photo",
                oldDO.getPhosphatePhoto(), newDO.getPhosphatePhoto(),
                oldData, newData, diffData);
        compareField("acidity",
                oldDO.getAcidity(), newDO.getAcidity(),
                oldData, newData, diffData);
        compareField("acidity_photo",
                oldDO.getAcidityPhoto(), newDO.getAcidityPhoto(),
                oldData, newData, diffData);
        // ==================== 定性试验 ====================
        compareField("rose_bengal",
                oldDO.getRoseBengal(), newDO.getRoseBengal(),
                oldData, newData, diffData);
        compareField("bromothymol_blue",
                oldDO.getBromothymolBlue(), newDO.getBromothymolBlue(),
                oldData, newData, diffData);
        compareField("ferric_chloride",
                oldDO.getFerricChloride(), newDO.getFerricChloride(),
                oldData, newData, diffData);
        compareField("methylene_blue_test",
                oldDO.getMethyleneBlueTest(), newDO.getMethyleneBlueTest(),
                oldData, newData, diffData);
        compareField("blood_milk_test",
                oldDO.getBloodMilkTest(), newDO.getBloodMilkTest(),
                oldData, newData, diffData);
        compareField("blood_milk_test_photo",
                oldDO.getBloodMilkTestPhoto(), newDO.getBloodMilkTestPhoto(),
                oldData, newData, diffData);
        // ==================== E50/感官 ====================
        compareField("e50",
                oldDO.getE50(), newDO.getE50(),
                oldData, newData, diffData);
        compareField("sensory_index",
                oldDO.getSensoryIndex(), newDO.getSensoryIndex(),
                oldData, newData, diffData);
        compareField("sensory_index_photo",
                oldDO.getSensoryIndexPhoto(), newDO.getSensoryIndexPhoto(),
                oldData, newData, diffData);
        // ==================== 黄曲霉毒素 ====================
        compareField("aflatoxin_m1",
                oldDO.getAflatoxinM1(), newDO.getAflatoxinM1(),
                oldData, newData, diffData);
        compareField("aflatoxin_photo",
                oldDO.getAflatoxinPhoto(), newDO.getAflatoxinPhoto(),
                oldData, newData, diffData);
        // ==================== 其他检测项 ====================
        compareField("is_push_milk_source", // 是否推送奶源
                oldDO.getIsPushMilkSource(), newDO.getIsPushMilkSource(),
                oldData, newData, diffData);
        compareField("quinolone", // 喹诺酮
                oldDO.getQuinolone(), newDO.getQuinolone(),
                oldData, newData, diffData);
        compareField("freezing_point", // 冰点
                oldDO.getFreezingPoint(), newDO.getFreezingPoint(),
                oldData, newData, diffData);
        compareField("antibiotic_residue", // 解抗剂
                oldDO.getAntibioticResidue(), newDO.getAntibioticResidue(),
                oldData, newData, diffData);
        compareField("flunixin", // 氟尼辛
                oldDO.getFlunixin(), newDO.getFlunixin(),
                oldData, newData, diffData);
        compareField("meloxicam", // 美洛昔康
                oldDO.getMeloxicam(), newDO.getMeloxicam(),
                oldData, newData, diffData);

        compareField("streptomycin", // 链霉素/双氢链霉素
                oldDO.getStreptomycin(), newDO.getStreptomycin(),
                oldData, newData, diffData);
        compareField("kanamycin", // 卡那
                oldDO.getKanamycin(), newDO.getKanamycin(),
                oldData, newData, diffData);
        compareField("monensin", // 莫能菌素
                oldDO.getMonensin(), newDO.getMonensin(),
                oldData, newData, diffData);

        // 2. 封装结果
        ChangeCompareResult result = new ChangeCompareResult();
        result.setOldData(oldData);
        result.setNewData(newData);
        result.setDiffData(diffData);
        return result;
    }

    /**
     * 入库
     */
    @Override
    protected int doInsert(MilkSampleQualityInspectionChangeLogDO log) {
        return changeLogMapper.insert(log);
    }

    /**
     * 创建变更日志（对外接口）
     */
    @Override
    public int recordUpdateLog(OpMilkSampleQualityInspection oldData,
                               OpMilkSampleQualityInspection newData,
                               String changeReason) {
        // 直接调用父类模板方法
        return super.recordUpdateLog(oldData, newData, changeReason);
    }

    /**
     * 根据 opMilkSampleQualityInspectionId 查询
     */
    @Override
    public List<MilkSampleQualityInspectionChangeLogDO> getListByMilkId(String opMilkSampleQualityInspectionId) {
        if (!StringUtils.hasText(opMilkSampleQualityInspectionId)) {
            throw new BusinessException("opMilkSampleQualityInspectionId 不能为空");
        }
        return changeLogMapper.selectListByMilkId(opMilkSampleQualityInspectionId);
    }

    /**
     * 查询列表
     */
    @Override
    public List<MilkSampleQualityInspectionChangeLogDO> getList(MilkSampleQualityInspectionChangeLogDO query) {
        // query 可为空，代表查询全部
        return changeLogMapper.selectList(query);
    }
}