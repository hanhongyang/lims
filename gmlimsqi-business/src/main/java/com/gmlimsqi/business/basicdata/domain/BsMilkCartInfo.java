package com.gmlimsqi.business.basicdata.domain;

import lombok.Data;
import com.gmlimsqi.common.annotation.Excel;
import com.gmlimsqi.common.core.domain.BaseEntity;

import java.util.List;

/**
 * 奶车信息对象 bs_milk_cart_info
 * 
 * @author hhy
 * @date 2025-11-05
 */
@Data
public class BsMilkCartInfo extends BaseEntity implements BaseEntity.CreateAware, BaseEntity.UpdateAware
{
    private static final long serialVersionUID = 1L;

    /** 奶车信息表主键 */
    private String id;

    /** 奶车状态 */
    @Excel(name = "奶车状态")
    private String cartStatus;

    /** 奶车车牌号码 */
    @Excel(name = "奶车车牌号码")
    private String cartLicensePlateNumber;

    /** 奶车司机姓名 */
    @Excel(name = "奶车司机姓名")
    private String cartDriverName;

    /** 奶车司机电话 */
    @Excel(name = "奶车司机电话")
    private String cartDriverPhone;

    /** 奶车铅封号，逗号隔开 */
    @Excel(name = "奶车铅封号，逗号隔开")
    private String cartLeadSealNumber;

    /** 奶车编号-备用字段 */
    @Excel(name = "奶车编号-备用字段")
    private String cartNumber;

    /** 奶车类型-备用字段 */
    @Excel(name = "奶车类型-备用字段")
    private String cartType;

    /** 部门id */
    @Excel(name = "部门id")
    private String deptId;

    /** 子表列表 */
    private List<BsMilkCartInfoLeadSeal> milkCartInfoLeadSealList;

}
