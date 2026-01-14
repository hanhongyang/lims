package com.gmlimsqi.business.labtest.mapper;


import com.gmlimsqi.business.labtest.domain.OpJczxBloodResultInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;


/**
 * 检测中心血样化验单子Mapper接口
 * 
 * @author hhy
 * @date 2025-10-14
 */
public interface OpJczxBloodResultInfoMapper 
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
     * 通过检测中心血样化验单子主键更新删除标志
     *
     * @param opJczxPcrResultInfoId 检测中心血样化验单子ID
     * @return 结果
     */
    public int updateDeleteFlagById(String opJczxPcrResultInfoId);

    /**
     * 批量通过检测中心血样化验单子主键更新删除标志
     *
     * @param opJczxPcrResultInfoId 检测中心血样化验单子ID
     * @return 结果
     */
    public int updateDeleteFlagByIds(String[] opJczxPcrResultInfoIds);

    /**
     * 删除检测中心血样化验单子
     * 
     * @param opJczxPcrResultInfoId 检测中心血样化验单子主键
     * @return 结果
     */
    public int deleteOpJczxBloodResultInfoByOpJczxPcrResultInfoId(String opJczxPcrResultInfoId);

    /**
     * 批量删除检测中心血样化验单子
     * 
     * @param opJczxPcrResultInfoIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteOpJczxBloodResultInfoByOpJczxPcrResultInfoIds(String[] opJczxPcrResultInfoIds);

    void updateResultBySampleNo(OpJczxBloodResultInfo info);

    void updateExamineNoteBySampleNo(@Param("sampleNo") String sampleNo, @Param("examineNote")String examineNote);
}
