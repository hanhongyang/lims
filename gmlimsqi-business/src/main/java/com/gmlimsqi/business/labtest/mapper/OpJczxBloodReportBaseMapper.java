package com.gmlimsqi.business.labtest.mapper;


import com.gmlimsqi.business.labtest.domain.OpJczxBloodReportBase;
import com.gmlimsqi.business.labtest.dto.OpJczxFeedReportDto;

import java.util.List;
import java.util.Map;

/**
 * 检测中心血样报告主Mapper接口
 * 
 * @author hhy
 * @date 2025-10-22
 */
public interface OpJczxBloodReportBaseMapper 
{
    /**
     * 查询检测中心血样报告主
     * 
     * @param opJczxBloodReportBaseId 检测中心血样报告主主键
     * @return 检测中心血样报告主
     */
    public OpJczxBloodReportBase selectOpJczxBloodReportBaseByOpJczxBloodReportBaseId(String opJczxBloodReportBaseId);

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
    public int insertOpJczxBloodReportBase(OpJczxBloodReportBase opJczxBloodReportBase);

    /**
     * 修改检测中心血样报告主
     * 
     * @param opJczxBloodReportBase 检测中心血样报告主
     * @return 结果
     */
    public int updateOpJczxBloodReportBase(OpJczxBloodReportBase opJczxBloodReportBase);

    /**
     * 通过检测中心血样报告主主键更新删除标志
     *
     * @param opJczxBloodReportBaseId 检测中心血样报告主ID
     * @return 结果
     */
    public int updateDeleteFlagById(String opJczxBloodReportBaseId);

    /**
     * 批量通过检测中心血样报告主主键更新删除标志
     *
     * @param opJczxBloodReportBaseIds 检测中心血样报告主ID
     * @return 结果
     */
    public int updateDeleteFlagByIds(String[] opJczxBloodReportBaseIds);

    /**
     * 删除检测中心血样报告主
     * 
     * @param opJczxBloodReportBaseId 检测中心血样报告主主键
     * @return 结果
     */
    public int deleteOpJczxBloodReportBaseByOpJczxBloodReportBaseId(String opJczxBloodReportBaseId);

    /**
     * 批量删除检测中心血样报告主
     * 
     * @param opJczxBloodReportBaseIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteOpJczxBloodReportBaseByOpJczxBloodReportBaseIds(String[] opJczxBloodReportBaseIds);

    List<OpJczxBloodReportBase> selectOpJczxBloodReportBaseListStatus0(OpJczxBloodReportBase opJczxBloodReportBase);

     /**
     * 查询检测中心血样报告主列表
     *
     * @param bloodEntrustOrderId 血样委托单号
     * @return 检测中心血样报告主集合
     */
    public List<OpJczxBloodReportBase> selectReportBaseByOrderId(String bloodEntrustOrderId);


    Integer  getStatus0Count(OpJczxBloodReportBase opJczxBloodReportBase);

    List<Map<String, Object>> getStatusCount(OpJczxBloodReportBase opJczxBloodReportBase);

    void updateStatus(OpJczxBloodReportBase opJczxBloodReportBase);
}
