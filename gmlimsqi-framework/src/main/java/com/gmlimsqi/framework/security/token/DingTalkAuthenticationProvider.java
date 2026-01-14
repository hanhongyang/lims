package com.gmlimsqi.framework.security.token;

import cn.hutool.core.util.ObjectUtil;
import com.gmlimsqi.common.core.domain.entity.SysDept;
import com.gmlimsqi.common.core.domain.entity.SysUser;
import com.gmlimsqi.common.core.domain.model.LoginUser;
import com.gmlimsqi.common.enums.UserStatus;
import com.gmlimsqi.common.utils.StringUtils;
import com.gmlimsqi.framework.security.CpAuthenticationToken;
import com.gmlimsqi.framework.web.service.SysPermissionService;
import com.gmlimsqi.system.mapper.SysDeptMapper;
import com.gmlimsqi.system.service.ISysUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

/**
 * 经理身份验证提供者
 *
 * @author Administrator
 * @date 2022/03/15
 */
@Component
@Slf4j
public class DingTalkAuthenticationProvider implements AuthenticationProvider {
    
    @Autowired
    private ISysUserService userService;
    @Autowired
    private SysPermissionService permissionService;
    @Autowired
    private SysDeptMapper deptMapper;
    
    private UserDetailsService userDetailsService;

    public DingTalkAuthenticationProvider(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        DingTalkAuthenticationToken authenticationToken =
                (DingTalkAuthenticationToken) authentication;
        String username = (String) authenticationToken.getPrincipal();
        //        根据当前手机号查询用户信息
//        SysUser user = userService.selectUserByUserName(username);
        if (ObjectUtil.isNull(username)) {
            throw new UsernameNotFoundException(username + "您当前未开通该系统帐号，无法登录系统请联系管理人员!");
        }
//        String newUsername = new String(Base64.getEncoder().encode(username.getBytes()));
        SysUser user = userService.selectUserByPhonenumber(username);
        
        if (StringUtils.isNull(user)) {
            log.info("登录用户：{} 不存在.", username);
            throw new UsernameNotFoundException("登录用户：" + username + " 不存在,或密码错误！");
        } else if (UserStatus.DELETED.getCode().equals(user.getDelFlag())) {
            log.info("登录用户：{} 已被删除.", username);
//            throw new BaseException("对不起，您的账号：" + username + " 已被删除");
            throw new UsernameNotFoundException("登录用户：" + username + " 不存在,或密码错误！");
        } else if (UserStatus.DISABLE.getCode().equals(user.getStatus())) {
            log.info("登录用户：{} 已被停用.", username);
//            throw new BaseException("对不起，您的账号：" + username + " 已停用");
            throw new UsernameNotFoundException("登录用户：" + username + " 不存在,或密码错误！");
        }
        
        Long deptId = user.getDeptId();
        if (!ObjectUtils.isEmpty(deptId)) {
            SysDept sysDept = deptMapper.selectDeptById(user.getDeptId());
            //                重新存储dept
            user.setDept(sysDept);
            if (!ObjectUtils.isEmpty(sysDept) && StringUtils.isNotEmpty(sysDept.getSapName())){
                user.setSapCode(sysDept.getSapName());
            }
        }
        
        UserDetails loginUser = createLoginUser(user);
        
        CpAuthenticationToken authenticationResult =
                new CpAuthenticationToken(loginUser,
                        loginUser.getAuthorities());
        authenticationResult.setDetails(authenticationToken.getDetails());
        return authenticationResult;
    }

    /**
     * 判断只有传入ManagerAuthenticationToken的时候才使用这个Provider
     * supports会在AuthenticationManager层被调用
     * @param authentication
     * @return
     */
    @Override
    public boolean supports(Class<?> authentication) {
        return DingTalkAuthenticationToken.class.isAssignableFrom(authentication);
    }

    public UserDetailsService getUserDetailsService() {
        return userDetailsService;
    }

    public void setUserDetailsService(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }
    
    public UserDetails createLoginUser(SysUser user) {
        return new LoginUser(user.getUserId(), user.getDeptId(), user,
                permissionService.getMenuPermission(user));
    }
    
}
