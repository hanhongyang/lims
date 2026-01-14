package com.gmlimsqi.business.basicdata.service;

import com.gmlimsqi.business.basicdata.domain.BaseInvbill;
import com.gmlimsqi.business.basicdata.domain.BusinessInvbill;

import java.util.List;

public interface IBaseInvbillService {
    
    List<BaseInvbill> selectInvbillList(BaseInvbill baseInvbill);
    
    void syncBaseInvbill();
    
    int updateInvbill(BusinessInvbill baseInvbill);

    /**
     * 根据id查询物资详情
     * @param id
     * @return
     */
    BusinessInvbill selectInvbillById(String id);

    /**
     * 查询物料档案列表（查询SAP系统中的）
     */
    List<BaseInvbill> selectInvbillListSap(BaseInvbill baseInvbill);
}
