package com.gmlimsqi.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum RoleEnum {
    
    supplier(100L, "供应商", "supplier"),
    gate(101, "门岗人员", "gate"),
    quality(102L, "质检人员", "quality"),
    warehouse(103L, "仓库人员", "warehouse"),
    process(104L, "加工工厂管理员", "process"),
    system(105L, "系统管理员", "system"),
    pound(106L, "磅房", "pound"),
    ;
    
    long code;
    String message;
    String name;
    
}
