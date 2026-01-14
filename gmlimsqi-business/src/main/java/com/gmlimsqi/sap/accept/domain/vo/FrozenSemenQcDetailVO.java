package com.gmlimsqi.sap.accept.domain.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * QM101冻精质检数据查询响应明细VO
 */
@Data
public class FrozenSemenQcDetailVO {

    /** 工厂 */
    @JsonProperty("WERK")
    private String WERK;

    /** 检验批次 */
    @JsonProperty("PRUEFLOS")
    private String PRUEFLOS;

    /** 物料编码 */
    @JsonProperty("SELMATNR")
    private String SELMATNR;

    /** 物料描述 */
    @JsonProperty("KTEXTMAT")
    private String KTEXTMAT;

    /** 样品编号 */
    @JsonProperty("ZNUM")
    private String ZNUM;

    /** 到厂日期 */
    @JsonProperty("ENSTEHDAT")
    private String ENSTEHDAT;

    /** 决策日期 */
    @JsonProperty("BUDAT")
    private String BUDAT;

    /** 供应商编码（可选，技术名称SELLIFNR） */
    @JsonProperty("SELLIFNR")
    private String SELLIFNR;

    /** 供应商名称 */
    @JsonProperty("NAM1")
    private String NAM1;

    /** 采购凭证编号 */
    @JsonProperty("EBELN")
    private String EBELN;

    /** 行项目号 */
    @JsonProperty("EBELP")
    private String EBELP;

    /** 生产订单号 */
    @JsonProperty("AUFNR")
    private String AUFNR;

    /** 批数量 */
    @JsonProperty("LOSMENGE")
    private String LOSMENGE;

    /** 在接收标准范围内 */
    @JsonProperty("ZJS")
    private String ZJS;

    /** 在可接收标准范围内 */
    @JsonProperty("ZKJS")
    private String ZKJS;

    /** 在超收货范围内 */
    @JsonProperty("ZCJS")
    private String ZCJS;

    /** 判定结果 */
    @JsonProperty("VCODE")
    private String VCODE;

    /** 判定结果描述 */
    @JsonProperty("KURZTEXT")
    private String KURZTEXT;

    /** 销售订单 */
    @JsonProperty("KDAUF")
    private String KDAUF;

    /** SO项目 */
    @JsonProperty("KDPOS")
    private String KDPOS;

    /** 交货单号 */
    @JsonProperty("LS_VBELN")
    private String LS_VBELN;

    /** 交货单行项目 */
    @JsonProperty("LS_POSNR")
    private String LS_POSNR;

    /** 客户 */
    @JsonProperty("KUNNR")
    private String KUNNR;

    /** 客户名称 */
    @JsonProperty("KUNNR_T")
    private String KUNNR_T;


}