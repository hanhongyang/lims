package com.gmlimsqi.business.labtest.service.impl;

import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.List;

import com.gmlimsqi.business.labtest.domain.OpNearInfraredSummary;
import com.gmlimsqi.business.labtest.mapper.OpNearInfraredSummaryMapper;
import com.gmlimsqi.business.labtest.service.IOpNearInfraredSummaryService;
import com.gmlimsqi.business.util.UserCacheService;
import com.gmlimsqi.common.utils.DateUtils;
import com.gmlimsqi.common.utils.SecurityUtils;
import com.gmlimsqi.common.utils.StringUtils;
import com.gmlimsqi.common.utils.uuid.IdUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 近红外汇总Service业务层处理
 * 
 * @author hhy
 * @date 2025-10-29
 */
@Service
public class OpNearInfraredSummaryServiceImpl implements IOpNearInfraredSummaryService
{
    @Autowired
    private OpNearInfraredSummaryMapper opNearInfraredSummaryMapper;
    @Autowired
    private UserCacheService userCacheService;

    /**
     * 导入近红外分析结果
     */
    @Transactional
    @Override
    public int importNearInfraredSummary(List<OpNearInfraredSummary> list) {
        int count = 0;
        for (OpNearInfraredSummary opNearInfraredSummary : list) {
            // 设置默认值
            opNearInfraredSummary.setImportTime(DateUtils.getNowDate());
            opNearInfraredSummary.setImportBy(SecurityUtils.getUserId().toString());

            // 查询是否存在该数据
            OpNearInfraredSummary existing = opNearInfraredSummaryMapper.selectOpNearInfraredSummaryByDairylandId(opNearInfraredSummary.getDairylandId());
            // 存在覆盖原数据
            if (existing != null) {
                // 覆盖原数据
                count += opNearInfraredSummaryMapper.updateOpNearInfraredSummary(opNearInfraredSummary);
            }else {
                // 插入数据库
                count += opNearInfraredSummaryMapper.insertOpNearInfraredSummary(opNearInfraredSummary);
            }
        }
        return count;
    }

    /**
     * 查询近红外汇总
     * 
     * @param dairylandId 近红外汇总主键
     * @return 近红外汇总
     */
    @Override
    public OpNearInfraredSummary selectOpNearInfraredSummaryByDairylandId(String dairylandId)
    {
        return opNearInfraredSummaryMapper.selectOpNearInfraredSummaryByDairylandId(dairylandId);
    }

    /**
     * 查询近红外汇总列表
     * 
     * @param opNearInfraredSummary 近红外汇总
     * @return 近红外汇总
     */
    @Override
    public List<OpNearInfraredSummary> selectOpNearInfraredSummaryList(OpNearInfraredSummary opNearInfraredSummary)
    {
        List<OpNearInfraredSummary> items = opNearInfraredSummaryMapper.selectOpNearInfraredSummaryList(opNearInfraredSummary);

        // 批量处理用户名
        if (!items.isEmpty()) {
            Set<String> userIds = items.stream()
                    .map(OpNearInfraredSummary::getCreateBy)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toSet());

            Map<String, String> usernameMap = userCacheService.batchGetUsernames(userIds);

            items.forEach(vo ->
                    vo.setCreateBy(usernameMap.get(vo.getCreateBy())));
        }

        return items;
    }

    /**
     * 新增近红外汇总
     * 
     * @param opNearInfraredSummary 近红外汇总
     * @return 结果
     */
    @Override
    public int insertOpNearInfraredSummary(OpNearInfraredSummary opNearInfraredSummary)
    {
        if (StringUtils.isEmpty(opNearInfraredSummary.getDairylandId())) {
            opNearInfraredSummary.setDairylandId(IdUtils.simpleUUID());
        }
        // 自动填充创建/更新信息
        opNearInfraredSummary.fillCreateInfo();
        return opNearInfraredSummaryMapper.insertOpNearInfraredSummary(opNearInfraredSummary);
    }

    /**
     * 修改近红外汇总
     * 
     * @param opNearInfraredSummary 近红外汇总
     * @return 结果
     */
    @Override
    public int updateOpNearInfraredSummary(OpNearInfraredSummary opNearInfraredSummary)
    {
        // 自动填充更新信息
        opNearInfraredSummary.fillUpdateInfo();
        return opNearInfraredSummaryMapper.updateOpNearInfraredSummary(opNearInfraredSummary);
    }

    /**
     * 批量删除近红外汇总
     * 
     * @param dairylandIds 需要删除的近红外汇总主键
     * @return 结果
     */
    @Override
    public int deleteOpNearInfraredSummaryByDairylandIds(String[] dairylandIds)
    {
        return opNearInfraredSummaryMapper.deleteOpNearInfraredSummaryByDairylandIds(dairylandIds);
    }

    /**
     * 删除近红外汇总信息
     * 
     * @param dairylandId 近红外汇总主键
     * @return 结果
     */
    @Override
    public int deleteOpNearInfraredSummaryByDairylandId(String dairylandId)
    {
        return opNearInfraredSummaryMapper.deleteOpNearInfraredSummaryByDairylandId(dairylandId);
    }
}
