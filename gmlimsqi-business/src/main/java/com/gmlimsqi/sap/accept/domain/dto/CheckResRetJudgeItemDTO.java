package com.gmlimsqi.sap.accept.domain.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * 检验批结果回传&判定项子表DTO
 */
@Data
public class CheckResRetJudgeItemDTO {

    /** 行项 */
    @JsonProperty("ITEM")
    private String ITEM;

    /** 特性*/
    @JsonProperty("VERWMERKM")
    private String VERWMERKM;

    /** 特性描述 */
     @JsonProperty("KURZTEXT")
    private String KURZTEXT;

    /** 检验结果*/
    @JsonProperty("ORIGINAL_INPUT")
    private String ORIGINAL_INPUT;

    /** I备用1 */
    @JsonProperty("ZITEXT1")
    private String ZITEXT1;

    /** I备用2 */
    @JsonProperty("ZITEXT2")
    private String ZITEXT2;

    /** I备用3 */
    @JsonProperty("ZITEXT3")
    private String ZITEXT3;

    /** I备用4 */
    @JsonProperty("ZITEXT4")
    private String ZITEXT4;

     /** I备用5 */
    @JsonProperty("ZITEXT5")
    private String ZITEXT5;

}
