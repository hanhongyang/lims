package com.gmlimsqi.business.basicdata.domain;

import lombok.Data;
import com.gmlimsqi.common.annotation.Excel;
import com.gmlimsqi.common.core.domain.BaseEntity;

/**
 * 奶仓档案对象 bs_milk_warehouse
 * 
 * @author hhy
 * @date 2025-11-05
 */
@Data
public class BsMilkWarehouse extends BaseEntity implements BaseEntity.CreateAware, BaseEntity.UpdateAware
{
    private static final long serialVersionUID = 1L;

    /** 奶仓信息表主键 */
    private String id;

    /** 奶仓状态 */
    @Excel(name = "奶仓状态")
    private String warehouseStatus;

    /** 奶仓名称 */
    @Excel(name = "奶仓名称")
    private String warehouseName;

    /** 奶仓地址 */
    @Excel(name = "奶仓地址")
    private String warehouseAddress;

    /** 奶仓编号 */
    @Excel(name = "奶仓编号")
    private String warehouseNumber;

    /** 部门id */
    @Excel(name = "部门id")
    private String deptId;

     /** 部门名称 */
    @Excel(name = "部门名称")
    private String deptName;

}
