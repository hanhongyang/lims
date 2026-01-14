package com.gmlimsqi.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum YesNo2Enum {
    YES("1", "是"),
    NO("0", "否");
    
    String code;
    String message;
}
