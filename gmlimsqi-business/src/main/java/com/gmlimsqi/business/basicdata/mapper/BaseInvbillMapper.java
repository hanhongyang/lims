package com.gmlimsqi.business.basicdata.mapper;

import com.gmlimsqi.business.basicdata.domain.BaseInvbill;
import com.gmlimsqi.business.basicdata.vo.BusinessInvbillVo;
import com.gmlimsqi.common.annotation.DataSource;
import com.gmlimsqi.common.enums.DataSourceType;

import java.util.List;

public interface BaseInvbillMapper {
    
    List<BaseInvbill> selectInvbillList(BaseInvbill baseInvbill);
    
    @DataSource(value = DataSourceType.SLAVE)
    List<BusinessInvbillVo> selectBusinessInvbillList();


    /**
     * 查询物料档案列表（查询SAP系统中的）
     */
    List<BaseInvbill> selectInvbillListSap(BaseInvbill baseInvbill);
}
