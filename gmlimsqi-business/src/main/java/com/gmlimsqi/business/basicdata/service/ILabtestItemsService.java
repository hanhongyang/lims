package com.gmlimsqi.business.basicdata.service;

import java.util.List;
import com.gmlimsqi.business.basicdata.domain.LabtestItems;

/**
 * 检测项目Service接口
 * 
 * @author hhy
 * @date 2025-08-05
 */
public interface ILabtestItemsService
{
    /**
     * 查询检测项目
     * 
     * @param LabtestItemsId 检测项目主键
     * @return 检测项目
     */
    public LabtestItems selectBsLabtestItemsByLabtestItemsId(String LabtestItemsId);

    /**
     * 查询检测项目列表
     * 
     * @param labtestItems 检测项目
     * @return 检测项目集合
     */
    public List<LabtestItems> selectBsLabtestItemsList(LabtestItems labtestItems);

    /**
     * 新增检测项目
     * 
     * @param labtestItems 检测项目
     * @return 结果
     */
    public int insertBsLabtestItems(LabtestItems labtestItems);

    /**
     * 修改检测项目
     * 
     * @param labtestItems 检测项目
     * @return 结果
     */
    public int updateBsLabtestItems(LabtestItems labtestItems);

    /**
     * 批量删除检测项目
     * 
     * @param LabtestItemsIds 需要删除的检测项目主键集合
     * @return 结果
     */
    public int deleteBsLabtestItemsByLabtestItemsIds(String[] LabtestItemsIds);

    /**
     * 删除检测项目信息
     * 
     * @param LabtestItemsId 检测项目主键
     * @return 结果
     */
    public int deleteBsLabtestItemsByLabtestItemsId(String LabtestItemsId);

    public int updateEnableById(LabtestItems labtestItems);
}
