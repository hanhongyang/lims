package com.gmlimsqi.business.basicdata.mapper;

import java.util.List;
import com.gmlimsqi.business.basicdata.domain.BsLabtestItemFeature;
import com.gmlimsqi.business.basicdata.dto.BatchAddItemFeatureDto;
import com.gmlimsqi.business.basicdata.vo.BsLabtestItemFeatureVo;
import org.apache.ibatis.annotations.Param;

/**
 * 检测项目特性对应Mapper接口
 * 
 * @author hhy
 * @date 2025-09-05
 */
public interface BsLabtestItemFeatureMapper 
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
     * 通过检测项目特性对应主键更新删除标志
     *
     * @param bsLabtestItemFeatureId 检测项目特性对应ID
     * @param string
     * @return 结果
     */
    public int updateDeleteFlagById(@Param("bsLabtestItemFeatureId") String bsLabtestItemFeatureId,@Param("updateUserId") String updateUserId);

    /**
     * 批量通过检测项目特性对应主键更新删除标志
     *
     * @param bsLabtestItemFeatureId 检测项目特性对应ID
     * @return 结果
     */
    public int updateDeleteFlagByIds(String[] bsLabtestItemFeatureIds);

    /**
     * 按项目分组查询项目特性列表
     */
    public List<BsLabtestItemFeatureVo> selectListGroupByItem(BsLabtestItemFeature bsLabtestItemFeature);

    void updateDeleteFlagByItemId(@Param("itemId") String itemId,@Param("updateUserId") String updateUserId);

    /**
     * 分页查询项目ID列表
     */
    List<String> selectItemIdsPage(BsLabtestItemFeature bsLabtestItemFeature);

    /**
     * 根据项目ID列表查询项目特性详情
     */
    List<BsLabtestItemFeature> selectFeatureListByItemIds(@Param("itemIds") List<String> itemIds,
                                                            @Param("bsLabtestItemFeature") BsLabtestItemFeature bsLabtestItemFeature);

    void insertBatch(@Param("addList")List<BsLabtestItemFeature> addList);
}
