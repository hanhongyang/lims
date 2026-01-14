package com.gmlimsqi.business.basicdata.domain;

import com.gmlimsqi.common.annotation.Excel;
import com.gmlimsqi.common.core.domain.BaseEntity;
import lombok.Data;

/**
 * 物料子对象 business_invbill_info
 * 
 * @author egap
 * @date 2025-01-13
 */
@Data
public class BusinessInvbillInfo extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 物料信息子表 */
    private String id;

    /** 物料信息主表id */
    @Excel(name = "物料信息主表id")
    private String invbillId;

    /** 牧场id */
    @Excel(name = "牧场id")
    private String deptId;

    /** 牧场名称 */
    @Excel(name = "牧场名称")
    private String deptName;

    /** 是否跳过质检  0否 1是*/
    @Excel(name = "是否跳过质检  0否 1是")
    private String izj;

    /** 牧场sap编码 */
    private String sapName;

}
