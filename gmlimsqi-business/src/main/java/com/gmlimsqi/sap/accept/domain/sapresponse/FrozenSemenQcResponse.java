package com.gmlimsqi.sap.accept.domain.sapresponse;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.gmlimsqi.sap.accept.domain.vo.FrozenSemenQcDetailVO;
import lombok.Data;
import java.util.List;

/**
 * QM101冻精质检数据查询响应外层VO
 */
@Data
public class FrozenSemenQcResponse {
    // 对应返回报文中的DATA数组
    @JsonProperty("DATA")
    private List<FrozenSemenQcDetailVO> data;
}