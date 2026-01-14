package com.gmlimsqi.business.basicdata.service.impl;

import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.List;
import com.gmlimsqi.common.utils.StringUtils;
import com.gmlimsqi.common.utils.uuid.IdUtils;
import com.gmlimsqi.business.util.UserCacheService;
// +++
import com.gmlimsqi.common.utils.SecurityUtils;
import com.gmlimsqi.common.core.domain.model.LoginUser;
import com.gmlimsqi.common.annotation.DataScope;
// +++
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.gmlimsqi.business.basicdata.mapper.BsQualityInspectionSyncMapper;
import com.gmlimsqi.business.basicdata.domain.BsQualityInspectionSync;
import com.gmlimsqi.business.basicdata.service.IBsQualityInspectionSyncService;

/**
 * 质检信息同步Service业务层处理
 * * @author hhy
 * @date 2025-11-20
 */
@Service
public class BsQualityInspectionSyncServiceImpl implements IBsQualityInspectionSyncService
{
    @Autowired
    private BsQualityInspectionSyncMapper bsQualityInspectionSyncMapper;
    @Autowired
    private UserCacheService userCacheService;

    /**
     * 查询质检信息同步
     * * @param id 质检信息同步主键
     * @return 质检信息同步
     */
    @Override
    public BsQualityInspectionSync selectBsQualityInspectionSyncById(String id)
    {
        return bsQualityInspectionSyncMapper.selectBsQualityInspectionSyncById(id);
    }

    /**
     * 查询质检信息同步列表
     * * @param bsQualityInspectionSync 质检信息同步
     * @return 质检信息同步
     */
    // +++ 新增：@DataScope 注解以激活数据权限切面 +++
    @Override
    public List<BsQualityInspectionSync> selectBsQualityInspectionSyncList(BsQualityInspectionSync bsQualityInspectionSync)
    {
        List<BsQualityInspectionSync> items = bsQualityInspectionSyncMapper.selectBsQualityInspectionSyncList(bsQualityInspectionSync);

        // 批量处理用户名
        if (!items.isEmpty()) {
            Set<String> userIds = items.stream()
                    .map(BsQualityInspectionSync::getCreateBy)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toSet());

            Map<String, String> usernameMap = userCacheService.batchGetUsernames(userIds);

            items.forEach(vo ->
                    vo.setCreateBy(usernameMap.get(vo.getCreateBy())));
        }

        return items;
    }

    /**
     * 新增质检信息同步
     * * @param bsQualityInspectionSync 质检信息同步
     * @return 结果
     */
    @Override
    public int insertBsQualityInspectionSync(BsQualityInspectionSync bsQualityInspectionSync)
    {
        if (StringUtils.isEmpty(bsQualityInspectionSync.getId())) {
            bsQualityInspectionSync.setId(IdUtils.simpleUUID());
        }

        // +++ 自动填充部门ID，用于数据权限 +++
        LoginUser loginUser = SecurityUtils.getLoginUser();

        // 自动填充创建/更新信息
        bsQualityInspectionSync.fillCreateInfo();
        return bsQualityInspectionSyncMapper.insertBsQualityInspectionSync(bsQualityInspectionSync);
    }

    /**
     * 修改质检信息同步
     * * @param bsQualityInspectionSync 质检信息同步
     * @return 结果
     */
    @Override
    public int updateBsQualityInspectionSync(BsQualityInspectionSync bsQualityInspectionSync)
    {
        // 自动填充更新信息
        bsQualityInspectionSync.fillUpdateInfo();
        return bsQualityInspectionSyncMapper.updateBsQualityInspectionSync(bsQualityInspectionSync);
    }





}