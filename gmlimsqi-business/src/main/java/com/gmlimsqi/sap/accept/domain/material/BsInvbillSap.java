package com.gmlimsqi.sap.accept.domain.material;

import com.gmlimsqi.common.annotation.Excel;
import com.gmlimsqi.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * sap物料推送对象 bs_invbill_sap
 *
 * @author EGP
 * @date 2024-03-27
 */
@Data
@Accessors(chain = true)
public class BsInvbillSap extends BaseEntity {
    private static final long serialVersionUID = 1L;
    
    /**
     * 物料编号
     */
    @Excel(name = "物料编号")
    private String matnr;
    
    /**
     * 物料描述
     */
    @Excel(name = "物料描述")
    private String maktx;
    
    /**
     * 工厂编号
     */
    @Excel(name = "工厂编号")
    private String werks;
    
    /**
     * 工厂名称
     */
    @Excel(name = "工厂名称")
    private String werksName1;
    
    /**
     * 基本计量单位
     */
    @Excel(name = "基本计量单位")
    private String meins;
    
    /**
     * 旧物料号
     */
    @Excel(name = "旧物料号")
    private String bismt;
    
    /**
     * 毛重
     */
    @Excel(name = "毛重")
    private String brgew;
    
    /**
     * 净重
     */
    @Excel(name = "净重")
    private String ntgew;
    
    /**
     * 重量单位
     */
    @Excel(name = "重量单位")
    private String gewei;
    
    /**
     * 安全库存
     */
    @Excel(name = "安全库存")
    private String eisbe;
    
    /**
     * 规格型号
     */
    @Excel(name = "规格型号")
    private String zbzgg;
    
    /**
     * 物料组编码
     */
    @Excel(name = "物料组编码")
    private String matkl;
    
    /**
     * 物料组描述
     */
    @Excel(name = "物料组描述")
    private String wgbez;
    
    /**
     * 物料状态
     */
    @Excel(name = "物料状态")
    private String synstatus;
    
    /**
     * 预留字段1
     */
    @Excel(name = "预留字段1")
    private String reserved1;
    
    /**
     * 预留字段2
     */
    @Excel(name = "预留字段2")
    private String reserved2;
    
    /**
     * 预留字段3
     */
    @Excel(name = "预留字段3")
    private String reserved3;
    
    /**
     * 预留字段4
     */
    @Excel(name = "预留字段4")
    private String reserved4;
    
}
