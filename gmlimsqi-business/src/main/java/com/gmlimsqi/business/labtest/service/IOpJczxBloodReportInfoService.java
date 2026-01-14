package com.gmlimsqi.business.labtest.service;


import com.gmlimsqi.business.labtest.domain.OpJczxBloodReportInfo;

import java.util.List;

/**
 * 检测中心血样报告子Service接口
 * 
 * @author hhy
 * @date 2025-10-22
 */
public interface IOpJczxBloodReportInfoService 
{
    /**
     * 查询检测中心血样报告子
     * 
     * @param opJczxBloodReportInfoId 检测中心血样报告子主键
     * @return 检测中心血样报告子
     */
    public OpJczxBloodReportInfo selectOpJczxBloodReportInfoByOpJczxBloodReportInfoId(String opJczxBloodReportInfoId);

    /**
     * 查询检测中心血样报告子列表
     * 
     * @param opJczxBloodReportInfo 检测中心血样报告子
     * @return 检测中心血样报告子集合
     */
    public List<OpJczxBloodReportInfo> selectOpJczxBloodReportInfoList(OpJczxBloodReportInfo opJczxBloodReportInfo);

    /**
     * 新增检测中心血样报告子
     * 
     * @param opJczxBloodReportInfo 检测中心血样报告子
     * @return 结果
     */
    public int insertOpJczxBloodReportInfo(OpJczxBloodReportInfo opJczxBloodReportInfo);

    /**
     * 修改检测中心血样报告子
     * 
     * @param opJczxBloodReportInfo 检测中心血样报告子
     * @return 结果
     */
    public int updateOpJczxBloodReportInfo(OpJczxBloodReportInfo opJczxBloodReportInfo);

    /**
     * 批量删除检测中心血样报告子
     * 
     * @param opJczxBloodReportInfoIds 需要删除的检测中心血样报告子主键集合
     * @return 结果
     */
    public int deleteOpJczxBloodReportInfoByOpJczxBloodReportInfoIds(String[] opJczxBloodReportInfoIds);

    /**
     * 删除检测中心血样报告子信息
     * 
     * @param opJczxBloodReportInfoId 检测中心血样报告子主键
     * @return 结果
     */
    public int deleteOpJczxBloodReportInfoByOpJczxBloodReportInfoId(String opJczxBloodReportInfoId);
}
