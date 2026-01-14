package com.gmlimsqi.business.milkfillingorder.domain;

import lombok.Data;
import com.gmlimsqi.common.annotation.Excel;
import com.gmlimsqi.common.core.domain.BaseEntity;

/**
 * 装奶单子对象 op_milk_filling_order_detail
 * 
 * @author hhy
 * @date 2025-11-13
 */
@Data
public class OpMilkFillingOrderDetail extends BaseEntity implements BaseEntity.CreateAware, BaseEntity.UpdateAware
{
    private static final long serialVersionUID = 1L;

    /** 装奶单子表主键 */
    private String opMilkFillingOrderDetailId;

    /** 装奶单主键 */
    @Excel(name = "装奶单主键")
    private String opMilkFillingOrderId;

    /** 奶仓id */
    @Excel(name = "奶仓id")
    private String milkWarehouseId;

    /** 奶仓名称 */
    @Excel(name = "奶仓名称")
    private String milkWarehouseName;

    /** 仓口号 */
    @Excel(name = "仓口号")
    private String milkWarehouseNumber;

    /** 装奶量 */
    @Excel(name = "装奶量")
    private String milkCapacity;

}
