package com.gmlimsqi.sap.accept.domain.customer;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class Line {
    
    /**
     * 公司代码
     */
    private String BUKRS;
    /**
     * 工厂
     */
    private String BWKEY;
    /**
     * 业务伙伴编号-仓库SAP编码
     */
    private String PARTNER;
    /**
     * 组织名称 1
     */
    private String NAME_ORG1;
    /**
     * 创建日期
     */
    private String CRDAT;
    /**
     * 更改日期
     */
    private String CHDAT;
    /**
     * 地区
     */
    private String REGIO;
    /**
     * 城市
     */
    private String CITY1;
    /**
     * 街道
     */
    private String STREET;
    /**
     * 电话
     */
    private String TEL_NUMBER;
    /**
     * 类别   C = 客商  S 供应商
     */
    private String BPROLE;
    /**
     * 公司代码层删除冻结
     */
    private String NODEL;
    /**
     * 对公司代码过帐冻结
     */
    private String SPERR;
    
}
