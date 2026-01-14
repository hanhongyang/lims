package com.gmlimsqi.business.labtest.mapper;

import com.gmlimsqi.business.labtest.domain.OpJczxBloodReportInfo;
import com.gmlimsqi.business.labtest.domain.OpJczxBloodResultInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;


/**
 * 检测中心血样报告子Mapper接口
 * 
 * @author hhy
 * @date 2025-10-22
 */
public interface OpJczxBloodReportInfoMapper 
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
     * 通过检测中心血样报告子主键更新删除标志
     *
     * @param opJczxBloodReportInfoId 检测中心血样报告子ID
     * @return 结果
     */
    public int updateDeleteFlagById(String opJczxBloodReportInfoId);

    /**
     * 批量通过检测中心血样报告子主键更新删除标志
     *
     * @param opJczxBloodReportInfoIds 检测中心血样报告子ID
     * @return 结果
     */
    public int updateDeleteFlagByIds(String[] opJczxBloodReportInfoIds);

    /**
     * 删除检测中心血样报告子
     * 
     * @param opJczxBloodReportInfoId 检测中心血样报告子主键
     * @return 结果
     */
    public int deleteOpJczxBloodReportInfoByOpJczxBloodReportInfoId(String opJczxBloodReportInfoId);

    /**
     * 批量删除检测中心血样报告子
     * 
     * @param opJczxBloodReportInfoIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteOpJczxBloodReportInfoByOpJczxBloodReportInfoIds(String[] opJczxBloodReportInfoIds);

    int deleteByBaseId(@Param("updateUserId") String  updateUserId ,@Param("baseId") String baseId);

    List<OpJczxBloodReportInfo> selectOpJczxBloodReportInfoListByBaseId(@Param("opJczxBloodReportBaseId") String opJczxBloodReportBaseId);
}
