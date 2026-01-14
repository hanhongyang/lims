package com.gmlimsqi.business.labtest.service;


import com.gmlimsqi.business.labtest.domain.OpJczxBloodReportBase;
import com.gmlimsqi.business.labtest.dto.BloodEmailDTO;
import com.gmlimsqi.business.labtest.vo.ReportEmailVo;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * 检测中心血样报告主Service接口
 * 
 * @author hhy
 * @date 2025-10-22
 */
public interface IOpJczxBloodReportBaseService 
{
    /**
     * 查询检测中心血样报告主
     * 
     * @param opJczxBloodReportBaseId 检测中心血样报告主主键
     * @return 检测中心血样报告主
     */
    public OpJczxBloodReportBase selectOpJczxBloodReportBaseByOpJczxBloodReportBaseId(OpJczxBloodReportBase opJczxBloodReportBaseId);

    /**
     * 查询检测中心血样报告主列表
     * 
     * @param opJczxBloodReportBase 检测中心血样报告主
     * @return 检测中心血样报告主集合
     */
    public List<OpJczxBloodReportBase> selectOpJczxBloodReportBaseList(OpJczxBloodReportBase opJczxBloodReportBase);

    /**
     * 新增检测中心血样报告主
     * 
     * @param opJczxBloodReportBase 检测中心血样报告主
     * @return 结果
     */
    public String insertOpJczxBloodReportBase(OpJczxBloodReportBase opJczxBloodReportBase);

    /**
     * 修改检测中心血样报告主
     * 
     * @param opJczxBloodReportBase 检测中心血样报告主
     * @return 结果
     */
    public int updateOpJczxBloodReportBase(OpJczxBloodReportBase opJczxBloodReportBase);

    /**
     * 批量删除检测中心血样报告主
     * 
     * @param opJczxBloodReportBaseIds 需要删除的检测中心血样报告主主键集合
     * @return 结果
     */
    public int deleteOpJczxBloodReportBaseByOpJczxBloodReportBaseIds(String[] opJczxBloodReportBaseIds);

    /**
     * 删除检测中心血样报告主信息
     * 
     * @param opJczxBloodReportBaseId 检测中心血样报告主主键
     * @return 结果
     */
    public int deleteOpJczxBloodReportBaseByOpJczxBloodReportBaseId(String opJczxBloodReportBaseId);

    int verifyOpJczxBloodReportBase(OpJczxBloodReportBase opJczxBloodReportBase);

    int commitOpJczxBloodReportBase(OpJczxBloodReportBase opJczxBloodReportBase);

    void downloadZaoyunReportExcel(HttpServletResponse response, OpJczxBloodReportBase opJczxBloodReportBase) throws Exception;

    void downloadBiochemistryReportExcel(HttpServletResponse response, OpJczxBloodReportBase opJczxBloodReportBase)throws Exception;

     /**
     * 查询发送报告邮箱
     *
     * @return 发送报告邮箱集合
     */
    List<ReportEmailVo> selectOpJczxBloodReportBaseEmailList(String bloodEntrustOrderId);

     /**
     * 发送检测中心血样报告
     *
     * @param emailDTOList 发送报告邮箱集合
     * @return 结果
     */
    int sendOpJczxBloodReport(List<BloodEmailDTO> emailDTOList) throws IOException;

    Map<String, Integer> getStatusCount(OpJczxBloodReportBase opJczxBloodReportBase);

    OpJczxBloodReportBase getReport(String opJczxBloodReportBaseId);

    void addAll(OpJczxBloodReportBase opJczxBloodReportBase);

    void zyBack(OpJczxBloodReportBase opJczxBloodReportBase);

    /**
     * 获取疾病报告
     * @param opJczxBloodReportBase 获取条件
     * @return 疾病报告集合
     */
    List<OpJczxBloodReportBase> queryDiseaseReport(OpJczxBloodReportBase opJczxBloodReportBase);

    /**
     * 获取早产报告
     * @param opJczxBloodReportBase 获取条件
     * @return 早产报告集合
     */
    List<OpJczxBloodReportBase> queryEarlyPregnancyReport(OpJczxBloodReportBase opJczxBloodReportBase);

    /**
     * 查询生化报告
     * @param opJczxBloodReportBase 查询条件
     * @return 生化报告集合
     */
    public List<OpJczxBloodReportBase> queryBiochemistryReport(OpJczxBloodReportBase opJczxBloodReportBase);

}
