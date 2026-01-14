package com.gmlimsqi.business.milksamplequalityinspection.domain;

import lombok.Data;

import java.util.Date;

/**
 * 出场检测报表查询参数
 */
@Data
public class ExitInspectionReportDTO {

    /** 部门id */
    private String deptId;

    /** 测试开始时间 */
    private Date testStartTime;

     /** 测试结束时间 */
    private Date testEndTime;

}
