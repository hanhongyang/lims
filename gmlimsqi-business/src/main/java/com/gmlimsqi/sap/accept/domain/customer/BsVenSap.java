package com.gmlimsqi.sap.accept.domain.customer;

import com.gmlimsqi.common.annotation.Excel;
import com.gmlimsqi.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 供应商sap对接对象 bs_ven_sap
 *
 * @author EGP
 * @date 2024-03-28
 */
@Data
@Accessors(chain = true)
public class BsVenSap extends BaseEntity {
    private static final long serialVersionUID = 1L;
    
    /**
     * 公司代码
     */
    @Excel(name = "公司代码")
    private String bukrs;
    
    /**
     * 工厂
     */
    @Excel(name = "工厂")
    private String bwkey;
    
    /**
     * 业务伙伴编号
     */
    @Excel(name = "业务伙伴编号")
    private String partner;
    
    /**
     * 组织名称 1
     */
    @Excel(name = "组织名称 1")
    private String nameOrg1;
    
    /**
     * 创建日期
     */
    @Excel(name = "创建日期")
    private String crdat;
    
    /**
     * 更改日期
     */
    @Excel(name = "更改日期")
    private String chdat;
    
    /**
     * 地区
     */
    @Excel(name = "地区")
    private String regio;
    
    /**
     * 城市
     */
    @Excel(name = "城市")
    private String city1;
    
    /**
     * 街道
     */
    @Excel(name = "街道")
    private String street;
    
    /**
     * 电话
     */
    @Excel(name = "电话")
    private String telNumber;
    
    /**
     * 类别
     */
    @Excel(name = "类别")
    private String bprole;
    
    /**
     * 公司代码层删除冻结
     */
    @Excel(name = "公司代码层删除冻结")
    private String nodel;
    
    /**
     * 对公司代码过帐冻结
     */
    @Excel(name = "对公司代码过帐冻结")
    private String sperr;
    private String company_id;
    
}
