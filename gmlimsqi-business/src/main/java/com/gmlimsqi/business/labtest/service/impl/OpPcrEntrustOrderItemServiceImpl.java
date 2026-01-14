package com.gmlimsqi.business.labtest.service.impl;

import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.List;

import com.gmlimsqi.business.labtest.domain.OpJczxPcrResultInfo;
import com.gmlimsqi.common.utils.StringUtils;
import com.gmlimsqi.common.utils.uuid.IdUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.gmlimsqi.business.labtest.mapper.OpPcrEntrustOrderItemMapper;
import com.gmlimsqi.business.labtest.domain.OpPcrEntrustOrderItem;
import com.gmlimsqi.business.labtest.service.IOpPcrEntrustOrderItemService;

/**
 * pcr样品委托-项目对应Service业务层处理
 * 
 * @author hhy
 * @date 2025-09-17
 */
@Service
public class OpPcrEntrustOrderItemServiceImpl implements IOpPcrEntrustOrderItemService 
{
    @Autowired
    private OpPcrEntrustOrderItemMapper opPcrEntrustOrderItemMapper;


    /**
     * 查询pcr样品委托-项目对应
     * 
     * @param opPcrEntrustOrderItemId pcr样品委托-项目对应主键
     * @return pcr样品委托-项目对应
     */
    @Override
    public OpPcrEntrustOrderItem selectOpPcrEntrustOrderItemByOpPcrEntrustOrderItemId(String opPcrEntrustOrderItemId)
    {
        return opPcrEntrustOrderItemMapper.selectOpPcrEntrustOrderItemByOpPcrEntrustOrderItemId(opPcrEntrustOrderItemId);
    }

    /**
     * 查询pcr样品委托-项目对应列表
     * 
     * @param opPcrEntrustOrderItem pcr样品委托-项目对应
     * @return pcr样品委托-项目对应
     */
    @Override
    public List<OpPcrEntrustOrderItem> selectOpPcrEntrustOrderItemList(OpPcrEntrustOrderItem opPcrEntrustOrderItem)
    {
        List<OpPcrEntrustOrderItem> items = opPcrEntrustOrderItemMapper.selectOpPcrEntrustOrderItemList(opPcrEntrustOrderItem);


        return items;
    }

    /**
     * 新增pcr样品委托-项目对应
     * 
     * @param opPcrEntrustOrderItem pcr样品委托-项目对应
     * @return 结果
     */
    @Override
    public int insertOpPcrEntrustOrderItem(OpPcrEntrustOrderItem opPcrEntrustOrderItem)
    {
        if (StringUtils.isEmpty(opPcrEntrustOrderItem.getOpPcrEntrustOrderItemId())) {
            opPcrEntrustOrderItem.setOpPcrEntrustOrderItemId(IdUtils.simpleUUID());
        }
        // 自动填充创建/更新信息
        opPcrEntrustOrderItem.fillCreateInfo();
        return opPcrEntrustOrderItemMapper.insertOpPcrEntrustOrderItem(opPcrEntrustOrderItem);
    }

    @Override
    public List<OpPcrEntrustOrderItem> getBaseByEntrustOrderNo(String pcrTaskItemType, String entrustOrderNo) {
        return opPcrEntrustOrderItemMapper.getBaseByEntrustOrderNo(pcrTaskItemType,entrustOrderNo);
    }

    @Override
    public List<OpPcrEntrustOrderItem> getBaseByResultNo(String resultNo) {
        return opPcrEntrustOrderItemMapper.getBaseByResultNo(resultNo);
    }
}
