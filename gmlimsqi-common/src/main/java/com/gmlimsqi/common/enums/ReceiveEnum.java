package com.gmlimsqi.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ReceiveEnum {
    /**
     * 0否1是
     */
    UnReceive("0", "不接收"),
    Receive("1", "接收"),
    ;
    String code;
    String message;
}
