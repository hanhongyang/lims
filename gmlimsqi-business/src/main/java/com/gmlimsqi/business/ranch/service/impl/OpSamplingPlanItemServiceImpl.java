package com.gmlimsqi.business.ranch.service.impl;


import com.gmlimsqi.business.ranch.domain.OpSamplingPlanItem;
import com.gmlimsqi.business.ranch.mapper.OpSamplingPlanItemMapper;
import com.gmlimsqi.business.ranch.mapper.OpSamplingPlanSampleMapper;
import com.gmlimsqi.business.ranch.service.IOpSamplingPlanItemService;
import com.gmlimsqi.business.util.UserCacheService;
import com.gmlimsqi.common.utils.StringUtils;
import com.gmlimsqi.common.utils.uuid.IdUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 取样计划-项目对应Service业务层处理
 * 
 * @author hhy
 * @date 2025-11-04
 */
@Service
public class OpSamplingPlanItemServiceImpl implements IOpSamplingPlanItemService
{
    @Autowired
    private OpSamplingPlanItemMapper opSamplingPlanItemMapper;
    @Autowired
    private UserCacheService userCacheService;
    @Autowired
    private OpSamplingPlanSampleMapper opSamplingPlanSampleMapper;

    /**
     * 查询取样计划-项目对应
     * 
     * @param opSamplingPlanItemId 取样计划-项目对应主键
     * @return 取样计划-项目对应
     */
    @Override
    public OpSamplingPlanItem selectOpSamplingPlanItemByOpSamplingPlanItemId(String opSamplingPlanItemId)
    {
        return opSamplingPlanItemMapper.selectOpSamplingPlanItemByOpSamplingPlanItemId(opSamplingPlanItemId);
    }

    /**
     * 查询取样计划-项目对应列表
     * 
     * @param opSamplingPlanItem 取样计划-项目对应
     * @return 取样计划-项目对应
     */
    @Override
    public List<OpSamplingPlanItem> selectOpSamplingPlanItemList(OpSamplingPlanItem opSamplingPlanItem)
    {
        List<OpSamplingPlanItem> items = opSamplingPlanItemMapper.selectOpSamplingPlanItemList(opSamplingPlanItem);

        // 批量处理用户名
        if (!items.isEmpty()) {
            Set<String> userIds = items.stream()
                    .map(OpSamplingPlanItem::getCreateBy)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toSet());

            Map<String, String> usernameMap = userCacheService.batchGetUsernames(userIds);

            items.forEach(vo ->
                    vo.setCreateBy(usernameMap.get(vo.getCreateBy())));
        }

        return items;
    }

    /**
     * 新增取样计划-项目对应
     * 
     * @param opSamplingPlanItem 取样计划-项目对应
     * @return 结果
     */
    @Override
    public int insertOpSamplingPlanItem(OpSamplingPlanItem opSamplingPlanItem)
    {
        if (StringUtils.isEmpty(opSamplingPlanItem.getOpSamplingPlanItemId())) {
            opSamplingPlanItem.setOpSamplingPlanItemId(IdUtils.simpleUUID());
        }
        // 自动填充创建/更新信息
        opSamplingPlanItem.fillCreateInfo();
        return opSamplingPlanItemMapper.insertOpSamplingPlanItem(opSamplingPlanItem);
    }

    /**
     * 修改取样计划-项目对应
     * 
     * @param opSamplingPlanItem 取样计划-项目对应
     * @return 结果
     */
    @Override
    public int updateOpSamplingPlanItem(OpSamplingPlanItem opSamplingPlanItem)
    {
        // 自动填充更新信息
        opSamplingPlanItem.fillUpdateInfo();
        return opSamplingPlanItemMapper.updateOpSamplingPlanItem(opSamplingPlanItem);
    }

    /**
     * 批量删除取样计划-项目对应
     * 
     * @param opSamplingPlanItemIds 需要删除的取样计划-项目对应主键
     * @return 结果
     */
    @Override
    public int deleteOpSamplingPlanItemByOpSamplingPlanItemIds(String[] opSamplingPlanItemIds)
    {
        return opSamplingPlanItemMapper.deleteOpSamplingPlanItemByOpSamplingPlanItemIds(opSamplingPlanItemIds);
    }

    /**
     * 删除取样计划-项目对应信息
     * 
     * @param opSamplingPlanItemId 取样计划-项目对应主键
     * @return 结果
     */
    @Override
    public int deleteOpSamplingPlanItemByOpSamplingPlanItemId(String opSamplingPlanItemId)
    {
        return opSamplingPlanItemMapper.deleteOpSamplingPlanItemByOpSamplingPlanItemId(opSamplingPlanItemId);
    }
}
