package com.gmlimsqi.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum FeedReportEnum {
    Make("1", "制作中"),
    Save("2", "制作完成"),
    Veriy("3", "已审核"),
    Commit("4", "已批准"),
    Send("5", "已发送"),
    Nullify("6", "作废");
    String code;
    String message;
}
