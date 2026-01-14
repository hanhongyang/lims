package com.gmlimsqi.sap.accept.domain.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * QM101冻精质检数据查询请求参数VO
 */
@Data
public class FrozenSemenQcQueryDTO {
    /** 工厂（必填，技术名称WERK） */
    @JsonProperty("WERK")
    private String WERK;

    /** 检验批次（可选，技术名称PRUEFLOS） */
    @JsonProperty("PRUEFLOS")
    private String PRUEFLOS;

    /** 检验类型（可选，技术名称HERKUNFT） */
    @JsonProperty("HERKUNFT")
    private String HERKUNFT;

    /** 物料编码（可选，技术名称SELMATNR） */
    @JsonProperty("SELMATNR")
    private String SELMATNR;

    /** 物料描述（可选，技术名称KTEXTMAT） */
    @JsonProperty("KTEXTMAT")
    private String KTEXTMAT;

    /** 到厂日期（可选，格式YYYYMMDD，技术名称ENSTEHDAT，支持区间） */
    @JsonProperty("ENSTEHDAT")
    private String ENSTEHDAT;

    /** 决策日期（可选，格式YYYYMMDD，技术名称BUDAT） */
    @JsonProperty("BUDAT")
    private String BUDAT;



}