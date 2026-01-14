package com.gmlimsqi.business.ranch.service;


import com.gmlimsqi.business.ranch.domain.OpSamplingPlanItem;

import java.util.List;

/**
 * 取样计划-项目对应Service接口
 * 
 * @author hhy
 * @date 2025-11-04
 */
public interface IOpSamplingPlanItemService 
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
     * 批量删除取样计划-项目对应
     * 
     * @param opSamplingPlanItemIds 需要删除的取样计划-项目对应主键集合
     * @return 结果
     */
    public int deleteOpSamplingPlanItemByOpSamplingPlanItemIds(String[] opSamplingPlanItemIds);

    /**
     * 删除取样计划-项目对应信息
     * 
     * @param opSamplingPlanItemId 取样计划-项目对应主键
     * @return 结果
     */
    public int deleteOpSamplingPlanItemByOpSamplingPlanItemId(String opSamplingPlanItemId);
}
