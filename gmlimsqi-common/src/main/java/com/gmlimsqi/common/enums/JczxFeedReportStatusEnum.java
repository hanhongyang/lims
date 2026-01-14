package com.gmlimsqi.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum JczxFeedReportStatusEnum {
    DZZ("0", "待制作"),
    ZZZ("1", "制作中"),
    DSH("2", "待审核"),
    YSH("3", "已审核"),
    YPZ("4", "已批准"),
    YFS("5", "已发送"),
    ZF("6", "作废")
    ;
    String code;
    String message;
}
