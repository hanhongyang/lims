package com.gmlimsqi.framework.web.service;

import java.util.concurrent.TimeUnit;

import com.gmlimsqi.common.exception.ServiceException;
import com.gmlimsqi.common.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import com.gmlimsqi.common.constant.CacheConstants;
import com.gmlimsqi.common.core.domain.entity.SysUser;
import com.gmlimsqi.common.core.redis.RedisCache;
import com.gmlimsqi.common.exception.user.UserPasswordNotMatchException;
import com.gmlimsqi.common.exception.user.UserPasswordRetryLimitExceedException;
import com.gmlimsqi.common.utils.SecurityUtils;
import com.gmlimsqi.framework.security.context.AuthenticationContextHolder;

/**
 * 登录密码方法
 * 
 * @author ruoyi
 */
@Component
public class SysPasswordService
{
    @Autowired
    private RedisCache redisCache;

    @Value(value = "${user.password.maxRetryCount}")
    private int maxRetryCount;

    @Value(value = "${user.password.lockTime}")
    private int lockTime;
    @Value("${user.password.minLength}")
    private int minLength;

    @Value("${user.password.maxLength}")
    private int maxLength;

    @Value("${user.password.regex}")
    private String regex;

    @Value("${user.password.message}")
    private String message;

    /**
     * 登录账户密码错误次数缓存键名
     * 
     * @param username 用户名
     * @return 缓存键key
     */
    private String getCacheKey(String username)
    {
        return CacheConstants.PWD_ERR_CNT_KEY + username;
    }

    public void validate(SysUser user)
    {
        Authentication usernamePasswordAuthenticationToken = AuthenticationContextHolder.getContext();
        String username = usernamePasswordAuthenticationToken.getName();
        String password = usernamePasswordAuthenticationToken.getCredentials().toString();

        Integer retryCount = redisCache.getCacheObject(getCacheKey(username));

        if (retryCount == null)
        {
            retryCount = 0;
        }

        if (retryCount >= Integer.valueOf(maxRetryCount).intValue())
        {
            throw new UserPasswordRetryLimitExceedException(maxRetryCount, lockTime);
        }

        if (!matches(user, password))
        {
            retryCount = retryCount + 1;
            redisCache.setCacheObject(getCacheKey(username), retryCount, lockTime, TimeUnit.MINUTES);
            throw new UserPasswordNotMatchException();
        }
        else
        {
            clearLoginRecordCache(username);
        }
    }
    /**
     * [!! 新增方法 !!]
     * 专门用于校验新密码的复杂度。
     * 这个方法不关心登录重试。
     *
     * @param password 明文的新密码
     */
    public void validateNewPassword(String password)
    {
        if (StringUtils.isEmpty(password))
        {
            throw new ServiceException("密码不能为空");
        }
        if (password.length() < minLength || password.length() > maxLength)
        {
            throw new ServiceException(String.format("密码长度必须在 %d 到 %d 个字符之间", minLength, maxLength));
        }

        // [!! 关键 !!]
        // 把你的校验逻辑放在这里
        if (!password.matches(regex))
        {
            throw new ServiceException(message); // "密码长度至少8位..."
        }
    }
    public boolean matches(SysUser user, String rawPassword)
    {
        return SecurityUtils.matchesPassword(rawPassword, user.getPassword());
    }

    public void clearLoginRecordCache(String loginName)
    {
        if (redisCache.hasKey(getCacheKey(loginName)))
        {
            redisCache.deleteObject(getCacheKey(loginName));
        }
    }
}
