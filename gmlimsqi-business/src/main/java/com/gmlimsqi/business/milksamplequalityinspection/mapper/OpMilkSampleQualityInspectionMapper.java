package com.gmlimsqi.business.milksamplequalityinspection.mapper;

import com.gmlimsqi.business.milksamplequalityinspection.domain.ExitInspectionReportDTO;
import com.gmlimsqi.business.milksamplequalityinspection.domain.ExitInspectionReportVO;
import com.gmlimsqi.business.milksamplequalityinspection.domain.OpMilkSampleQIException;
import com.gmlimsqi.business.milksamplequalityinspection.domain.OpMilkSampleQualityInspection;
import com.gmlimsqi.common.change.annotation.ChangeLog;
import com.gmlimsqi.common.change.constant.BizType;
import com.gmlimsqi.common.change.constant.ChangeLogConstant;

import java.util.List;

/**
 * 奶样质检Mapper接口
 *
 * @author hhy
 * @date 2025-11-10
 */
public interface OpMilkSampleQualityInspectionMapper {
    /**
     * 查询奶样质检
     *
     * @param opMilkSampleQualityInspectionId 奶样质检主键
     * @return 奶样质检
     */
    public OpMilkSampleQualityInspection selectOpMilkSampleQualityInspectionByOpMilkSampleQualityInspectionId(String opMilkSampleQualityInspectionId);

    /**
     * 查询奶样质检列表
     *
     * @param opMilkSampleQualityInspection 奶样质检
     * @return 奶样质检集合
     */
    public List<OpMilkSampleQualityInspection> selectOpMilkSampleQualityInspectionList(OpMilkSampleQualityInspection opMilkSampleQualityInspection);

    /**
     * 新增奶样质检
     *
     * @param opMilkSampleQualityInspection 奶样质检
     * @return 结果
     */
    public int insertOpMilkSampleQualityInspection(OpMilkSampleQualityInspection opMilkSampleQualityInspection);

    /**
     * 修改奶样质检
     *
     * @param opMilkSampleQualityInspection 奶样质检
     * @return 结果
     */
    @ChangeLog(
            bizType = BizType.MILK_SAMPLE_QUALITY_INSPECTION,
            opType = ChangeLogConstant.OP_TYPE_UPDATE,
            bizId = "#p0.opMilkSampleQualityInspectionId",
            selectById = "selectOpMilkSampleQualityInspectionByOpMilkSampleQualityInspectionId(#bizId)",
            reason = "#p0.remark"
    )
    public int updateOpMilkSampleQualityInspection(OpMilkSampleQualityInspection opMilkSampleQualityInspection);

    /**
     * 通过奶样质检主键更新删除标志
     *
     * @param opMilkSampleQualityInspectionId 奶样质检ID
     * @return 结果
     */
    public int updateDeleteFlagById(String opMilkSampleQualityInspectionId);

    /**
     * 批量通过奶样质检主键更新删除标志
     *
     * @param opMilkSampleQualityInspectionId 奶样质检ID
     * @return 结果
     */
    public int updateDeleteFlagByIds(String[] opMilkSampleQualityInspectionIds);

    /**
     * 根据奶罐车检查ID查询奶样质检
     */
    public OpMilkSampleQualityInspection selectOpMilkSampleQualityInspectionByInspectionMilkTankersId(String inspectionMilkTankersId);

    /**
     * 查询奶样质检异常列表
     *
     * @param opMilkSampleQIException 奶样质检异常
     * @return 奶样质检异常集合
     */
    public List<OpMilkSampleQIException> selectOpMilkSampleQIExceptionList(OpMilkSampleQIException opMilkSampleQIException);

    /**
     * 查询退出检查报告列表
     *
     * @param exitInspectionReportDTO 退出检查报告查询条件
     * @return 退出检查报告集合
     */
    List<ExitInspectionReportVO> exitInspectionReport(ExitInspectionReportDTO exitInspectionReportDTO);

}
