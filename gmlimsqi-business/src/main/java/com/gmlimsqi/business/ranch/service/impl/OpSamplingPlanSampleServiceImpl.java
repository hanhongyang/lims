package com.gmlimsqi.business.ranch.service.impl;


import com.gmlimsqi.business.ranch.domain.OpSamplingPlanSample;
import com.gmlimsqi.business.ranch.mapper.OpSamplingPlanSampleMapper;
import com.gmlimsqi.business.ranch.service.IOpSamplingPlanSampleService;
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
 * 取样计划-样品Service业务层处理
 * 
 * @author hhy
 * @date 2025-11-04
 */
@Service
public class OpSamplingPlanSampleServiceImpl implements IOpSamplingPlanSampleService
{
    @Autowired
    private OpSamplingPlanSampleMapper opSamplingPlanSampleMapper;
    @Autowired
    private UserCacheService userCacheService;

    /**
     * 查询取样计划-样品
     * 
     * @param opSamplingPlanSampleId 取样计划-样品主键
     * @return 取样计划-样品
     */
    @Override
    public OpSamplingPlanSample selectOpSamplingPlanSampleByOpSamplingPlanSampleId(String opSamplingPlanSampleId)
    {
        return opSamplingPlanSampleMapper.selectOpSamplingPlanSampleByOpSamplingPlanSampleId(opSamplingPlanSampleId);
    }

    /**
     * 查询取样计划-样品列表
     * 
     * @param opSamplingPlanSample 取样计划-样品
     * @return 取样计划-样品
     */
    @Override
    public List<OpSamplingPlanSample> selectOpSamplingPlanSampleList(OpSamplingPlanSample opSamplingPlanSample)
    {
        List<OpSamplingPlanSample> items = opSamplingPlanSampleMapper.selectOpSamplingPlanSampleList(opSamplingPlanSample);

        // 批量处理用户名
        if (!items.isEmpty()) {
            Set<String> userIds = items.stream()
                    .map(OpSamplingPlanSample::getCreateBy)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toSet());

            Map<String, String> usernameMap = userCacheService.batchGetUsernames(userIds);

            items.forEach(vo ->
                    vo.setCreateBy(usernameMap.get(vo.getCreateBy())));
        }

        return items;
    }

    /**
     * 新增取样计划-样品
     * 
     * @param opSamplingPlanSample 取样计划-样品
     * @return 结果
     */
    @Override
    public int insertOpSamplingPlanSample(OpSamplingPlanSample opSamplingPlanSample)
    {
        if (StringUtils.isEmpty(opSamplingPlanSample.getOpSamplingPlanSampleId())) {
            opSamplingPlanSample.setOpSamplingPlanSampleId(IdUtils.simpleUUID());

        }
        opSamplingPlanSample.setIsPushSap("0");
        // 自动填充创建/更新信息
        opSamplingPlanSample.fillCreateInfo();
        return opSamplingPlanSampleMapper.insertOpSamplingPlanSample(opSamplingPlanSample);
    }

    /**
     * 修改取样计划-样品
     * 
     * @param opSamplingPlanSample 取样计划-样品
     * @return 结果
     */
    @Override
    public int updateOpSamplingPlanSample(OpSamplingPlanSample opSamplingPlanSample)
    {
        // 自动填充更新信息
        opSamplingPlanSample.fillUpdateInfo();
        return opSamplingPlanSampleMapper.updateOpSamplingPlanSample(opSamplingPlanSample);
    }

    /**
     * 批量删除取样计划-样品
     * 
     * @param opSamplingPlanSampleIds 需要删除的取样计划-样品主键
     * @return 结果
     */
    @Override
    public int deleteOpSamplingPlanSampleByOpSamplingPlanSampleIds(String[] opSamplingPlanSampleIds)
    {
        return opSamplingPlanSampleMapper.deleteOpSamplingPlanSampleByOpSamplingPlanSampleIds(opSamplingPlanSampleIds);
    }

    /**
     * 删除取样计划-样品信息
     * 
     * @param opSamplingPlanSampleId 取样计划-样品主键
     * @return 结果
     */
    @Override
    public int deleteOpSamplingPlanSampleByOpSamplingPlanSampleId(String opSamplingPlanSampleId)
    {
        return opSamplingPlanSampleMapper.deleteOpSamplingPlanSampleByOpSamplingPlanSampleId(opSamplingPlanSampleId);
    }
}
