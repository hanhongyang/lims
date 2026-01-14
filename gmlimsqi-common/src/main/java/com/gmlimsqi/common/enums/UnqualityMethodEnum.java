package com.gmlimsqi.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum UnqualityMethodEnum {
    /**
     *  0扣重  1扣款 2退货
     */
    kZ("0", "扣重"),
    KK("1", "扣款"),
    TH("2", "退货")
    ;
    String code;
    String message;
}
