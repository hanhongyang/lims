package com.gmlimsqi.business.labtest.controller;

import com.gmlimsqi.business.labtest.domain.OpOutentrustRegisterSample;
import com.gmlimsqi.business.labtest.mapper.OpOutentrustRegisterSampleMapper;
import com.gmlimsqi.business.labtest.service.IOpOutentrustRegisterSampleService;
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
 * 外部委托检测单样品子Service业务层处理
 *
 * @author wgq
 * @date 2025-09-17
 */
@Service
public class OpOutentrustRegisterSampleServiceImpl implements IOpOutentrustRegisterSampleService
{
    @Autowired
    private OpOutentrustRegisterSampleMapper opOutentrustRegisterSampleMapper;
    @Autowired
    private UserCacheService userCacheService;

    /**
     * 查询外部委托检测单样品子
     *
     * @param outentrustRegisterSampleId 外部委托检测单样品子主键
     * @return 外部委托检测单样品子
     */
    @Override
    public OpOutentrustRegisterSample selectOpOutentrustRegisterSampleByOutentrustRegisterSampleId(String outentrustRegisterSampleId)
    {
        return opOutentrustRegisterSampleMapper.selectOpOutentrustRegisterSampleByOutentrustRegisterSampleId(outentrustRegisterSampleId);
    }

    /**
     * 查询外部委托检测单样品子列表
     *
     * @param opOutentrustRegisterSample 外部委托检测单样品子
     * @return 外部委托检测单样品子
     */
    @Override
    public List<OpOutentrustRegisterSample> selectOpOutentrustRegisterSampleList(OpOutentrustRegisterSample opOutentrustRegisterSample)
    {
        List<OpOutentrustRegisterSample> items = opOutentrustRegisterSampleMapper.selectOpOutentrustRegisterSampleList(opOutentrustRegisterSample);

        // 批量处理用户名
        if (!items.isEmpty()) {
            Set<String> userIds = items.stream()
                    .map(OpOutentrustRegisterSample::getCreateBy)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toSet());

            Map<String, String> usernameMap = userCacheService.batchGetUsernames(userIds);

            items.forEach(vo ->
                    vo.setCreateBy(usernameMap.get(vo.getCreateBy())));
        }

        return items;
    }

    /**
     * 新增外部委托检测单样品子
     *
     * @param opOutentrustRegisterSample 外部委托检测单样品子
     * @return 结果
     */
    @Override
    public int insertOpOutentrustRegisterSample(OpOutentrustRegisterSample opOutentrustRegisterSample)
    {
        if (StringUtils.isEmpty(opOutentrustRegisterSample.getOutentrustRegisterSampleId())) {
            opOutentrustRegisterSample.setOutentrustRegisterSampleId(IdUtils.simpleUUID());
        }
        // 自动填充创建/更新信息
        opOutentrustRegisterSample.fillCreateInfo();
        return opOutentrustRegisterSampleMapper.insertOpOutentrustRegisterSample(opOutentrustRegisterSample);
    }

    /**
     * 修改外部委托检测单样品子
     *
     * @param opOutentrustRegisterSample 外部委托检测单样品子
     * @return 结果
     */
    @Override
    public int updateOpOutentrustRegisterSample(OpOutentrustRegisterSample opOutentrustRegisterSample)
    {
        // 自动填充更新信息
        opOutentrustRegisterSample.fillUpdateInfo();
        return opOutentrustRegisterSampleMapper.updateOpOutentrustRegisterSample(opOutentrustRegisterSample);
    }

    /**
     * 批量删除外部委托检测单样品子
     *
     * @param outentrustRegisterSampleIds 需要删除的外部委托检测单样品子主键
     * @return 结果
     */
    @Override
    public int deleteOpOutentrustRegisterSampleByOutentrustRegisterSampleIds(String[] outentrustRegisterSampleIds)
    {
        return opOutentrustRegisterSampleMapper.deleteOpOutentrustRegisterSampleByOutentrustRegisterSampleIds(outentrustRegisterSampleIds);
    }

    /**
     * 删除外部委托检测单样品子信息
     *
     * @param outentrustRegisterSampleId 外部委托检测单样品子主键
     * @return 结果
     */
    @Override
    public int deleteOpOutentrustRegisterSampleByOutentrustRegisterSampleId(String outentrustRegisterSampleId)
    {
        return opOutentrustRegisterSampleMapper.deleteOpOutentrustRegisterSampleByOutentrustRegisterSampleId(outentrustRegisterSampleId);
    }
}
