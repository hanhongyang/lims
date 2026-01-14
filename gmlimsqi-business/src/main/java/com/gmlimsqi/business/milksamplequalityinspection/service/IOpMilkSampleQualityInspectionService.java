package com.gmlimsqi.business.milksamplequalityinspection.service;

import com.gmlimsqi.business.milksamplequalityinspection.domain.ExitInspectionReportDTO;
import com.gmlimsqi.business.milksamplequalityinspection.domain.ExitInspectionReportVO;
import com.gmlimsqi.business.milksamplequalityinspection.domain.OpMilkSampleQIException;
import com.gmlimsqi.business.milksamplequalityinspection.domain.OpMilkSampleQualityInspection;

import java.util.List;

/**
 * 奶样质检Service接口
 * 
 * @author hhy
 * @date 2025-11-10
 */
public interface IOpMilkSampleQualityInspectionService 
{
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
    public int updateOpMilkSampleQualityInspection(OpMilkSampleQualityInspection opMilkSampleQualityInspection);

    /**
     * 审核奶样质检单
     *
     * @param inspectionMilkTankersId 奶样质检主键
     * @return 结果
     */
    int auditOpMilkSampleQualityInspection(String inspectionMilkTankersId);

    /**
     * 奶样质检单取样
     *
     * @param opMilkSampleQualityInspection 奶样质检
     * @return 结果
     */
    int sampling(OpMilkSampleQualityInspection opMilkSampleQualityInspection);

    /**
     * 查询奶样质检异常列表
     * @param opMilkSampleQIException
     * @return
     */
    List<OpMilkSampleQIException> selectOpMilkSampleQIExceptionList(OpMilkSampleQIException opMilkSampleQIException);

    /**
     * 提交异常
     * @param opMilkSampleQIException
     * @return
     */
    int submitOpMilkSampleQIException(OpMilkSampleQIException opMilkSampleQIException);

     /**
     * 导出退出质检报告
     * @param exitInspectionReportDTO
     * @return
     */
     List<ExitInspectionReportVO> exitInspectionReport(ExitInspectionReportDTO exitInspectionReportDTO);

    /**
     * 推送奶源
     * @param opMilkSampleQualityInspectionId
     * @return
     */
    int pushMilkSource(String opMilkSampleQualityInspectionId);
}
