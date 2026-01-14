package com.gmlimsqi.business.labtest.service.impl;

import com.gmlimsqi.business.labtest.domain.OpBloodEntrustOrderItem;
import com.gmlimsqi.business.labtest.mapper.OpBloodEntrustOrderItemMapper;
import com.gmlimsqi.business.labtest.service.IOpBloodEntrustOrderItemService;
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
 * 血样样品委托-项目对应Service业务层处理
 *
 * @author hhy
 * @date 2025-09-20
 */
@Service
public class OpBloodEntrustOrderItemServiceImpl implements IOpBloodEntrustOrderItemService
{
    @Autowired
    private OpBloodEntrustOrderItemMapper opBloodEntrustOrderItemMapper;
    @Autowired
    private UserCacheService userCacheService;

    /**
     * 查询血样样品委托-项目对应
     *
     * @param opFeedEntrustOrderItemId 血样样品委托-项目对应主键
     * @return 血样样品委托-项目对应
     */
    @Override
    public OpBloodEntrustOrderItem selectOpBloodEntrustOrderItemByOpFeedEntrustOrderItemId(String opFeedEntrustOrderItemId)
    {
        return opBloodEntrustOrderItemMapper.selectOpBloodEntrustOrderItemByOpFeedEntrustOrderItemId(opFeedEntrustOrderItemId);
    }

    /**
     * 查询血样样品委托-项目对应列表
     *
     * @param opBloodEntrustOrderItem 血样样品委托-项目对应
     * @return 血样样品委托-项目对应
     */
    @Override
    public List<OpBloodEntrustOrderItem> selectOpBloodEntrustOrderItemList(OpBloodEntrustOrderItem opBloodEntrustOrderItem)
    {
        List<OpBloodEntrustOrderItem> items = opBloodEntrustOrderItemMapper.selectOpBloodEntrustOrderItemList(opBloodEntrustOrderItem);

        // 批量处理用户名
        if (!items.isEmpty()) {
            Set<String> userIds = items.stream()
                    .map(OpBloodEntrustOrderItem::getCreateBy)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toSet());

            Map<String, String> usernameMap = userCacheService.batchGetUsernames(userIds);

            items.forEach(vo ->
                    vo.setCreateBy(usernameMap.get(vo.getCreateBy())));
        }

        return items;
    }

    /**
     * 新增血样样品委托-项目对应
     *
     * @param opBloodEntrustOrderItem 血样样品委托-项目对应
     * @return 结果
     */
    @Override
    public int insertOpBloodEntrustOrderItem(OpBloodEntrustOrderItem opBloodEntrustOrderItem)
    {
        if (StringUtils.isEmpty(opBloodEntrustOrderItem.getOpBloodEntrustOrderItemId())) {
            opBloodEntrustOrderItem.setOpBloodEntrustOrderItemId(IdUtils.simpleUUID());
        }
        // 自动填充创建/更新信息
        opBloodEntrustOrderItem.fillCreateInfo();
        return opBloodEntrustOrderItemMapper.insertOpBloodEntrustOrderItem(opBloodEntrustOrderItem);
    }

    /**
     * 修改血样样品委托-项目对应
     *
     * @param opBloodEntrustOrderItem 血样样品委托-项目对应
     * @return 结果
     */
    @Override
    public int updateOpBloodEntrustOrderItem(OpBloodEntrustOrderItem opBloodEntrustOrderItem)
    {
        // 自动填充更新信息
        opBloodEntrustOrderItem.fillUpdateInfo();
        return opBloodEntrustOrderItemMapper.updateOpBloodEntrustOrderItem(opBloodEntrustOrderItem);
    }

    /**
     * 批量删除血样样品委托-项目对应
     *
     * @param opFeedEntrustOrderItemIds 需要删除的血样样品委托-项目对应主键
     * @return 结果
     */
    @Override
    public int deleteOpBloodEntrustOrderItemByOpFeedEntrustOrderItemIds(String[] opFeedEntrustOrderItemIds)
    {
        return opBloodEntrustOrderItemMapper.deleteOpBloodEntrustOrderItemByOpFeedEntrustOrderItemIds(opFeedEntrustOrderItemIds);
    }

    /**
     * 删除血样样品委托-项目对应信息
     *
     * @param opFeedEntrustOrderItemId 血样样品委托-项目对应主键
     * @return 结果
     */
    @Override
    public int deleteOpBloodEntrustOrderItemByOpFeedEntrustOrderItemId(String opFeedEntrustOrderItemId)
    {
        return opBloodEntrustOrderItemMapper.deleteOpBloodEntrustOrderItemByOpFeedEntrustOrderItemId(opFeedEntrustOrderItemId);
    }
}
