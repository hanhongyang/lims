package com.gmlimsqi.business.ranch.mapper;

import java.util.List;
import java.util.Map;

import com.gmlimsqi.business.ranch.domain.OpTestResultBase;
import com.gmlimsqi.business.ranch.vo.SampleTaskVo;
import com.gmlimsqi.business.ranch.vo.SamplingPlanItemVO;
import org.apache.ibatis.annotations.Param;

/**
 * 样品化验Mapper接口
 * 
 * @author hhy
 * @date 2025-11-07
 */
public interface OpTestResultBaseMapper 
{
    /**
     * 查询样品化验
     * 
     * @param id 样品化验主键
     * @return 样品化验
     */
    public OpTestResultBase selectOpTestResultBaseById(String id);

    /**
     * 查询样品化验列表
     * 
     * @param opTestResultBase 样品化验
     * @return 样品化验集合
     */
    public List<OpTestResultBase> selectOpTestResultBaseList(OpTestResultBase opTestResultBase);

    /**
     * 新增样品化验
     * 
     * @param opTestResultBase 样品化验
     * @return 结果
     */
    public int insertOpTestResultBase(OpTestResultBase opTestResultBase);

    /**
     * 修改样品化验
     * 
     * @param opTestResultBase 样品化验
     * @return 结果
     */
    public int updateOpTestResultBase(OpTestResultBase opTestResultBase);

    /**
     * 通过样品化验主键更新删除标志
     *
     * @param id 样品化验ID
     * @return 结果
     */
    public int updateDeleteFlagById(String id);

    /**
     * 批量通过样品化验主键更新删除标志
     *
     * @param id 样品化验ID
     * @return 结果
     */
    public int updateDeleteFlagByIds(String[] ids);


    /**
     * (新) 查询 "待化验" (Tab 1) 列表
     */
    public List<SampleTaskVo> selectPendingTaskList(SampleTaskVo queryParams);

    /**
     * (新) 查询 "待化验" (Tab 1) 的数量
     */
    public Long countPendingTaskList(SampleTaskVo queryParams);

    /**
     * (新) 按状态分组查询 (Tab 2, 3, 4) 的数量
     */
    public List<Map<String, Object>> countByStatus(OpTestResultBase queryParams);

    /**
     * 根据样品ID查询采样计划报表数据
     */
    List<SamplingPlanItemVO> selectReportBySampleId(@Param("sampleId") String sampleId);

}
