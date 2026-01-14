package com.gmlimsqi.business.ranch.dto.changelog;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Data
public class ResultChangeLogQueryDTO {

    /**
     * 检测项目变更ID
     */
    private String resultId;

    /**
     * 原检测结果
     */
    private String originResult;

    /**
     * 原审核结果
     */
    private String originCheckResult;

    /**
     * 新检测结果
     */
    private String newResult;

    /**
     * 新审核结果
     */
    private String newCheckResult;

    /**
     * 创建人
     */
    private String createBy;

    /**
     * 创建时间开始
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTimeStart;

    /**
     * 创建时间结束
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTimeEnd;
}
