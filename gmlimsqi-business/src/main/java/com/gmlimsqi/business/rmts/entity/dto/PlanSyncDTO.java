package com.gmlimsqi.business.rmts.entity.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 计划同步DTO
 */
@Data
public class PlanSyncDTO {

    /** 计划日期 格式为 yyyy-MM-dd，只能传当天及以后的日期 */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date schedulingDate;

    /** 牧场编号 */
    private String pastureCode;

}
