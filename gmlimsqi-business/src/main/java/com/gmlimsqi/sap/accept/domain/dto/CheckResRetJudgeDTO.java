package com.gmlimsqi.sap.accept.domain.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

/**
 * 检验批结果回传&判定DTO
 */
@Data
public class CheckResRetJudgeDTO {

    /** 工厂编号 必填 */
    @JsonProperty("WERK")
    private String WERK;

     /** 类型 必填 */
    @JsonProperty("ZTYPE")
    private String ZTYPE;

    /** 检验批次 必填 */
    @JsonProperty("PRUEFLOS")
    private String PRUEFLOS;

     /** 判定结果 必填 */
    @JsonProperty("VCODE")
    private String VCODE;

    /** 质检单据编号 必填 */
    @JsonProperty("ZLJYNUM")
    private String ZLJYNUM;

    /** 检验类型 */
    @JsonProperty("HERKUNFT")
    private String HERKUNFT;

    /** 物料凭证 */
    @JsonProperty("MBLNR")
    private String MBLNR;

    /** 物料凭证行项目 */
    @JsonProperty("ZEILE")
    private String ZEILE;

    /** 判定结果描述 */
    @JsonProperty("KURZTEXT")
    private String KURZTEXT;

    /** H备用1 */
    @JsonProperty("ZHTEXT1")
    private String ZHTEXT1;

    /** H备用2 */
    @JsonProperty("ZHTEXT2")
    private String ZHTEXT2;

    /** H备用3 */
    @JsonProperty("ZHTEXT3")
    private String ZHTEXT3;

    /** H备用4 */
    @JsonProperty("ZHTEXT4")
    private String ZHTEXT4;

    /** H备用5 */
    @JsonProperty("ZHTEXT5")
    private String ZHTEXT5;

    /** 子表 */
    @JsonProperty("ITEMS")
    private List<CheckResRetJudgeItemDTO> ITEMS;

}
