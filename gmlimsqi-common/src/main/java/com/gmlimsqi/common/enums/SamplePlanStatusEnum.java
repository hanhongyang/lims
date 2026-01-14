package com.gmlimsqi.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum SamplePlanStatusEnum {
    /**
     *  0未取样  1已取样 2无需取样
     */
    WSAPLE("0", "未取样"),
    YSAPLE("1", "已取样"),
    NSAPLE("2", "无需取样")
    ;
    String code;
    String message;
}
