package com.gmlimsqi.business.basicdata.domain;

import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.gmlimsqi.common.annotation.Excel;
import com.gmlimsqi.common.core.domain.BaseEntity;

/**
 * 物料使用部门对象 bs_invbill_dept
 * 
 * @author hhy
 * @date 2025-09-23
 */
@Data
public class BsInvbillDept extends BaseEntity implements BaseEntity.CreateAware, BaseEntity.UpdateAware
{
    private static final long serialVersionUID = 1L;

    /** id */
    private String bsInvbillDeptId;

    /** 物料sap编码 */
    private String invbillSapCode;

    /** 机构sap编码 */
    private String deptSapCode;
    private String deptName;
    /** 是否删除 */
    private String isDelete;

}
