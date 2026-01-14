package com.gmlimsqi.business.labtest.service;

import com.gmlimsqi.business.labtest.domain.OpOutentrustRegisterItem;

import java.util.List;

/**
 * 外部委托检测单化验项目子Service接口
 *
 * @author wgq
 * @date 2025-09-17
 */
public interface IOpOutentrustRegisterItemService
{
    /**
     * 查询外部委托检测单化验项目子
     *
     * @param outentrustRegisterItemId 外部委托检测单化验项目子主键
     * @return 外部委托检测单化验项目子
     */
    public OpOutentrustRegisterItem selectOpOutentrustRegisterItemByOutentrustRegisterItemId(String outentrustRegisterItemId);

    /**
     * 查询外部委托检测单化验项目子列表
     *
     * @param opOutentrustRegisterItem 外部委托检测单化验项目子
     * @return 外部委托检测单化验项目子集合
     */
    public List<OpOutentrustRegisterItem> selectOpOutentrustRegisterItemList(OpOutentrustRegisterItem opOutentrustRegisterItem);

    /**
     * 新增外部委托检测单化验项目子
     *
     * @param opOutentrustRegisterItem 外部委托检测单化验项目子
     * @return 结果
     */
    public int insertOpOutentrustRegisterItem(OpOutentrustRegisterItem opOutentrustRegisterItem);

    /**
     * 修改外部委托检测单化验项目子
     *
     * @param opOutentrustRegisterItem 外部委托检测单化验项目子
     * @return 结果
     */
    public int updateOpOutentrustRegisterItem(OpOutentrustRegisterItem opOutentrustRegisterItem);

    /**
     * 批量删除外部委托检测单化验项目子
     *
     * @param outentrustRegisteritemId 需要删除的外部委托检测单化验项目子主键集合
     * @return 结果
     */
    public int deleteOpOutentrustRegisterItemByOutentrustRegisteritemId(String[] outentrustRegisteritemId);

    /**
     * 删除外部委托检测单化验项目子信息
     *
     * @param outentrustRegisterItemId 外部委托检测单化验项目子主键
     * @return 结果
     */
    public int deleteOpOutentrustRegisterItemByOutentrustRegisterItemId(String outentrustRegisterItemId);
}
