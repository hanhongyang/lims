package com.gmlimsqi.business.util;

import com.gmlimsqi.common.core.domain.entity.SysUser;
import com.gmlimsqi.common.core.redis.RedisCache;
import com.gmlimsqi.system.mapper.SysUserMapper;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
public class UserCacheService {
    private static final String USERNAME_KEY = "sys_user_name:";
    private static final Integer CACHE_EXPIRE = 120; // 分钟

    @Autowired
    private SysUserMapper userMapper;
    @Autowired
    private RedisCache redisCache;

    /**
     * 批量获取用户名（输入输出都是String类型的用户ID）
     * @param stringUserIds 字符串形式的用户ID集合
     * @return Map<用户ID字符串, 用户名>
     */
    public Map<String, String> batchGetUsernames(Collection<String> stringUserIds) {
        // 1. 过滤并转换有效的Long型ID
        List<Long> longUserIds = stringUserIds.stream()
                .map(this::safeParseLong)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        if (longUserIds.isEmpty()) {
            return Collections.emptyMap();
        }

        // 2. 从Redis批量获取
        Map<String, String> result = new HashMap<>();
        List<Long> missIds = new ArrayList<>();

        for (Long userId : longUserIds) {
            String username = redisCache.getCacheObject(USERNAME_KEY + userId);
            if (username != null) {
                result.put(userId.toString(), username);
            } else {
                missIds.add(userId);
            }
        }

        // 3. 处理未命中缓存的ID
        if (!missIds.isEmpty()) {
            List<SysUser> dbResults = userMapper.selectUsernamesByIds(missIds);
            dbResults.forEach(user -> {
                redisCache.setCacheObject(
                        USERNAME_KEY + Objects.toString(user.getUserId()),
                        user.getNickName(),
                        CACHE_EXPIRE,
                        TimeUnit.MINUTES
                );
                result.put(Objects.toString(user.getUserId()), user.getNickName()); // 转为String作为外部输出
            });
        }

        return result;
    }

    private Long safeParseLong(String str) {
        try {
            return str != null ? Long.parseLong(str) : null;
        } catch (NumberFormatException e) {

            return null;
        }
    }

    public String getUsername(String createBy) {
        return batchGetUsernames(Collections.singleton(createBy)).get(createBy);
    }
}