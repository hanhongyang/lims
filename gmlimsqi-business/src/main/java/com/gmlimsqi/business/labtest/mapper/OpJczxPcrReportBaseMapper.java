package com.gmlimsqi.business.labtest.mapper;

import com.gmlimsqi.business.labtest.domain.OpJczxPcrReportBase;
import com.gmlimsqi.business.labtest.vo.OpPcrReportListVo;

import java.util.List;


/**
 * pcr报告主Mapper接口
 * 
 * @author hhy
 * @date 2025-10-20
 */
public interface OpJczxPcrReportBaseMapper 
{
    /**
     * 查询pcr报告主
     * 
     * @param opJczxPcrReportBaseId pcr报告主主键
     * @return pcr报告主
     */
    public OpJczxPcrReportBase selectOpJczxPcrReportBaseByOpJczxPcrReportBaseId(String opJczxPcrReportBaseId);

    /**
     * 查询pcr报告主列表
     *
     * @param opPcrReportListVo pcr报告主
     * @return pcr报告主集合
     */
    public List<OpPcrReportListVo> selectOpJczxPcrReportBaseList(OpPcrReportListVo opPcrReportListVo);

    /**
     * 新增pcr报告主
     * 
     * @param opJczxPcrReportBase pcr报告主
     * @return 结果
     */
    public int insertOpJczxPcrReportBase(OpJczxPcrReportBase opJczxPcrReportBase);

    /**
     * 修改pcr报告主
     * 
     * @param opJczxPcrReportBase pcr报告主
     * @return 结果
     */
    public int updateOpJczxPcrReportBase(OpJczxPcrReportBase opJczxPcrReportBase);

    /**
     * 通过pcr报告主主键更新删除标志
     *
     * @param opJczxPcrReportBaseId pcr报告主ID
     * @return 结果
     */
    public int updateDeleteFlagById(String opJczxPcrReportBaseId);

    /**
     * 批量通过pcr报告主主键更新删除标志
     *
     * @param opJczxPcrReportBaseId pcr报告主ID
     * @return 结果
     */
    public int updateDeleteFlagByIds(String[] opJczxPcrReportBaseIds);

    /**
     * 删除pcr报告主
     * 
     * @param opJczxPcrReportBaseId pcr报告主主键
     * @return 结果
     */
    public int deleteOpJczxPcrReportBaseByOpJczxPcrReportBaseId(String opJczxPcrReportBaseId);

    /**
     * 批量删除pcr报告主
     * 
     * @param opJczxPcrReportBaseIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteOpJczxPcrReportBaseByOpJczxPcrReportBaseIds(String[] opJczxPcrReportBaseIds);

    List<OpPcrReportListVo> selectOpJczxPcrReportBaseListStatus0(OpPcrReportListVo opPcrReportListVo);
}
