package com.gmlimsqi.common.annotation;

import com.gmlimsqi.common.enums.BusinessType;
import com.gmlimsqi.common.enums.FunTypeEnums;

import java.lang.annotation.*;

/**
 * 青贮种植系统功能注解
 * 
 * @author Lzy
 *
 */
@Target({ ElementType.PARAMETER, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface FunLog
{
    /**
     * 模块 
     */
    public String title() default "";
    
    /**
     * 系统名称
     */
    public FunTypeEnums systemName() default FunTypeEnums.NULL;
    
    /**
     * 功能描述
     */
    public String description() default "";

    /**
     * 功能
     */
    public BusinessType businessType() default BusinessType.OTHER;

    /**
     * 是否保存请求的参数
     */
    public boolean isSaveRequestData() default true;

    /**
     * 是否保存响应的参数
     */
    public boolean isSaveResponseData() default true;
    
    /**
     * 登录
     */
    public boolean register() default true;
    
}
