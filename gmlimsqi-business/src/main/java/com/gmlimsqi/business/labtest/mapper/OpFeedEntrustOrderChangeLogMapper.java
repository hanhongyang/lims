package com.gmlimsqi.business.labtest.mapper;

import com.gmlimsqi.business.labtest.domain.OpFeedEntrustOrderChangeLog;
import java.util.List;

public interface OpFeedEntrustOrderChangeLogMapper {
    /**
     * 批量新增变更记录
     */
    int insertBatch(List<OpFeedEntrustOrderChangeLog> list);

    /**
     * 根据业务ID列表查询变更记录
     */
    List<OpFeedEntrustOrderChangeLog> selectLogsByBusinessIds(List<String> businessIds);
    
    /**
     * 根据委托单ID查询所有相关日志（包括样品）
     * 这里的逻辑可能需要关联查询，或者简单点前端传ID列表查
     */
    List<OpFeedEntrustOrderChangeLog> selectLogsByOrderId(String orderId);
}