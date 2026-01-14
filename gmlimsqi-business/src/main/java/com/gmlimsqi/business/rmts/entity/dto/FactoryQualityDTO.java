package com.gmlimsqi.business.rmts.entity.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import java.util.Date;

/**
 * 工厂质检信息同步DTO
 */
@Data
public class FactoryQualityDTO {

    /** 计划日期（必填）*/
    @NotBlank(message = "请输入计划日期")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date schedulingDate;

    /** 牧场编号（选填）*/
    @NotBlank(message = "请输入牧场编号")
    private String pastureCode;

}
