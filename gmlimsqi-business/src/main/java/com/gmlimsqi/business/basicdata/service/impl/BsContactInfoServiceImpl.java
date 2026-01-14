package com.gmlimsqi.business.basicdata.service.impl;

import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.List;

import com.gmlimsqi.business.basicdata.mapper.BsContactInfoMapper;
import com.gmlimsqi.business.util.UserCacheService;
import com.gmlimsqi.common.utils.StringUtils;
import com.gmlimsqi.common.utils.uuid.IdUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.gmlimsqi.business.basicdata.domain.BsContactInfo;
import com.gmlimsqi.business.basicdata.service.IBsContactInfoService;

/**
 * 通讯方式联系人子Service业务层处理
 *
 * @author wgq
 * @date 2025-09-15
 */
@Service
public class BsContactInfoServiceImpl implements IBsContactInfoService
{
    @Autowired
    private BsContactInfoMapper bsContactInfoMapper;
    @Autowired
    private UserCacheService userCacheService;

    /**
     * 查询通讯方式联系人子
     *
     * @param bsContactInfoId 通讯方式联系人子主键
     * @return 通讯方式联系人子
     */
    @Override
    public BsContactInfo selectBsContactInfoByBsContactInfoId(String bsContactInfoId)
    {
        return bsContactInfoMapper.selectBsContactInfoByBsContactInfoId(bsContactInfoId);
    }

    /**
     * 查询通讯方式联系人子列表
     *
     * @param bsContactInfo 通讯方式联系人子
     * @return 通讯方式联系人子
     */
    @Override
    public List<BsContactInfo> selectBsContactInfoList(BsContactInfo bsContactInfo)
    {
        List<BsContactInfo> items = bsContactInfoMapper.selectBsContactInfoList(bsContactInfo);

        // 批量处理用户名
        if (!items.isEmpty()) {
            Set<String> userIds = items.stream()
                    .map(BsContactInfo::getCreateBy)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toSet());

            Map<String, String> usernameMap = userCacheService.batchGetUsernames(userIds);

            items.forEach(vo ->
                    vo.setCreateBy(usernameMap.get(vo.getCreateBy())));
        }

        return items;
    }

    /**
     * 新增通讯方式联系人子
     *
     * @param bsContactInfo 通讯方式联系人子
     * @return 结果
     */
    @Override
    public int insertBsContactInfo(BsContactInfo bsContactInfo)
    {
        if (StringUtils.isEmpty(bsContactInfo.getBsContactInfoId())) {
            bsContactInfo.setBsContactInfoId(IdUtils.simpleUUID());
        }
        // 自动填充创建/更新信息
        bsContactInfo.fillCreateInfo();
        return bsContactInfoMapper.insertBsContactInfo(bsContactInfo);
    }

    /**
     * 修改通讯方式联系人子
     *
     * @param bsContactInfo 通讯方式联系人子
     * @return 结果
     */
    @Override
    public int updateBsContactInfo(BsContactInfo bsContactInfo)
    {
        // 自动填充更新信息
        bsContactInfo.fillUpdateInfo();
        return bsContactInfoMapper.updateBsContactInfo(bsContactInfo);
    }

    /**
     * 批量删除通讯方式联系人子
     *
     * @param bsContactInfoIds 需要删除的通讯方式联系人子主键
     * @return 结果
     */
    @Override
    public int deleteBsContactInfoByBsContactInfoIds(String[] bsContactInfoIds)
    {
        return bsContactInfoMapper.deleteBsContactInfoByBsContactInfoIds(bsContactInfoIds);
    }

    /**
     * 删除通讯方式联系人子信息
     *
     * @param bsContactInfoId 通讯方式联系人子主键
     * @return 结果
     */
    @Override
    public int deleteBsContactInfoByBsContactInfoId(String bsContactInfoId)
    {
        return bsContactInfoMapper.deleteBsContactInfoByBsContactInfoId(bsContactInfoId);
    }
}
