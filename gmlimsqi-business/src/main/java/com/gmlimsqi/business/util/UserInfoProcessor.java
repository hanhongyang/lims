package com.gmlimsqi.business.util;

import com.gmlimsqi.common.core.domain.BaseEntity;
import com.gmlimsqi.common.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 用户信息处理工具类（增强版）
 */
@Component
public class UserInfoProcessor {
    
    @Autowired
    private UserCacheService userCacheService;
    
    /**
     * 处理标准BaseEntity的用户信息
     */
    public void processBaseEntityUserInfo(List<? extends BaseEntity> entityList) {
        if (entityList == null || entityList.isEmpty()) {
            return;
        }
        
        Set<String> userIds = new HashSet<>();
        
        for (BaseEntity entity : entityList) {
            if (StringUtils.isNotEmpty(entity.getCreateBy())) {
                userIds.add(entity.getCreateBy());
            }
            if (StringUtils.isNotEmpty(entity.getUpdateBy())) {
                userIds.add(entity.getUpdateBy());
            }
        }
        
        if (userIds.isEmpty()) {
            return;
        }
        
        Map<String, String> usernameMap = userCacheService.batchGetUsernames(userIds);
        
        for (BaseEntity entity : entityList) {
            if (StringUtils.isNotEmpty(entity.getCreateBy())) {
                entity.setCreateBy(usernameMap.getOrDefault(entity.getCreateBy(), entity.getCreateBy()));
            }
            if (StringUtils.isNotEmpty(entity.getUpdateBy())) {
                entity.setUpdateBy(usernameMap.getOrDefault(entity.getUpdateBy(), entity.getUpdateBy()));
            }
        }
    }
}

