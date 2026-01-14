package com.gmlimsqi.framework.web.service;

import javax.annotation.Resource;

import com.dingtalk.api.response.OapiV2UserGetResponse;
import com.gmlimsqi.common.core.domain.entity.SysDept;
import com.gmlimsqi.common.utils.*;
import com.gmlimsqi.framework.security.token.DingTalkAuthenticationToken;
import com.gmlimsqi.system.mapper.SysDeptMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import com.gmlimsqi.common.constant.CacheConstants;
import com.gmlimsqi.common.constant.Constants;
import com.gmlimsqi.common.constant.UserConstants;
import com.gmlimsqi.common.core.domain.entity.SysUser;
import com.gmlimsqi.common.core.domain.model.LoginUser;
import com.gmlimsqi.common.core.redis.RedisCache;
import com.gmlimsqi.common.exception.ServiceException;
import com.gmlimsqi.common.exception.user.BlackListException;
import com.gmlimsqi.common.exception.user.CaptchaException;
import com.gmlimsqi.common.exception.user.CaptchaExpireException;
import com.gmlimsqi.common.exception.user.UserNotExistsException;
import com.gmlimsqi.common.exception.user.UserPasswordNotMatchException;
import com.gmlimsqi.common.utils.ip.IpUtils;
import com.gmlimsqi.framework.manager.AsyncManager;
import com.gmlimsqi.framework.manager.factory.AsyncFactory;
import com.gmlimsqi.framework.security.context.AuthenticationContextHolder;
import com.gmlimsqi.system.service.ISysConfigService;
import com.gmlimsqi.system.service.ISysUserService;
import org.springframework.util.ObjectUtils;

/**
 * 登录校验方法
 * 
 * @author ruoyi
 */
@Component
public class SysLoginService
{
    @Autowired
    private TokenService tokenService;

    @Resource
    private AuthenticationManager authenticationManager;

    @Autowired
    private RedisCache redisCache;
    
    @Autowired
    private ISysUserService userService;

    @Autowired
    private SysPermissionService permissionService;
    @Autowired
    private ISysConfigService configService;
    @Autowired
    private DingTalkService dingTalkService;

    @Autowired
    private SysDeptMapper deptMapper;

    /**
     * 登录验证
     *
     * @param username 用户名
     * @param password 密码
     * @param code 验证码
     * @param uuid 唯一标识
     * @return 结果
     */
    public String login(String username, String password, String code, String uuid, boolean ifCaptcha)
    {
        // 验证码校验
        validateCaptcha(username, code, uuid);

        // 添加RSA解密逻辑
        try {
            // 判断密码是否是加密的（加密的密码通常很长且包含特殊字符）
            if (password != null && password.length() > 50) {
                password = RsaUtils.decrypt(password);
            }
        } catch (Exception e) {
            AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, "密码解密失败"));
            throw new ServiceException("密码解密失败，请检查加密配置");
        }

        // 登录前置校验
        loginPreCheck(username, password);
        // 用户验证
        Authentication authentication = null;
        try
        {
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);
            AuthenticationContextHolder.setContext(authenticationToken);
            // 该方法会去调用UserDetailsServiceImpl.loadUserByUsername
            authentication = authenticationManager.authenticate(authenticationToken);
        }
        catch (Exception e)
        {
            if (e instanceof BadCredentialsException)
            {
                AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, MessageUtils.message("user.password.not.match")));
                throw new UserPasswordNotMatchException();
            }
            else
            {
                AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, e.getMessage()));
                throw new ServiceException(e.getMessage());
            }
        }
        finally
        {
            AuthenticationContextHolder.clearContext();
        }
        AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_SUCCESS, MessageUtils.message("user.login.success")));
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        recordLoginInfo(loginUser.getUserId());
        // 生成token
        return tokenService.createToken(loginUser);
    }



    /**
     * 登录验证
     *
     * @param code 验证码
     * @return 结果
     */
    public String dingTalkLoginFree(String code) throws Exception {
        System.out.println("授权码" + code);
        OapiV2UserGetResponse.UserGetResponse user = dingTalkService.getUserByCoe(code);
        if (StringUtils.isEmpty(user.getMobile())){
            throw new ServiceException("应用缺少权限，请联系管理员！");
        }
        System.out.println("用户数据+" + user.toString() + user.getMobile());
        // 用户验证
        Authentication authentication = null;
        try {
            // 该方法会去调用DingTalkDetailsServiceImpl.loadUserByUsername
            authentication = authenticationManager
                    .authenticate(new DingTalkAuthenticationToken(user.getMobile()));
        } catch (Exception e) {
            if (e instanceof BadCredentialsException) {
                AsyncManager.me().execute(AsyncFactory.recordLogininfor(user.getMobile(), Constants.LOGIN_FAIL, MessageUtils.message("user.password.not.match")));
                throw new UserPasswordNotMatchException();
            } else {
                AsyncManager.me().execute(AsyncFactory.recordLogininfor(user.getMobile(), Constants.LOGIN_FAIL, e.getMessage()));
                throw new ServiceException(e.getMessage());
            }
        }
        AsyncManager.me().execute(AsyncFactory.recordLogininfor(user.getMobile(), Constants.LOGIN_SUCCESS, MessageUtils.message("user.login.success")));
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        recordLoginInfo(loginUser.getUserId(), user.getUserid(), user.getUnionid());

        // 生成token
        return tokenService.createToken(loginUser);
    }
    /**
     * 校验验证码
     * * @param username 用户名
     * @param code 验证码
     * @param uuid 唯一标识
     * @return 结果
     */
    public void validateCaptcha(String username, String code, String uuid)
    {
        boolean captchaEnabled = configService.selectCaptchaEnabled();
        if (captchaEnabled)
        {
            // ---  开始修改  ---
            // 如果 uuid 为空 (例如手机App登录)，则不校验验证码
            if (StringUtils.isEmpty(uuid))
            {
                return;
            }
            // ---  结束修改  ---

            String verifyKey = CacheConstants.CAPTCHA_CODE_KEY + StringUtils.nvl(uuid, "");
            String captcha = redisCache.getCacheObject(verifyKey);
            if (captcha == null)
            {
                AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, MessageUtils.message("user.jcaptcha.expire")));
                throw new CaptchaExpireException();
            }
            redisCache.deleteObject(verifyKey);
            if (!code.equalsIgnoreCase(captcha))
            {
                AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, MessageUtils.message("user.jcaptcha.error")));
                throw new CaptchaException();
            }
        }
    }

    /**
     * 登录前置校验
     * @param username 用户名
     * @param password 用户密码
     */
    public void loginPreCheck(String username, String password)
    {
        // 用户名或密码为空 错误
        if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password))
        {
            AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, MessageUtils.message("not.null")));
            throw new UserNotExistsException();
        }
        // 密码如果不在指定范围内 错误
        if (password.length() < UserConstants.PASSWORD_MIN_LENGTH
                || password.length() > UserConstants.PASSWORD_MAX_LENGTH)
        {
            AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, MessageUtils.message("user.password.not.match")));
            throw new UserPasswordNotMatchException();
        }
        // 用户名不在指定范围内 错误
        if (username.length() < UserConstants.USERNAME_MIN_LENGTH
                || username.length() > UserConstants.USERNAME_MAX_LENGTH)
        {
            AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, MessageUtils.message("user.password.not.match")));
            throw new UserPasswordNotMatchException();
        }
        // IP黑名单校验
        String blackStr = configService.selectConfigByKey("sys.login.blackIPList");
        if (IpUtils.isMatchedIp(blackStr, IpUtils.getIpAddr()))
        {
            AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, MessageUtils.message("login.blocked")));
            throw new BlackListException();
        }
    }

    /**
     * 记录登录信息
     *
     * @param userId 用户ID
     */
    public void recordLoginInfo(Long userId) {
        SysUser sysUser = new SysUser();
        sysUser.setUserId(userId);
        sysUser.setLoginIp(IpUtils.getIpAddr(ServletUtils.getRequest()));
        sysUser.setLoginDate(DateUtils.getNowDate());
        userService.updateUserProfile(sysUser);
    }
    /**
     * 记录登录信息
     *
     * @param userId 用户ID
     */
    public void recordLoginInfo(Long userId, String dingTaskUserId, String dingTaskUnionId) {
        SysUser sysUser = new SysUser();
        sysUser.setUserId(userId);
        sysUser.setLoginIp(IpUtils.getIpAddr(ServletUtils.getRequest()));
        sysUser.setLoginDate(DateUtils.getNowDate());
        sysUser.setDingtaskUserId(dingTaskUserId);
        sysUser.setDingtaskUnionId(dingTaskUnionId);
        userService.updateUserProfile(sysUser);
    }

    /**
     * 根据userName获取用户
     *
     * @param userName 钉钉注册进来的可以是手机号也可以是工号
     * @return 用户
     */
    public UserDetails loadUserByUserName(String userName) {
        {
            SysUser user = userService.selectUserByUserName(userName);
            if (StringUtils.isNull(user)) {
                throw new ServiceException("登录用户不存在");
            }

//            放入部门及sap信息
            Long deptId = user.getDeptId();
            if (!ObjectUtils.isEmpty(deptId)) {
                SysDept sysDept = deptMapper.selectDeptById(user.getDeptId());
                //                重新存储dept
                user.setDept(sysDept);
                if (!ObjectUtils.isEmpty(sysDept) && StringUtils.isNotEmpty(sysDept.getSapName())){
                    user.setSapCode(sysDept.getSapName());
                }
            }

            return createLoginUser(user);
        }

    }


    public UserDetails createLoginUser(SysUser user) {
        return new LoginUser(user.getUserId(), user.getDeptId(), user,
                permissionService.getMenuPermission(user));
    }
}
