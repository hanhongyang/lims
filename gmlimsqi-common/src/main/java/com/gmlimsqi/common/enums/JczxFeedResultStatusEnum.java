package com.gmlimsqi.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum JczxFeedResultStatusEnum {
    /**
     * 0否1是
     */
    KSHY("1", "开始化验"),
    HYWC("2", "化验完成"),
    JDWC("3", "校对完成"),
    SHWC("4", "审核完成"),
    ZF("5", "作废"),
    TH("6", "退回")
    ;
    String code;
    String message;
}
