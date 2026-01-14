package com.gmlimsqi.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum JczxPcrReportStatusEnum {
    DZZ("0", "待制作"),
    ZZZ("1", "待提交"),
    DSH("2", "待审核"),
    YSH("3", "待批准"),
    YPZ("4", "待发送"),
    YFS("5", "已发送"),
    ZF("6", "作废")
    ;
    String code;
    String message;
}
