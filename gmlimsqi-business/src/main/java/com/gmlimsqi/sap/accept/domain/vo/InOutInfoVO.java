package com.gmlimsqi.sap.accept.domain.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * 出入库信息VO
 */
@Data
public class InOutInfoVO {

    /** 物料凭证 */
    @JsonProperty("MBLNR")
    private String MBLNR;

    /** 日期 */
    @JsonProperty("BUDAT")
    private String BUDAT;

    /** 工厂编号 */
    @JsonProperty("WERKS")
    private String WERKS;

    /** 工厂描述 */
    @JsonProperty("WERKS_NAM1")
    private String WERKS_NAM1;

    /** 物料编号 */
    @JsonProperty("MATNR")
    private String MATNR;

    /** 物料描述 */
    @JsonProperty("MAKTX")
    private String MAKTX;

    /** 批次 */
    @JsonProperty("CHARG")
    private String CHARG;

    /** 移动类型 */
    @JsonProperty("BWART")
    private String BWART;

    /** 移动类型描述 */
    @JsonProperty("BTEXT")
    private String BTEXT;

    /** 库存地点 */
    @JsonProperty("LGORT")
    private String LGORT;

     /** 库存地点描述 */
    @JsonProperty("LGOBE")
    private String LGOBE;

    /** 数量 */
    @JsonProperty("MENGE")
    private String MENGE;

    /** 操作人 */
    @JsonProperty("USNAM")
    private String USNAM;
}
