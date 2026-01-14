package com.gmlimsqi.business.ranch.mapper;


import com.gmlimsqi.business.ranch.domain.OpSamplingPlanItem;
import com.gmlimsqi.business.ranch.domain.OpSamplingPlanSample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 取样计划-项目对应Mapper接口
 * 
 * @author hhy
 * @date 2025-11-04
 */
public interface OpSamplingPlanItemMapper 
{
    /**
     * 查询取样计划-项目对应
     * 
     * @param opSamplingPlanItemId 取样计划-项目对应主键
     * @return 取样计划-项目对应
     */
    public OpSamplingPlanItem selectOpSamplingPlanItemByOpSamplingPlanItemId(String opSamplingPlanItemId);

    /**
     * 查询取样计划-项目对应列表
     * 
     * @param opSamplingPlanItem 取样计划-项目对应
     * @return 取样计划-项目对应集合
     */
    public List<OpSamplingPlanItem> selectOpSamplingPlanItemList(OpSamplingPlanItem opSamplingPlanItem);

    /**
     * 新增取样计划-项目对应
     * 
     * @param opSamplingPlanItem 取样计划-项目对应
     * @return 结果
     */
    public int insertOpSamplingPlanItem(OpSamplingPlanItem opSamplingPlanItem);

    /**
     * 修改取样计划-项目对应
     * 
     * @param opSamplingPlanItem 取样计划-项目对应
     * @return 结果
     */
    public int updateOpSamplingPlanItem(OpSamplingPlanItem opSamplingPlanItem);

    /**
     * 通过取样计划-项目对应主键更新删除标志
     *
     * @param opSamplingPlanItemId 取样计划-项目对应ID
     * @return 结果
     */
    public int updateDeleteFlagById(String opSamplingPlanItemId);

    /**
     * 批量通过取样计划-项目对应主键更新删除标志
     *
     * @param opSamplingPlanItemId 取样计划-项目对应ID
     * @return 结果
     */
    public int updateDeleteFlagByIds(String[] opSamplingPlanItemIds);

    /**
     * 删除取样计划-项目对应
     * 
     * @param opSamplingPlanItemId 取样计划-项目对应主键
     * @return 结果
     */
    public int deleteOpSamplingPlanItemByOpSamplingPlanItemId(String opSamplingPlanItemId);

    /**
     * 批量删除取样计划-项目对应
     * 
     * @param opSamplingPlanItemIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteOpSamplingPlanItemByOpSamplingPlanItemIds(String[] opSamplingPlanItemIds);
    /**
     * 批量新增取样计划-项目对应
     */
    int batchInsertOpSamplingPlanItem(@Param("list") List<OpSamplingPlanItem> list);

    public List<OpSamplingPlanItem> selectOpSamplingPlanItemBySampleId(String opSamplingPlanSampleId);
    /**
     * (新) 根据主键ID列表查询
     */
    public List<OpSamplingPlanItem> selectOpSamplingPlanItemListByIds(List<String> ids);

    void updateTestToNullById(@Param("planItemId") String planItemId, @Param("updateUser") String updateUser);

    /**
     * 根据样品表id更新删除标志
     */
     void updateItemDeleteFlagByPlanId(@Param("samplingPlanSampleId") String samplingPlanSampleId, @Param("updateUserId") String updateUserId);

    /**
     * (新) 管理员全流程监控：查询检测项目详情（穿透查询）
     */
    public List<OpSamplingPlanItem> selectItemMonitorDetail(String sampleId);
}
