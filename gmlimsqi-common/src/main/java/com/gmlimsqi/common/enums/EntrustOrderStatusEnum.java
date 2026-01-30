package com.gmlimsqi.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum EntrustOrderStatusEnum {

    DTJ("0", "待提交"),
    DSL("1", "待受理"),
    JCZ("2", "检测中"),
    JCWC("3", "检测完成"),
    YSH("4", "已审核"),
    YFS("5", "已发送"),
    YBH("6", "已驳回"),
    ZF("7", "作废"),
    ;
    String code;
    String message;
}
