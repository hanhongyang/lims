package com.gmlimsqi.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum UserTypeEnum {
    SYSTEM("1", "供应商"),
    PROCESSING_PLANT("00", "内部人员"),
    ;
    String code;
    String message;
}
