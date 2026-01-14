package com.gmlimsqi.business.labtest.dto;

import lombok.Data;

/**
 * 邮件DTO
 */
@Data
public class EmailDTO {

    /**
     * 检测中心饲料报告主表id
     */
    private String opJczxFeedReportBaseId;

    /**
     * 饲料委托订单样本ID
     */
    private String feedEntrustOrderSampleId;
    /**
     * 饲料委托单ID
     */
    private String feedEntrustOrderId;
    private String emails;

     /**
      * 附件
      */
//    private MultipartFile file;

}
