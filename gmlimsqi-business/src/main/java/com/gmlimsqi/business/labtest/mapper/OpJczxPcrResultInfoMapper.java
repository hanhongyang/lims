package com.gmlimsqi.business.labtest.mapper;

import java.util.List;

import com.gmlimsqi.business.labtest.domain.OpJczxPcrResultBase;
import com.gmlimsqi.business.labtest.domain.OpJczxPcrResultInfo;
import org.apache.ibatis.annotations.Param;

/**
 * 检测中心pce化验单子Mapper接口
 * 
 * @author hhy
 * @date 2025-10-13
 */
public interface OpJczxPcrResultInfoMapper 
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

    /**
     * 通过检测中心pce化验单子主键更新删除标志
     *
     * @param opJczxPcrResultInfoId 检测中心pce化验单子ID
     * @return 结果
     */
    public int updateDeleteFlagById(String opJczxPcrResultInfoId);

    /**
     * 批量通过检测中心pce化验单子主键更新删除标志
     *
     * @param opJczxPcrResultInfoId 检测中心pce化验单子ID
     * @return 结果
     */
    public int updateDeleteFlagByIds(String[] opJczxPcrResultInfoIds);

    /**
     * 删除检测中心pce化验单子
     * 
     * @param opJczxPcrResultInfoId 检测中心pce化验单子主键
     * @return 结果
     */
    public int deleteOpJczxPcrResultInfoByOpJczxPcrResultInfoId(String opJczxPcrResultInfoId);

    /**
     * 批量删除检测中心pce化验单子
     * 
     * @param opJczxPcrResultInfoIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteOpJczxPcrResultInfoByOpJczxPcrResultInfoIds(String[] opJczxPcrResultInfoIds);

    //弃用
    public void updateResultBySampleNoItemName2(@Param("sampleNo") String sampleNo, @Param("testResult") String testResult, @Param("itemName") String itemName, @Param("testUserId") String testUserId);

    public void updateResultBySampleNoItemName(OpJczxPcrResultInfo info );

    public List<OpJczxPcrResultInfo> getBaseByEntrustOrderNo(@Param("pcrTaskItemType") String pcrTaskItemType, @Param("entrustOrderNo")  String entrustOrderNo);


    int updateRemarkBySampleNoItemId(@Param("remark")String remark,@Param("sampleNo")String sampleNo, @Param("pcrEntrustOrderItemId")String pcrEntrustOrderItemId);

    int batchClearExamineFields(@Param("opJczxPcrResultBaseId")String opJczxPcrResultBaseId);
}
