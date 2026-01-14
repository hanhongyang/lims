package com.gmlimsqi.business.labtest.service;


import com.gmlimsqi.business.labtest.domain.OpJczxBloodResultInfo;

import java.util.List;

/**
 * 检测中心血样化验单子Service接口
 * 
 * @author hhy
 * @date 2025-10-14
 */
public interface IOpJczxBloodResultInfoService 
{
    /**
     * 查询检测中心血样化验单子
     * 
     * @param opJczxPcrResultInfoId 检测中心血样化验单子主键
     * @return 检测中心血样化验单子
     */
    public OpJczxBloodResultInfo selectOpJczxBloodResultInfoByOpJczxPcrResultInfoId(String opJczxPcrResultInfoId);

    /**
     * 查询检测中心血样化验单子列表
     * 
     * @param opJczxBloodResultInfo 检测中心血样化验单子
     * @return 检测中心血样化验单子集合
     */
    public List<OpJczxBloodResultInfo> selectOpJczxBloodResultInfoList(OpJczxBloodResultInfo opJczxBloodResultInfo);

    /**
     * 新增检测中心血样化验单子
     * 
     * @param opJczxBloodResultInfo 检测中心血样化验单子
     * @return 结果
     */
    public int insertOpJczxBloodResultInfo(OpJczxBloodResultInfo opJczxBloodResultInfo);

    /**
     * 修改检测中心血样化验单子
     * 
     * @param opJczxBloodResultInfo 检测中心血样化验单子
     * @return 结果
     */
    public int updateOpJczxBloodResultInfo(OpJczxBloodResultInfo opJczxBloodResultInfo);

    /**
     * 批量删除检测中心血样化验单子
     * 
     * @param opJczxPcrResultInfoIds 需要删除的检测中心血样化验单子主键集合
     * @return 结果
     */
    public int deleteOpJczxBloodResultInfoByOpJczxPcrResultInfoIds(String[] opJczxPcrResultInfoIds);

    /**
     * 删除检测中心血样化验单子信息
     * 
     * @param opJczxPcrResultInfoId 检测中心血样化验单子主键
     * @return 结果
     */
    public int deleteOpJczxBloodResultInfoByOpJczxPcrResultInfoId(String opJczxPcrResultInfoId);
}
