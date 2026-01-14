package com.gmlimsqi.sap.accept.domain.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * QM102入库检验批查询请求参数VO
 */
@Data
public class InboundInspectionDTO {

    /** 工厂（必填，技术名称WERK） */
    @JsonProperty("WERK")
    private String WERK;

    /** 工厂描述（可选，技术名称WERKS） */
    @JsonProperty("WERK_NAME1")
    private String WERK_NAME1;

    /** 检验批次（必填，技术名称PRUEFLOS） */
    @JsonProperty("PRUEFLOS")
    private String PRUEFLOS;

    /** 检验开始日期（可选，格式YYYYMMDD，技术名称PASTRTERM） */
    @JsonProperty("PASTRTERM")
    private String PASTRTERM;

    /** 检验类型（可选，技术名称HERKUNFT） */
    @JsonProperty("HERKUNFT")
    private String HERKUNFT;

    /** 物料凭证（可选，技术名称MBLNR） */
    @JsonProperty("MBLNR")
    private String MBLNR;

    /** 生产订单号（可选，技术名称AUFNR） */
    @JsonProperty("AUFNR")
    private String AUFNR;

}