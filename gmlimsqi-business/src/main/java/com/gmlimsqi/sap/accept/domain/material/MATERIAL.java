package com.gmlimsqi.sap.accept.domain.material;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class MATERIAL {
    
    /**
     * 物料编号
     */
    private String MATNR;
    /**
     * 物料描述
     */
    private String MAKTX;
    /**
     * 工厂编号
     */
    private String WERKS;
    /**
     * 工厂名称
     */
    private String WERKS_NAME1;
    /**
     * 基本计量单位
     */
    private String MEINS;
    /**
     * 旧物料号
     */
    private String BISMT;
    /**
     * 毛重
     */
    private String BRGEW;
    /**
     * 净重
     */
    private String NTGEW;
    /**
     * 重量单位
     */
    private String GEWEI;
    /**
     * 安全库存
     */
    private String EISBE;
    /**
     * 规格型号
     */
    private String ZBZGG;
    /**
     * 物料组编码
     */
    private String MATKL;
    /**
     * 物料组描述
     */
    private String WGBEZ;
    /**
     * 物料状态
     */
    private String SYNSTATUS;
    /**
     * 预留字段1
     */
    private String RESERVED1;
    /**
     * 预留字段2
     */
    private String RESERVED2;
    /**
     * 预留字段3
     */
    private String RESERVED3;
    /**
     * 预留字段4
     */
    private String RESERVED4;
    
}
