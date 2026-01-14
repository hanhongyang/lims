package com.gmlimsqi.business.labtest.service;

import java.util.List;
import com.gmlimsqi.business.labtest.domain.OpJczxPcrResultInfo;

/**
 * 检测中心pce化验单子Service接口
 * 
 * @author hhy
 * @date 2025-10-13
 */
public interface IOpJczxPcrResultInfoService 
{
    /**
     * 查询检测中心pce化验单子
     * 
     * @param opJczxPcrResultInfoId 检测中心pce化验单子主键
     * @return 检测中心pce化验单子
     */
    public OpJczxPcrResultInfo selectOpJczxPcrResultInfoByOpJczxPcrResultInfoId(String opJczxPcrResultInfoId);

    /**
     * 查询检测中心pce化验单子列表
     * 
     * @param opJczxPcrResultInfo 检测中心pce化验单子
     * @return 检测中心pce化验单子集合
     */
    public List<OpJczxPcrResultInfo> selectOpJczxPcrResultInfoList(OpJczxPcrResultInfo opJczxPcrResultInfo);

    /**
     * 新增检测中心pce化验单子
     * 
     * @param opJczxPcrResultInfo 检测中心pce化验单子
     * @return 结果
     */
    public int insertOpJczxPcrResultInfo(OpJczxPcrResultInfo opJczxPcrResultInfo);

    /**
     * 修改检测中心pce化验单子
     * 
     * @param opJczxPcrResultInfo 检测中心pce化验单子
     * @return 结果
     */
    public int updateOpJczxPcrResultInfo(OpJczxPcrResultInfo opJczxPcrResultInfo);

}
