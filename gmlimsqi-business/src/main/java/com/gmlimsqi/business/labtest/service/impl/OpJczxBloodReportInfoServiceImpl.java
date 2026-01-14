package com.gmlimsqi.business.labtest.service.impl;


import com.gmlimsqi.business.labtest.domain.OpJczxBloodReportInfo;
import com.gmlimsqi.business.labtest.mapper.OpJczxBloodReportInfoMapper;
import com.gmlimsqi.business.labtest.service.IOpJczxBloodReportInfoService;
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
 * 检测中心血样报告子Service业务层处理
 * 
 * @author hhy
 * @date 2025-10-22
 */
@Service
public class OpJczxBloodReportInfoServiceImpl implements IOpJczxBloodReportInfoService
{
    @Autowired
    private OpJczxBloodReportInfoMapper opJczxBloodReportInfoMapper;
    @Autowired
    private UserCacheService userCacheService;

    /**
     * 查询检测中心血样报告子
     * 
     * @param opJczxBloodReportInfoId 检测中心血样报告子主键
     * @return 检测中心血样报告子
     */
    @Override
    public OpJczxBloodReportInfo selectOpJczxBloodReportInfoByOpJczxBloodReportInfoId(String opJczxBloodReportInfoId)
    {
        return opJczxBloodReportInfoMapper.selectOpJczxBloodReportInfoByOpJczxBloodReportInfoId(opJczxBloodReportInfoId);
    }

    /**
     * 查询检测中心血样报告子列表
     * 
     * @param opJczxBloodReportInfo 检测中心血样报告子
     * @return 检测中心血样报告子
     */
    @Override
    public List<OpJczxBloodReportInfo> selectOpJczxBloodReportInfoList(OpJczxBloodReportInfo opJczxBloodReportInfo)
    {
        List<OpJczxBloodReportInfo> items = opJczxBloodReportInfoMapper.selectOpJczxBloodReportInfoList(opJczxBloodReportInfo);

        // 批量处理用户名
        if (!items.isEmpty()) {
            Set<String> userIds = items.stream()
                    .map(OpJczxBloodReportInfo::getCreateBy)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toSet());

            Map<String, String> usernameMap = userCacheService.batchGetUsernames(userIds);

            items.forEach(vo ->
                    vo.setCreateBy(usernameMap.get(vo.getCreateBy())));
        }

        return items;
    }

    /**
     * 新增检测中心血样报告子
     * 
     * @param opJczxBloodReportInfo 检测中心血样报告子
     * @return 结果
     */
    @Override
    public int insertOpJczxBloodReportInfo(OpJczxBloodReportInfo opJczxBloodReportInfo)
    {
        if (StringUtils.isEmpty(opJczxBloodReportInfo.getOpJczxBloodReportInfoId())) {
            opJczxBloodReportInfo.setOpJczxBloodReportInfoId(IdUtils.simpleUUID());
        }
        // 自动填充创建/更新信息
        opJczxBloodReportInfo.fillCreateInfo();
        return opJczxBloodReportInfoMapper.insertOpJczxBloodReportInfo(opJczxBloodReportInfo);
    }

    /**
     * 修改检测中心血样报告子
     * 
     * @param opJczxBloodReportInfo 检测中心血样报告子
     * @return 结果
     */
    @Override
    public int updateOpJczxBloodReportInfo(OpJczxBloodReportInfo opJczxBloodReportInfo)
    {
        // 自动填充更新信息
        opJczxBloodReportInfo.fillUpdateInfo();
        return opJczxBloodReportInfoMapper.updateOpJczxBloodReportInfo(opJczxBloodReportInfo);
    }

    /**
     * 批量删除检测中心血样报告子
     * 
     * @param opJczxBloodReportInfoIds 需要删除的检测中心血样报告子主键
     * @return 结果
     */
    @Override
    public int deleteOpJczxBloodReportInfoByOpJczxBloodReportInfoIds(String[] opJczxBloodReportInfoIds)
    {
        return opJczxBloodReportInfoMapper.deleteOpJczxBloodReportInfoByOpJczxBloodReportInfoIds(opJczxBloodReportInfoIds);
    }

    /**
     * 删除检测中心血样报告子信息
     * 
     * @param opJczxBloodReportInfoId 检测中心血样报告子主键
     * @return 结果
     */
    @Override
    public int deleteOpJczxBloodReportInfoByOpJczxBloodReportInfoId(String opJczxBloodReportInfoId)
    {
        return opJczxBloodReportInfoMapper.deleteOpJczxBloodReportInfoByOpJczxBloodReportInfoId(opJczxBloodReportInfoId);
    }
}
