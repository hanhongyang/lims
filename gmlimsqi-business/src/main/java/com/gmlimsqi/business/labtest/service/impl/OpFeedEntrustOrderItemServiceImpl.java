package com.gmlimsqi.business.labtest.service.impl;

import java.util.List;
import com.gmlimsqi.common.utils.StringUtils;
import com.gmlimsqi.common.utils.uuid.IdUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.gmlimsqi.business.labtest.mapper.OpFeedEntrustOrderItemMapper;
import com.gmlimsqi.business.labtest.domain.OpFeedEntrustOrderItem;
import com.gmlimsqi.business.labtest.service.IOpFeedEntrustOrderItemService;

/**
 * 饲料样品委托-项目对应Service业务层处理
 * 
 * @author hhy
 * @date 2025-09-17
 */
@Service
public class OpFeedEntrustOrderItemServiceImpl implements IOpFeedEntrustOrderItemService 
{
    @Autowired
    private OpFeedEntrustOrderItemMapper opFeedEntrustOrderItemMapper;


    /**
     * 查询饲料样品委托-项目对应
     * 
     * @param opFeedEntrustOrderItemId 饲料样品委托-项目对应主键
     * @return 饲料样品委托-项目对应
     */
    @Override
    public OpFeedEntrustOrderItem selectOpFeedEntrustOrderItemByOpFeedEntrustOrderItemId(String opFeedEntrustOrderItemId)
    {
        return opFeedEntrustOrderItemMapper.selectOpFeedEntrustOrderItemByOpFeedEntrustOrderItemId(opFeedEntrustOrderItemId);
    }

    /**
     * 查询饲料样品委托-项目对应列表
     * 
     * @param opFeedEntrustOrderItem 饲料样品委托-项目对应
     * @return 饲料样品委托-项目对应
     */
    @Override
    public List<OpFeedEntrustOrderItem> selectOpFeedEntrustOrderItemList(OpFeedEntrustOrderItem opFeedEntrustOrderItem)
    {
        List<OpFeedEntrustOrderItem> items = opFeedEntrustOrderItemMapper.selectOpFeedEntrustOrderItemList(opFeedEntrustOrderItem);

        return items;
    }

    /**
     * 新增饲料样品委托-项目对应
     * 
     * @param opFeedEntrustOrderItem 饲料样品委托-项目对应
     * @return 结果
     */
    @Override
    public int insertOpFeedEntrustOrderItem(OpFeedEntrustOrderItem opFeedEntrustOrderItem)
    {
        if (StringUtils.isEmpty(opFeedEntrustOrderItem.getOpFeedEntrustOrderItemId())) {
            opFeedEntrustOrderItem.setOpFeedEntrustOrderItemId(IdUtils.simpleUUID());
        }
        // 自动填充创建/更新信息
        opFeedEntrustOrderItem.fillCreateInfo();
        return opFeedEntrustOrderItemMapper.insertOpFeedEntrustOrderItem(opFeedEntrustOrderItem);
    }

    /**
     * 修改饲料样品委托-项目对应
     * 
     * @param opFeedEntrustOrderItem 饲料样品委托-项目对应
     * @return 结果
     */
    @Override
    public int updateOpFeedEntrustOrderItem(OpFeedEntrustOrderItem opFeedEntrustOrderItem)
    {
        // 自动填充更新信息
        opFeedEntrustOrderItem.fillUpdateInfo();
        return opFeedEntrustOrderItemMapper.updateOpFeedEntrustOrderItem(opFeedEntrustOrderItem);
    }


}
