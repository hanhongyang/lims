package com.gmlimsqi.business.labtest.service.impl;

import com.gmlimsqi.business.basicdata.domain.OpJczxPcrReportInfo;
import com.gmlimsqi.business.labtest.mapper.OpJczxPcrReportInfoMapper;
import com.gmlimsqi.business.labtest.service.IOpJczxPcrReportInfoService;
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
 * pcr报告子Service业务层处理
 * 
 * @author hhy
 * @date 2025-10-20
 */
@Service
public class OpJczxPcrReportInfoServiceImpl implements IOpJczxPcrReportInfoService
{
    @Autowired
    private OpJczxPcrReportInfoMapper opJczxPcrReportInfoMapper;
    @Autowired
    private UserCacheService userCacheService;

    /**
     * 查询pcr报告子
     * 
     * @param opJczxPcrReportInfoId pcr报告子主键
     * @return pcr报告子
     */
    @Override
    public OpJczxPcrReportInfo selectOpJczxPcrReportInfoByOpJczxPcrReportInfoId(String opJczxPcrReportInfoId)
    {
        return opJczxPcrReportInfoMapper.selectOpJczxPcrReportInfoByOpJczxPcrReportInfoId(opJczxPcrReportInfoId);
    }

    /**
     * 查询pcr报告子列表
     * 
     * @param opJczxPcrReportInfo pcr报告子
     * @return pcr报告子
     */
    @Override
    public List<OpJczxPcrReportInfo> selectOpJczxPcrReportInfoList(OpJczxPcrReportInfo opJczxPcrReportInfo)
    {
        List<OpJczxPcrReportInfo> items = opJczxPcrReportInfoMapper.selectOpJczxPcrReportInfoList(opJczxPcrReportInfo);

        // 批量处理用户名
        if (!items.isEmpty()) {
            Set<String> userIds = items.stream()
                    .map(OpJczxPcrReportInfo::getCreateBy)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toSet());

            Map<String, String> usernameMap = userCacheService.batchGetUsernames(userIds);

            items.forEach(vo ->
                    vo.setCreateBy(usernameMap.get(vo.getCreateBy())));
        }

        return items;
    }

    /**
     * 新增pcr报告子
     * 
     * @param opJczxPcrReportInfo pcr报告子
     * @return 结果
     */
    @Override
    public int insertOpJczxPcrReportInfo(OpJczxPcrReportInfo opJczxPcrReportInfo)
    {
        if (StringUtils.isEmpty(opJczxPcrReportInfo.getOpJczxPcrReportInfoId())) {
            opJczxPcrReportInfo.setOpJczxPcrReportInfoId(IdUtils.simpleUUID());
        }
        // 自动填充创建/更新信息
        opJczxPcrReportInfo.fillCreateInfo();
        return opJczxPcrReportInfoMapper.insertOpJczxPcrReportInfo(opJczxPcrReportInfo);
    }

    /**
     * 修改pcr报告子
     * 
     * @param opJczxPcrReportInfo pcr报告子
     * @return 结果
     */
    @Override
    public int updateOpJczxPcrReportInfo(OpJczxPcrReportInfo opJczxPcrReportInfo)
    {
        // 自动填充更新信息
        opJczxPcrReportInfo.fillUpdateInfo();
        return opJczxPcrReportInfoMapper.updateOpJczxPcrReportInfo(opJczxPcrReportInfo);
    }

    /**
     * 批量删除pcr报告子
     * 
     * @param opJczxPcrReportInfoIds 需要删除的pcr报告子主键
     * @return 结果
     */
    @Override
    public int deleteOpJczxPcrReportInfoByOpJczxPcrReportInfoIds(String[] opJczxPcrReportInfoIds)
    {
        return opJczxPcrReportInfoMapper.deleteOpJczxPcrReportInfoByOpJczxPcrReportInfoIds(opJczxPcrReportInfoIds);
    }

    /**
     * 删除pcr报告子信息
     * 
     * @param opJczxPcrReportInfoId pcr报告子主键
     * @return 结果
     */
    @Override
    public int deleteOpJczxPcrReportInfoByOpJczxPcrReportInfoId(String opJczxPcrReportInfoId)
    {
        return opJczxPcrReportInfoMapper.deleteOpJczxPcrReportInfoByOpJczxPcrReportInfoId(opJczxPcrReportInfoId);
    }
}
