package com.gmlimsqi.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum EntrustOrderTypeEnum {
    /**
     * 0否1是
     */
    FEED("1", "饲料"),
    BLOOD("2", "血样"),
    PCR("3", "PCR")
    ;
    String code;
    String message;
}
