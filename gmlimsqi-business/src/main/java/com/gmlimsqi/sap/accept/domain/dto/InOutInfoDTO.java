package com.gmlimsqi.sap.accept.domain.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * 出入库信息DTO
 */
@Data
public class InOutInfoDTO {

    /** 工厂编号 */
    @JsonProperty("WERKS")
    private String WERKS;

    /** 日期 */
    @JsonProperty("BUDAT")
    private String BUDAT;

    /** 批次 */
    @JsonProperty("CHARG")
    private String CHARG;

    /** 类型 01：入库 02：出库 */
    @JsonProperty("ZLEIX")
    private String ZLEIX;

}
