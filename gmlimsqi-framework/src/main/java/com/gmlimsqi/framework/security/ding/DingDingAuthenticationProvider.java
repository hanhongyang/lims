package com.gmlimsqi.framework.security.ding;

import com.gmlimsqi.framework.web.service.SysLoginService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Objects;

/**
 * 钉钉登录
 */
@Component
@Slf4j
public class DingDingAuthenticationProvider implements AuthenticationProvider {
    
    /** 钉钉登录验证服务 */
    @Autowired
    private SysLoginService sysLoginService;
    
    /**
     * 进行认证
     *
     * @param authentication
     * @return
     * @throws AuthenticationException
     */
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        long time = System.currentTimeMillis();
        log.info(this.getClass().getName() +
                "===========================钉钉登录验证");
        
        String userName = authentication.getName();
        // String rawCode = authentication.getCredentials().toString();
        
        // 1.根据手机号获取用户信息
        UserDetails userDetails =
                sysLoginService.loadUserByUserName(userName);
        if (Objects.isNull(userDetails)) {
            throw new BadCredentialsException("钉钉当前用户未关联到系统用户");
        }
        // 3、返回经过认证的Authentication
        DingDingAuthenticationToken result =
                new DingDingAuthenticationToken(userDetails,
                        Collections.emptyList());
        result.setDetails(authentication.getDetails());
        log.info("钉钉登录验证完成");
        return result;
    }
    
    @Override
    public boolean supports(Class<?> authentication) {
        boolean res =
                DingDingAuthenticationToken.class.isAssignableFrom(authentication);
        log.info("钉钉进行登录验证 res:" + res);
        return res;
    }
}
