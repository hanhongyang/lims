package com.gmlimsqi.framework.security;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.util.Assert;

import java.util.Collection;

/**
 * 用户身份验证令牌
 *
 * @author Administrator
 * @date 2022/03/15
 */
public class CpAuthenticationToken extends AbstractAuthenticationToken {
    private final Object principal;
    
    public CpAuthenticationToken(Object principal) {
        super((Collection) null);
        this.principal = principal;
        this.setAuthenticated(false);
    }
    
    public CpAuthenticationToken(Object principal, Collection<?
            extends GrantedAuthority> authorities) {
        super(authorities);
        this.principal = principal;
        super.setAuthenticated(true);
    }
    
    @Override
    public Object getCredentials() {
        return null;
    }
    
    @Override
    public Object getPrincipal() {
        return this.principal;
    }
    
    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        Assert.isTrue(!isAuthenticated, "Cannot set this token to " +
                "trusted - use constructor which takes a " +
                "GrantedAuthority list instead");
        super.setAuthenticated(false);
    }
    
    @Override
    public void eraseCredentials() {
        super.eraseCredentials();
    }
}
