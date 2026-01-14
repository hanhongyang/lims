package com.gmlimsqi.sap.accept.domain.sapresponse;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.gmlimsqi.sap.accept.domain.vo.InOutInfoVO;
import lombok.Data;
import java.util.List;

/**
 * SAP返回的出入库信息外层响应VO
 */
@Data
public class InOutInfoResponse {
    // 对应JSON中的DATA数组，类型为InOutInfoVO的列表
    @JsonProperty("DATA")
    private List<InOutInfoVO> data;
}