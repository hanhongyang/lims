package com.gmlimsqi.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum YesNoEnum {
    YES("Y", "是"),
    NO("N", "否");
    
    String code;
    String message;
}
