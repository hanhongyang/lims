package com.gmlimsqi.business.labtest.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 退回DTO
 */
@Data
public class ReturnDTO {

    /** 委托单id */
    private String opEntrustOrderId;

    /** 送检单号 */
    private String entrustOrderNo;
    //1饲料 2疾病 3pcr 4早产 5生化
    private String type;

    /**
     * 退回原因
     */
    private String returnReason;

    /**
     * 退回时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date returnDate;

    /**
     * 是否退回
     */
     private String isReturn;

}
