package com.gmlimsqi.sap.accept.domain.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * QM102入库检验批查询响应明细VO
 */
@Data
public class InboundInspectionDetailVO {

    @JsonProperty("WERK")
    private String WERK; // 工厂

    @JsonProperty("WERK_NAME1")
    private String WERK_NAME1; // 工厂描述

    @JsonProperty("PRUEFLOS")
    private String PRUEFLOS; // 检验批次

    @JsonProperty("PASTRTERM")
    private String PASTRTERM; // 检验开始日期

    @JsonProperty("HERKUNFT")
    private String HERKUNFT; // 检验类型

    @JsonProperty("SELMATNR")
    private String SELMATNR; // 物料编码

    @JsonProperty("KTEXTMAT")
    private String KTEXTMAT; // 物料描述

    @JsonProperty("CHARG")
    private String CHARG; // 批次

    @JsonProperty("LAGORTCHRG")
    private String LAGORTCHRG; // 库存地点

    @JsonProperty("MBLNR")
    private String MBLNR; // 物料凭证

    @JsonProperty("ZEILE")
    private String ZEILE; // 物料凭证行项目

    @JsonProperty("BWART")
    private String BWART; // 移动类型

    @JsonProperty("LOSMENGE")
    private String LOSMENGE; // 批数量

    @JsonProperty("EBELN")
    private String EBELN; // 采购凭证编号

    @JsonProperty("EBELP")
    private String EBELP; // 行项目号

    @JsonProperty("AUFNR")
    private String AUFNR; // 生产订单号

    @JsonProperty("KDAUF")
    private String KDAUF; // 销售订单

    @JsonProperty("KDPOS")
    private String KDPOS; // SO项目

    @JsonProperty("LS_VBELN")
    private String LS_VBELN; // 交货单号

    @JsonProperty("LS_POSNR")
    private String LS_POSNR; // 交货单行项目

    @JsonProperty("STAT")
    private String STAT; // 系统状态
}