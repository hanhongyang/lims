package com.gmlimsqi.business.basicdata.service;

import java.util.List;
import com.gmlimsqi.business.basicdata.domain.BsLabtestItemFeature;
import com.gmlimsqi.business.basicdata.dto.BatchAddItemFeatureDto;
import com.gmlimsqi.business.basicdata.vo.BsLabtestItemFeatureVo;

/**
 * 检测项目特性对应Service接口
 * 
 * @author hhy
 * @date 2025-09-05
 */
public interface IBsLabtestItemFeatureService 
{
    /**
     * 查询检测项目特性对应
     * 
     * @param bsLabtestItemFeatureId 检测项目特性对应主键
     * @return 检测项目特性对应
     */
    public BsLabtestItemFeature selectBsLabtestItemFeatureByBsLabtestItemFeatureId(String bsLabtestItemFeatureId);

    /**
     * 查询检测项目特性对应列表
     * 
     * @param bsLabtestItemFeature 检测项目特性对应
     * @return 检测项目特性对应集合
     */
    public List<BsLabtestItemFeature> selectBsLabtestItemFeatureList(BsLabtestItemFeature bsLabtestItemFeature);

    /**
     * 新增检测项目特性对应
     * 
     * @param bsLabtestItemFeature 检测项目特性对应
     * @return 结果
     */
    public int insertBsLabtestItemFeature(BsLabtestItemFeature bsLabtestItemFeature);

    /**
     * 修改检测项目特性对应
     * 
     * @param bsLabtestItemFeature 检测项目特性对应
     * @return 结果
     */
    public int updateBsLabtestItemFeature(BsLabtestItemFeature bsLabtestItemFeature);


    /**
     * 以项目分组的特性list
     * @param bsLabtestItemFeature
     * @return
     */
    public List<BsLabtestItemFeatureVo> selectListGroupByItem(BsLabtestItemFeature bsLabtestItemFeature);

    /**
     * 批量新增
     * @param batchAddDto
     * @return
     */
    public int batchAdd(BatchAddItemFeatureDto batchAddDto);

    public int batchUpdate(BatchAddItemFeatureDto batchAddDto);

    List<BsLabtestItemFeature> selectBsLabtestItemFeatureByItemId(String itemId);

    int updateDeleteFlagById(String bsLabtestItemFeatureId);
}
