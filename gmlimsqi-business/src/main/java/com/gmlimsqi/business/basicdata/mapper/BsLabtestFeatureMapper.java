package com.gmlimsqi.business.basicdata.mapper;

import java.util.List;
import com.gmlimsqi.business.basicdata.domain.BsLabtestFeature;

/**
 * 检测特性Mapper接口
 * 
 * @author hhy
 * @date 2025-09-05
 */
public interface BsLabtestFeatureMapper 
{
    /**
     * 查询检测特性
     * 
     * @param bsLabtestFeatureId 检测特性主键
     * @return 检测特性
     */
    public BsLabtestFeature selectBsLabtestFeatureByBsLabtestFeatureId(String bsLabtestFeatureId);

    /**
     * 查询检测特性列表
     * 
     * @param bsLabtestFeature 检测特性
     * @return 检测特性集合
     */
    public List<BsLabtestFeature> selectBsLabtestFeatureList(BsLabtestFeature bsLabtestFeature);

    /**
     * 新增检测特性
     * 
     * @param bsLabtestFeature 检测特性
     * @return 结果
     */
    public int insertBsLabtestFeature(BsLabtestFeature bsLabtestFeature);

    /**
     * 修改检测特性
     * 
     * @param bsLabtestFeature 检测特性
     * @return 结果
     */
    public int updateBsLabtestFeature(BsLabtestFeature bsLabtestFeature);

    /**
     * 通过检测特性主键更新删除标志
     *
     * @param bsLabtestFeatureId 检测特性ID
     * @return 结果
     */
    public int updateDeleteFlagById(String bsLabtestFeatureId);

    /**
     * 批量通过检测特性主键更新删除标志
     *
     * @param bsLabtestFeatureId 检测特性ID
     * @return 结果
     */
    public int updateDeleteFlagByIds(String[] bsLabtestFeatureIds);

    /**
     * 删除检测特性
     * 
     * @param bsLabtestFeatureId 检测特性主键
     * @return 结果
     */
    public int deleteBsLabtestFeatureByBsLabtestFeatureId(String bsLabtestFeatureId);

    /**
     * 批量删除检测特性
     * 
     * @param bsLabtestFeatureIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteBsLabtestFeatureByBsLabtestFeatureIds(String[] bsLabtestFeatureIds);

    /**
     * 修改启用状态
     * @param bsLabtestFeature
     * @return
     */
    public int updateEnableById(BsLabtestFeature bsLabtestFeature);
}
