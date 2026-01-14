package com.gmlimsqi.sap.accept.domain.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * 检验批结果回传&判定VO（对应接口响应/请求结构）
 */
@Data
public class CheckResRetJudgeVO {

    // 1. 修正：成员变量类型改为内部类 RETURN，而非 String
    // 2. @JsonProperty 确保与 JSON 中的 "RETURN" 字段映射
    @JsonProperty("RETURN")
    private RETURN returnInfo; // 变量名避免与内部类名重复，增强可读性

    // 3. 关键：内部类加 public 修饰，允许外部包访问
    @Data
    public static class RETURN {
        /** 消息类型（如 "S"=成功、"E"=错误，需结合接口文档定义） */
        @JsonProperty("TYPE")
        private String type;

        /** 回传/判定结果消息（如成功提示、错误原因） */
        @JsonProperty("MESSAGE")
        private String message;
    }

}