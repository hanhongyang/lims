package com.gmlimsqi.business.labtest.service;


import com.gmlimsqi.business.basicdata.domain.OpJczxPcrReportInfo;

import java.util.List;

/**
 * pcr报告子Service接口
 * 
 * @author hhy
 * @date 2025-10-20
 */
public interface IOpJczxPcrReportInfoService 
{
    /**
     * 查询pcr报告子
     * 
     * @param opJczxPcrReportInfoId pcr报告子主键
     * @return pcr报告子
     */
    public OpJczxPcrReportInfo selectOpJczxPcrReportInfoByOpJczxPcrReportInfoId(String opJczxPcrReportInfoId);

    /**
     * 查询pcr报告子列表
     * 
     * @param opJczxPcrReportInfo pcr报告子
     * @return pcr报告子集合
     */
    public List<OpJczxPcrReportInfo> selectOpJczxPcrReportInfoList(OpJczxPcrReportInfo opJczxPcrReportInfo);

    /**
     * 新增pcr报告子
     * 
     * @param opJczxPcrReportInfo pcr报告子
     * @return 结果
     */
    public int insertOpJczxPcrReportInfo(OpJczxPcrReportInfo opJczxPcrReportInfo);

    /**
     * 修改pcr报告子
     * 
     * @param opJczxPcrReportInfo pcr报告子
     * @return 结果
     */
    public int updateOpJczxPcrReportInfo(OpJczxPcrReportInfo opJczxPcrReportInfo);

    /**
     * 批量删除pcr报告子
     * 
     * @param opJczxPcrReportInfoIds 需要删除的pcr报告子主键集合
     * @return 结果
     */
    public int deleteOpJczxPcrReportInfoByOpJczxPcrReportInfoIds(String[] opJczxPcrReportInfoIds);

    /**
     * 删除pcr报告子信息
     * 
     * @param opJczxPcrReportInfoId pcr报告子主键
     * @return 结果
     */
    public int deleteOpJczxPcrReportInfoByOpJczxPcrReportInfoId(String opJczxPcrReportInfoId);
}
