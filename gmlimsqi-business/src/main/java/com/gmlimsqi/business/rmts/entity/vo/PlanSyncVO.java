package com.gmlimsqi.business.rmts.entity.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 计划同步VO
 */
@Data
public class PlanSyncVO {

    /**
     * 计划日期，格式为yyyy-MM-dd
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date schedulingDate;

    /**
     * 订单编号，唯一键
     */
    private String orderNumber;

    /**
     * 车次号
     */
    private Integer carIndex;

    /**
     * 牧场编号
     */
    private String pastureCode;

    /**
     * 牧场简称
     */
    private String pastureName;

    /**
     * 工厂编号
     */
    private String factoryCode;

    /**
     * 工厂简称
     */
    private String factoryName;

    /**
     * 物料编号
     */
    private String itemsCode;

    /**
     * 物料简称
     */
    private String itemsName;

    /**
     * 计划奶量，单位：吨
     */
    private Double milkQuantityPlan;

}
