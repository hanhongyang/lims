package com.gmlimsqi.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum JczxBloodResultStatusEnum {

    KSHY("1", "开始化验"),
    HYWC("2", "化验完成"),
    SHWC("3", "审核完成"),
    FS("4", "作废")
    ;
    String code;
    String message;
}
