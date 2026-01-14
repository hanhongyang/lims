package com.gmlimsqi.business.basicdata.vo;

import com.gmlimsqi.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 物料档案对象 business_invbill
 *
 * @author egap
 * @date 2024-12-16
 */
@Data
@Accessors(chain = true)
public class BusinessInvbillVo extends BaseEntity {
    private static final long serialVersionUID = 1L;
    
    private String id;
    
    private String bsysdel;
    
    private String cclasscode;
    
    private String cclassname;
    
    private String cinvcode;
    
    private String cinvname;
    
    private String cremark;
    
    private String ikz;
    /** 部门sap编码 */
    private String deptId;

    private String isSapType;
    /** 部门id */
    //private String ownDeptId;
}
