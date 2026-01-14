package com.gmlimsqi.business.basicdata.mapper;

import com.gmlimsqi.business.basicdata.domain.LabtestItems;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 检测项目Mapper接口
 * 
 * @author hhy
 * @date 2025-08-05
 */
public interface LabtestItemsMapper
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
     * 删除检测项目
     * 
     * @param LabtestItemsId 检测项目主键
     * @return 结果
     */
    public int deleteBsLabtestItemsByLabtestItemsId(String LabtestItemsId);

    /**
     * 批量删除检测项目
     * 
     * @param LabtestItemsIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteBsLabtestItemsByLabtestItemsIds(String[] LabtestItemsIds);

    /**
     * 更新启用状态
     * @param labtestItems
     * @return
     */
    public int updateEnableById(LabtestItems labtestItems);

    /**
     * 检查itemCode是否已存在
     * @param itemCode 检测项目编码
     * @param excludeId 需要排除的ID（用于更新场景）
     * @return 存在返回true，否则false
     */
    public boolean isItemCodeExist(@Param("itemCode") String itemCode,
                            @Param("excludeId") String excludeId);

    public LabtestItems selectBsLabtestItemsDetailByLabtestItemsId(String labtestItemsId);

    /**
     * 根据项目名称，查询检测项目
     * @param message
     * @return
     */
    public LabtestItems selectBsLabtestItemsByLabtestItemsName(String message);
}
