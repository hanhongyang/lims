package com.gmlimsqi.business.basicdata.service.impl;

import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.List;

import com.gmlimsqi.business.util.UserCacheService;
import com.gmlimsqi.business.util.UserInfoProcessor;
import com.gmlimsqi.common.utils.StringUtils;
import com.gmlimsqi.common.utils.uuid.IdUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.gmlimsqi.business.basicdata.mapper.BsLabtestFeatureMapper;
import com.gmlimsqi.business.basicdata.domain.BsLabtestFeature;
import com.gmlimsqi.business.basicdata.service.IBsLabtestFeatureService;

/**
 * 检测特性Service业务层处理
 * 
 * @author hhy
 * @date 2025-09-05
 */
@Service
public class BsLabtestFeatureServiceImpl implements IBsLabtestFeatureService 
{
    @Autowired
    private BsLabtestFeatureMapper bsLabtestFeatureMapper;
    @Autowired
    private UserInfoProcessor userInfoProcessor;

    /**
     * 查询检测特性
     * 
     * @param bsLabtestFeatureId 检测特性主键
     * @return 检测特性
     */
    @Override
    public BsLabtestFeature selectBsLabtestFeatureByBsLabtestFeatureId(String bsLabtestFeatureId)
    {
        return bsLabtestFeatureMapper.selectBsLabtestFeatureByBsLabtestFeatureId(bsLabtestFeatureId);
    }

    /**
     * 查询检测特性列表
     * 
     * @param bsLabtestFeature 检测特性
     * @return 检测特性
     */
    @Override
    public List<BsLabtestFeature> selectBsLabtestFeatureList(BsLabtestFeature bsLabtestFeature)
    {

        List<BsLabtestFeature> items = bsLabtestFeatureMapper.selectBsLabtestFeatureList(bsLabtestFeature);

        // 批量处理用户名（创建人和更新人）
        userInfoProcessor.processBaseEntityUserInfo(items);


        return items;
    }

    /**
     * 新增检测特性
     * 
     * @param bsLabtestFeature 检测特性
     * @return 结果
     */
    @Override
    public int insertBsLabtestFeature(BsLabtestFeature bsLabtestFeature)
    {
        if (StringUtils.isEmpty(bsLabtestFeature.getBsLabtestFeatureId())) {
            bsLabtestFeature.setBsLabtestFeatureId(IdUtils.simpleUUID());
        }

        // 自动填充创建/更新信息
        bsLabtestFeature.fillCreateInfo();
        return bsLabtestFeatureMapper.insertBsLabtestFeature(bsLabtestFeature);
    }

    /**
     * 修改检测特性
     * 
     * @param bsLabtestFeature 检测特性
     * @return 结果
     */
    @Override
    public int updateBsLabtestFeature(BsLabtestFeature bsLabtestFeature)
    {
        // 自动填充更新信息
        bsLabtestFeature.fillUpdateInfo();
        return bsLabtestFeatureMapper.updateBsLabtestFeature(bsLabtestFeature);
    }

    /**
     * 批量删除检测特性
     * 
     * @param bsLabtestFeatureIds 需要删除的检测特性主键
     * @return 结果
     */
    @Override
    public int deleteBsLabtestFeatureByBsLabtestFeatureIds(String[] bsLabtestFeatureIds)
    {
        return bsLabtestFeatureMapper.deleteBsLabtestFeatureByBsLabtestFeatureIds(bsLabtestFeatureIds);
    }

    /**
     * 删除检测特性信息
     * 
     * @param bsLabtestFeatureId 检测特性主键
     * @return 结果
     */
    @Override
    public int deleteBsLabtestFeatureByBsLabtestFeatureId(String bsLabtestFeatureId)
    {
        return bsLabtestFeatureMapper.deleteBsLabtestFeatureByBsLabtestFeatureId(bsLabtestFeatureId);
    }

    @Override
    public int updateEnableById(BsLabtestFeature bsLabtestFeature) {
        return bsLabtestFeatureMapper.updateEnableById(bsLabtestFeature);
    }
}
