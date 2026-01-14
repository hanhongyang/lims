package com.gmlimsqi.business.samplingplan.pojo.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@NoArgsConstructor // 关键：添加无参构造函数（Jackson 必需）
@AllArgsConstructor // 可选：保留全参构造，方便创建对象
public class ImportResultVO {
        private boolean success;
        private String message;
        private int successMainCount; // 成功主表数
        private int successSubCount; // 成功子表数
        private List<String> failMsgList;

        /*public ImportResultVO(boolean success, String message, int successMainCount, int successSubCount, List<String> failMsgList) {
            this.success = success;
            this.message = message;
            this.successMainCount = successMainCount;
            this.successSubCount = successSubCount;
            this.failMsgList = failMsgList;
        }*/
    }