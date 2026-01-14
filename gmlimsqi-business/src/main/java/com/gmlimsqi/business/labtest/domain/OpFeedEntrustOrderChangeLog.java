package com.gmlimsqi.business.labtest.domain;

import com.gmlimsqi.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 委托单变更记录对象 op_feed_entrust_order_change_log
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class OpFeedEntrustOrderChangeLog extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** 日志主键 */
    private String logId;

    /** 业务ID (委托单ID 或 新生成的样品ID) */
    private String businessId;

    /** 业务类型 (ORDER:订单, SAMPLE:样品) */
    private String businessType;

    /** 字段键值 (如 materialName) */
    private String fieldKey;

    /** 字段名称 (如 物料名称) */
    private String fieldName;

    /** 旧值 */
    private String oldValue;

    /** 新值 */
    private String newValue;
}