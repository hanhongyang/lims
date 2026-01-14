package com.gmlimsqi.business.rmts.entity.dto;

import lombok.Data;

/**
 * 车辆信息接口请求DTO
 */
@Data
public class CarInfoSyncDTO {

    /** 订单号 必填 */
    private String orderNumber;

}
