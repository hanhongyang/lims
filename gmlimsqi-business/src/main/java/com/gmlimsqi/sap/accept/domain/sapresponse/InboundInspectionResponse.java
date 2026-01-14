package com.gmlimsqi.sap.accept.domain.sapresponse;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.gmlimsqi.sap.accept.domain.vo.InboundInspectionDetailVO;
import lombok.Data;
import java.util.List;

/**
 * QM102入库检验批查询响应外层VO
 */
@Data
public class InboundInspectionResponse {
    // 对应返回报文中的DATA数组
    @JsonProperty("DATA")
    private List<InboundInspectionDetailVO> data;
}