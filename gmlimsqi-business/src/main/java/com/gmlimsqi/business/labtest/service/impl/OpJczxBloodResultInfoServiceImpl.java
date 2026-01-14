package com.gmlimsqi.business.labtest.service.impl;


import com.gmlimsqi.business.labtest.domain.OpJczxBloodResultInfo;
import com.gmlimsqi.business.labtest.mapper.OpJczxBloodResultInfoMapper;
import com.gmlimsqi.business.labtest.service.IOpJczxBloodResultInfoService;
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
 * 检测中心血样化验单子Service业务层处理
 * 
 * @author hhy
 * @date 2025-10-14
 */
@Service
public class OpJczxBloodResultInfoServiceImpl implements IOpJczxBloodResultInfoService
{
    @Autowired
    private OpJczxBloodResultInfoMapper opJczxBloodResultInfoMapper;
    @Autowired
    private UserCacheService userCacheService;

    /**
     * 查询检测中心血样化验单子
     * 
     * @param opJczxPcrResultInfoId 检测中心血样化验单子主键
     * @return 检测中心血样化验单子
     */
    @Override
    public OpJczxBloodResultInfo selectOpJczxBloodResultInfoByOpJczxPcrResultInfoId(String opJczxPcrResultInfoId)
    {
        return opJczxBloodResultInfoMapper.selectOpJczxBloodResultInfoByOpJczxPcrResultInfoId(opJczxPcrResultInfoId);
    }

    /**
     * 查询检测中心血样化验单子列表
     * 
     * @param opJczxBloodResultInfo 检测中心血样化验单子
     * @return 检测中心血样化验单子
     */
    @Override
    public List<OpJczxBloodResultInfo> selectOpJczxBloodResultInfoList(OpJczxBloodResultInfo opJczxBloodResultInfo)
    {
        List<OpJczxBloodResultInfo> items = opJczxBloodResultInfoMapper.selectOpJczxBloodResultInfoList(opJczxBloodResultInfo);

        // 批量处理用户名
        if (!items.isEmpty()) {
            Set<String> userIds = items.stream()
                    .map(OpJczxBloodResultInfo::getCreateBy)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toSet());

            Map<String, String> usernameMap = userCacheService.batchGetUsernames(userIds);

            items.forEach(vo ->
                    vo.setCreateBy(usernameMap.get(vo.getCreateBy())));
        }

        return items;
    }

    /**
     * 新增检测中心血样化验单子
     * 
     * @param opJczxBloodResultInfo 检测中心血样化验单子
     * @return 结果
     */
    @Override
    public int insertOpJczxBloodResultInfo(OpJczxBloodResultInfo opJczxBloodResultInfo)
    {
        if (StringUtils.isEmpty(opJczxBloodResultInfo.getOpJczxBloodResultInfoId())) {
            opJczxBloodResultInfo.setOpJczxBloodResultInfoId(IdUtils.simpleUUID());
        }
        // 自动填充创建/更新信息
        opJczxBloodResultInfo.fillCreateInfo();
        return opJczxBloodResultInfoMapper.insertOpJczxBloodResultInfo(opJczxBloodResultInfo);
    }

    /**
     * 修改检测中心血样化验单子
     * 
     * @param opJczxBloodResultInfo 检测中心血样化验单子
     * @return 结果
     */
    @Override
    public int updateOpJczxBloodResultInfo(OpJczxBloodResultInfo opJczxBloodResultInfo)
    {
        // 自动填充更新信息
        opJczxBloodResultInfo.fillUpdateInfo();
        return opJczxBloodResultInfoMapper.updateOpJczxBloodResultInfo(opJczxBloodResultInfo);
    }

    /**
     * 批量删除检测中心血样化验单子
     * 
     * @param opJczxPcrResultInfoIds 需要删除的检测中心血样化验单子主键
     * @return 结果
     */
    @Override
    public int deleteOpJczxBloodResultInfoByOpJczxPcrResultInfoIds(String[] opJczxPcrResultInfoIds)
    {
        return opJczxBloodResultInfoMapper.deleteOpJczxBloodResultInfoByOpJczxPcrResultInfoIds(opJczxPcrResultInfoIds);
    }

    /**
     * 删除检测中心血样化验单子信息
     * 
     * @param opJczxPcrResultInfoId 检测中心血样化验单子主键
     * @return 结果
     */
    @Override
    public int deleteOpJczxBloodResultInfoByOpJczxPcrResultInfoId(String opJczxPcrResultInfoId)
    {
        return opJczxBloodResultInfoMapper.deleteOpJczxBloodResultInfoByOpJczxPcrResultInfoId(opJczxPcrResultInfoId);
    }
}
