package com.gmlimsqi.business.labtest.dto;

import lombok.Data;

/**
 * 检测中心血样报告发送邮件DTO
 */
@Data
public class BloodEmailDTO {


    /** 委托单id */
    private String bloodEntrustOrderId;

    /** 报告id */
    private String opJczxBloodReportBaseId;

    private String emails;

    /** 邮件标题 */
    private String title;

    /** 邮件内容 */
    private String content;

}
