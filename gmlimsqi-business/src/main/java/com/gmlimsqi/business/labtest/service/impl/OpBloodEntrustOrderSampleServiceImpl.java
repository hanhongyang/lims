package com.gmlimsqi.business.labtest.service.impl;


import com.gmlimsqi.business.labtest.domain.OpBloodEntrustOrderSample;
import com.gmlimsqi.business.labtest.mapper.OpBloodEntrustOrderSampleMapper;
import com.gmlimsqi.business.labtest.service.IOpBloodEntrustOrderSampleService;
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
 * 血样样品委托-样品对应Service业务层处理
 *
 * @author hhy
 * @date 2025-09-20
 */
@Service
public class OpBloodEntrustOrderSampleServiceImpl implements IOpBloodEntrustOrderSampleService
{
    @Autowired
    private OpBloodEntrustOrderSampleMapper opBloodEntrustOrderSampleMapper;
    @Autowired
    private UserCacheService userCacheService;

    /**
     * 查询血样样品委托-样品对应
     *
     * @param opBloodEntrustOrderId 血样样品委托-样品对应主键
     * @return 血样样品委托-样品对应
     */
    @Override
    public OpBloodEntrustOrderSample selectOpBloodEntrustOrderSampleByOpBloodEntrustOrderId(String opBloodEntrustOrderId)
    {
        return opBloodEntrustOrderSampleMapper.selectOpBloodEntrustOrderSampleByOpBloodEntrustOrderId(opBloodEntrustOrderId);
    }

    /**
     * 查询血样样品委托-样品对应列表
     *
     * @param opBloodEntrustOrderSample 血样样品委托-样品对应
     * @return 血样样品委托-样品对应
     */
    @Override
    public List<OpBloodEntrustOrderSample> selectOpBloodEntrustOrderSampleList(OpBloodEntrustOrderSample opBloodEntrustOrderSample)
    {
        List<OpBloodEntrustOrderSample> items = opBloodEntrustOrderSampleMapper.selectOpBloodEntrustOrderSampleList(opBloodEntrustOrderSample);

        // 批量处理用户名
        if (!items.isEmpty()) {
            Set<String> userIds = items.stream()
                    .map(OpBloodEntrustOrderSample::getCreateBy)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toSet());

            Map<String, String> usernameMap = userCacheService.batchGetUsernames(userIds);

            items.forEach(vo ->
                    vo.setCreateBy(usernameMap.get(vo.getCreateBy())));
        }

        return items;
    }

    /**
     * 新增血样样品委托-样品对应
     *
     * @param opBloodEntrustOrderSample 血样样品委托-样品对应
     * @return 结果
     */
    @Override
    public int insertOpBloodEntrustOrderSample(OpBloodEntrustOrderSample opBloodEntrustOrderSample)
    {
        if (StringUtils.isEmpty(opBloodEntrustOrderSample.getOpBloodEntrustOrderSampleId())) {
            opBloodEntrustOrderSample.setOpBloodEntrustOrderSampleId(IdUtils.simpleUUID());
        }
        // 自动填充创建/更新信息
        opBloodEntrustOrderSample.fillCreateInfo();
        return opBloodEntrustOrderSampleMapper.insertOpBloodEntrustOrderSample(opBloodEntrustOrderSample);
    }

    /**
     * 修改血样样品委托-样品对应
     *
     * @param opBloodEntrustOrderSample 血样样品委托-样品对应
     * @return 结果
     */
    @Override
    public int updateOpBloodEntrustOrderSample(OpBloodEntrustOrderSample opBloodEntrustOrderSample)
    {
        // 自动填充更新信息
        opBloodEntrustOrderSample.fillUpdateInfo();
        return opBloodEntrustOrderSampleMapper.updateOpBloodEntrustOrderSample(opBloodEntrustOrderSample);
    }

    @Override
    public List<OpBloodEntrustOrderSample> getBaseByResultNo(String resultNo) {
        return opBloodEntrustOrderSampleMapper.getBaseByResultNo(resultNo);
    }


}
