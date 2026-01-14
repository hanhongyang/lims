package com.gmlimsqi.business.labtest.mapper;


import com.gmlimsqi.business.basicdata.domain.OpJczxPcrReportInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * pcr报告子Mapper接口
 * 
 * @author hhy
 * @date 2025-10-20
 */
public interface OpJczxPcrReportInfoMapper 
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
     * 通过pcr报告子主键更新删除标志
     *
     * @param opJczxPcrReportInfoId pcr报告子ID
     * @return 结果
     */
    public int updateDeleteFlagById(String opJczxPcrReportInfoId);
    public int updateDeleteFlagByBaseId(@Param("baseId") String baseId,@Param("updateUserId") String updateUserId);

    /**
     * 批量通过pcr报告子主键更新删除标志
     *
     * @param opJczxPcrReportInfoId pcr报告子ID
     * @return 结果
     */
    public int updateDeleteFlagByIds(String[] opJczxPcrReportInfoIds);

    /**
     * 删除pcr报告子
     * 
     * @param opJczxPcrReportInfoId pcr报告子主键
     * @return 结果
     */
    public int deleteOpJczxPcrReportInfoByOpJczxPcrReportInfoId(String opJczxPcrReportInfoId);

    /**
     * 批量删除pcr报告子
     * 
     * @param opJczxPcrReportInfoIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteOpJczxPcrReportInfoByOpJczxPcrReportInfoIds(String[] opJczxPcrReportInfoIds);

    List<String> selectItemTypeByBaseId(String opJczxPcrReportBaseId);
}
