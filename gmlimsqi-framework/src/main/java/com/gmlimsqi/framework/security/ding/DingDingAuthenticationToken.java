package com.gmlimsqi.framework.security.ding;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.SpringSecurityCoreVersion;

import java.util.Collection;

public class DingDingAuthenticationToken extends AbstractAuthenticationToken {
    
    private static final long serialVersionUID =
            SpringSecurityCoreVersion.SERIAL_VERSION_UID;
    
    // username
    private final Object principal;
    
    /**
     * 此构造函数用来初始化未授信凭据.
     *
     * @param principal
     */
    public DingDingAuthenticationToken(Object principal) {
        super(null);
        System.out.println("DingDingAuthenticationToken1" + principal.toString());
        this.principal = principal;
        setAuthenticated(false);
    }
    
    /**
     * 此构造函数用来初始化授信凭据.
     *
     * @param principal
     */
    public DingDingAuthenticationToken(Object principal, Collection<?
            extends GrantedAuthority> authorities) {
        super(authorities);
        System.out.println("DingDingAuthenticationToken2" + principal.toString());
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
        if (isAuthenticated) {
            throw new IllegalArgumentException(
                    "Cannot set this token to trusted - use constructor " +
                            "which takes a GrantedAuthority list instead");
        }
        super.setAuthenticated(false);
    }
    
    @Override
    public void eraseCredentials() {
        super.eraseCredentials();
    }
}
