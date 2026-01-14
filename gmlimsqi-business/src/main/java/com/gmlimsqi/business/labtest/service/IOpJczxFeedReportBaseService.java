package com.gmlimsqi.business.labtest.service;

import com.gmlimsqi.business.labtest.domain.OpJczxFeedReportBase;
import com.gmlimsqi.business.labtest.dto.EmailDTO;
import com.gmlimsqi.business.labtest.dto.OpJczxFeedReportDto;
import com.gmlimsqi.business.labtest.vo.OpFeedReportListVo;
import com.gmlimsqi.business.labtest.vo.OpFeedReportVo;
import com.gmlimsqi.business.labtest.vo.ReportSendInfoVo;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * 检测中心饲料报告主Service接口
 * 
 * @author hhy
 * @date 2025-10-14
 */
public interface IOpJczxFeedReportBaseService 
{
    /**
     * 查询检测中心饲料报告主
     * 
     * @param opJczxFeedReportBaseId 检测中心饲料报告主主键
     * @return 检测中心饲料报告主
     */
    public OpFeedReportVo selectOpJczxFeedReportBaseByOpJczxFeedReportBaseId(String opJczxFeedReportBaseId);

    /**
     * 查询检测中心饲料报告主列表
     * 
     * @param opJczxFeedReportBase 检测中心饲料报告主
     * @return 检测中心饲料报告主集合
     */
    public List<OpFeedReportListVo> selectOpJczxFeedReportBaseList(OpJczxFeedReportDto opJczxFeedReportBase);

    /**
     * 新增检测中心饲料报告主
     * 
     * @param opJczxFeedReportBase 检测中心饲料报告主
     * @return 结果
     */
    public int insertOpJczxFeedReportBase(OpJczxFeedReportBase opJczxFeedReportBase);

    /**
     * 修改检测中心饲料报告主
     * 
     * @param opJczxFeedReportBase 检测中心饲料报告主
     * @return 结果
     */
    public int updateOpJczxFeedReportBase(OpJczxFeedReportBase opJczxFeedReportBase);

    public  int verifyOpJczxFeedReport(OpJczxFeedReportBase opFeedReportVo);
    public  int commitOpJczxFeedReport(OpJczxFeedReportBase opFeedReportVo);

    /**
     * 获取报告制作时的信息
     */
    OpFeedReportVo selecReportMakeInfoByEntrustOrderSampleId(String entrustOrderSampleId);

    String saveOpJczxFeedReport(OpJczxFeedReportBase reportBase);

    /**
     * 发送检测中心饲料报告邮件
     * @param emailDTOList
     * @return
     */
    int sendOpJczxFeedReport(List<EmailDTO> emailDTOList) throws IOException;
    int sendOpJczxFeedReport2(List<EmailDTO> emailDTOList) throws IOException;
    /**
     * 保存检测中心饲料报告pdf
     * @param reportBase
     * @return
     */
    int saveOpJczxFeedReportPdf(OpJczxFeedReportBase reportBase);

    /**
     * 查询发送报告邮箱
     */
    ReportSendInfoVo selectOpJczxFeedReportBaseEmailList(String feedEntrustOrderId);

    Map<String,Integer> getStatusCount(OpJczxFeedReportDto opJczxFeedReportBase);

    OpFeedReportVo getReport(String opJczxFeedReportBaseId);

    void addAll(OpJczxFeedReportDto opJczxFeedReportBase);

    /**
     * 获取检测中心饲料报告列表
     *
     * @param opJczxFeedReportDto 查询参数
     * @return 结果
     */
    List<OpFeedReportListVo> queryFeedReport(OpJczxFeedReportDto opJczxFeedReportDto);

    void invalidateReport(OpJczxFeedReportDto opJczxFeedReportBase);
}
