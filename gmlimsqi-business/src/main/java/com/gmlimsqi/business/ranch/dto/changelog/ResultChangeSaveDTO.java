package com.gmlimsqi.business.ranch.dto.changelog;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 样品化验结果变更
 */
@Data
public class ResultChangeSaveDTO {

    @NotNull(message = "检测项目ID不能为空")
    private String resultId; // 检测项目ID

    @NotNull(message = "检测结果不能为空")
    private String result; // 检测结果

    @NotNull(message = "审核结果不能为空")
    private String checkResult; // 审核结果

    private String changeReason; // 变更原因

}
