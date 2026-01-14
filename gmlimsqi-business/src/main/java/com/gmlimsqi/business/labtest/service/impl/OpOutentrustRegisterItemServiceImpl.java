package com.gmlimsqi.business.labtest.service.impl;

import com.gmlimsqi.business.labtest.domain.OpOutentrustRegisterItem;
import com.gmlimsqi.business.labtest.mapper.OpOutentrustRegisterItemMapper;
import com.gmlimsqi.business.labtest.service.IOpOutentrustRegisterItemService;
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
 * 外部委托检测单化验项目子Service业务层处理
 *
 * @author wgq
 * @date 2025-09-17
 */
@Service
public class OpOutentrustRegisterItemServiceImpl implements IOpOutentrustRegisterItemService
{
    @Autowired
    private OpOutentrustRegisterItemMapper opOutentrustRegisterItemMapper;
    @Autowired
    private UserCacheService userCacheService;

    /**
     * 查询外部委托检测单化验项目子
     *
     * @param outentrustRegisterItemId 外部委托检测单化验项目子主键
     * @return 外部委托检测单化验项目子
     */
    @Override
    public OpOutentrustRegisterItem selectOpOutentrustRegisterItemByOutentrustRegisterItemId(String outentrustRegisterItemId)
    {
        return opOutentrustRegisterItemMapper.selectOpOutentrustRegisterItemByOutentrustRegisterItemId(outentrustRegisterItemId);
    }

    /**
     * 查询外部委托检测单化验项目子列表
     *
     * @param opOutentrustRegisterItem 外部委托检测单化验项目子
     * @return 外部委托检测单化验项目子
     */
    @Override
    public List<OpOutentrustRegisterItem> selectOpOutentrustRegisterItemList(OpOutentrustRegisterItem opOutentrustRegisterItem)
    {
        List<OpOutentrustRegisterItem> items = opOutentrustRegisterItemMapper.selectOpOutentrustRegisterItemList(opOutentrustRegisterItem);

        // 批量处理用户名
        if (!items.isEmpty()) {
            Set<String> userIds = items.stream()
                    .map(OpOutentrustRegisterItem::getCreateBy)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toSet());

            Map<String, String> usernameMap = userCacheService.batchGetUsernames(userIds);

            items.forEach(vo ->
                    vo.setCreateBy(usernameMap.get(vo.getCreateBy())));
        }

        return items;
    }

    /**
     * 新增外部委托检测单化验项目子
     *
     * @param opOutentrustRegisterItem 外部委托检测单化验项目子
     * @return 结果
     */
    @Override
    public int insertOpOutentrustRegisterItem(OpOutentrustRegisterItem opOutentrustRegisterItem)
    {
        if (StringUtils.isEmpty(opOutentrustRegisterItem.getOutentrustRegisterItemId())) {
            opOutentrustRegisterItem.setOutentrustRegisterItemId(IdUtils.simpleUUID());
        }
        // 自动填充创建/更新信息
        opOutentrustRegisterItem.fillCreateInfo();
        return opOutentrustRegisterItemMapper.insertOpOutentrustRegisterItem(opOutentrustRegisterItem);
    }

    /**
     * 修改外部委托检测单化验项目子
     *
     * @param opOutentrustRegisterItem 外部委托检测单化验项目子
     * @return 结果
     */
    @Override
    public int updateOpOutentrustRegisterItem(OpOutentrustRegisterItem opOutentrustRegisterItem)
    {
        // 自动填充更新信息
        opOutentrustRegisterItem.fillUpdateInfo();
        return opOutentrustRegisterItemMapper.updateOpOutentrustRegisterItem(opOutentrustRegisterItem);
    }

    /**
     * 批量删除外部委托检测单化验项目子
     *
     * @param outentrustRegisteritemId 需要删除的外部委托检测单化验项目子主键
     * @return 结果
     */
    @Override
    public int deleteOpOutentrustRegisterItemByOutentrustRegisteritemId(String[] outentrustRegisteritemId)
    {
        return opOutentrustRegisterItemMapper.deleteOpOutentrustRegisterItemByOutentrustRegisteritemId(outentrustRegisteritemId);
    }

    /**
     * 删除外部委托检测单化验项目子信息
     *
     * @param outentrustRegisterItemId 外部委托检测单化验项目子主键
     * @return 结果
     */
    @Override
    public int deleteOpOutentrustRegisterItemByOutentrustRegisterItemId(String outentrustRegisterItemId)
    {
        return opOutentrustRegisterItemMapper.deleteOpOutentrustRegisterItemByOutentrustRegisterItemId(outentrustRegisterItemId);
    }
}
