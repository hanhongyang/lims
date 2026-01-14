package com.gmlimsqi.business.bsweighbridge.domain;

import lombok.Data;
import com.gmlimsqi.common.annotation.Excel;
import com.gmlimsqi.common.core.domain.BaseEntity;

/**
 * 过磅单对象 bs_weigh_bridge
 * 
 * @author hhy
 * @date 2025-11-13
 */
@Data
public class BsWeighBridge extends BaseEntity implements BaseEntity.CreateAware, BaseEntity.UpdateAware
{
    private static final long serialVersionUID = 1L;

    /** 过磅单主键 */
    private String id;

    /** 磅单号 */
    @Excel(name = "磅单号")
    private String cweightno;

    /** 过磅类型 */
    @Excel(name = "过磅类型")
    private String weighingType;

    /** 签到id */
    @Excel(name = "签到id")
    private String sginId;

    /** 司机车牌号 */
    @Excel(name = "司机车牌号")
    private String driverCode;

    /** 是否过毛 */
    @Excel(name = "是否过毛")
    private String gmFlag;

    /** 过毛时间 */
    @Excel(name = "过毛时间")
    private String gmtime;

    /** 毛重 */
    @Excel(name = "毛重")
    private String gmweight;

    /** 是否过皮 */
    @Excel(name = "是否过皮")
    private String gpFlag;

    /** 过皮时间 */
    @Excel(name = "过皮时间")
    private String gptime;

    /** 皮重 */
    @Excel(name = "皮重")
    private String gpweight;

    /** 净重 */
    @Excel(name = "净重")
    private String netWeight;

    /** 结算重量 */
    @Excel(name = "结算重量")
    private String jsWeight;

    /** 物料编码 */
    @Excel(name = "物料编码")
    private String materialCode;

    /** 物料名称 */
    @Excel(name = "物料名称")
    private String materialName;

    /** 供应商编码 */
    @Excel(name = "供应商编码")
    private String supplierCode;

    /** 供应商名称 */
    @Excel(name = "供应商名称")
    private String supplierName;

    /** 仓库编码 */
    @Excel(name = "仓库编码")
    private String warehouseCode;

    /** 仓库名称 */
    @Excel(name = "仓库名称")
    private String warehouseName;

    /** 工厂sap编码 */
    @Excel(name = "工厂sap编码")
    private String sapCode;

    /** 是否已经使用   1已使用 */
    @Excel(name = "是否已经使用   1已使用")
    private String isUsed;

    /** 地磅附件 */
    @Excel(name = "地磅附件")
    private String fileInfo;

    /** 第一次过磅文件 */
    @Excel(name = "第一次过磅文件")
    private String firstWeighingFile;

    /** 第二次过磅文件 */
    @Excel(name = "第二次过磅文件")
    private String secondWeighingFile;

    /** 是否作废0:未作废，1：已作废 */
    @Excel(name = "是否作废0:未作废，1：已作废")
    private String voidOrNot;

    /** 是否超差  是、否 */
    @Excel(name = "是否超差  是、否")
    private String icc;

    /** 是否发送消息   0否1是 */
    @Excel(name = "是否发送消息   0否1是")
    private String isend;

    /** 打印次数 */
    @Excel(name = "打印次数")
    private String printCount;

    /** 是否上传sap   0否1是 */
    @Excel(name = "是否上传sap   0否1是")
    private String isUpload;

    /** sap上传结果 */
    @Excel(name = "sap上传结果")
    private String sapRes;

     /** 物料凭证 */
    @Excel(name = "物料凭证")
    private String materialVoucher;

    /**
     * 上传系统类型，0：无人，1：有人
     */
    private String sys_type;

    /**
     * 是否按送货重量结算
     */
    private String csh;

    /**
     * 送货重量
     */
    private String ishweight;

    /**
     * 扣重
     */
    private String idelweight;

    /**
     * 包装数量
     */
    private String ibzqty;

    /**
     * 牛号
     */
    private String cniuhao;

    /**
     * 订单号
     */
    private String cordercode;

    /**
     * sap行
     */
    private String csap_hangxiangmu;

    /**
     * TMR订单号
     */
    private String cordercode_TMR;

    /**
     * TMR订单行
     */
    private String changxiangmu_TMR;

    /**
     * TMR供应商
     */
    private String cvencode_TMR;

    /**
     * TMR供应商名称
     */
    private String cvenname_TMR;

    /**
     * 交货订单号
     */
    private String cordercode_jh;

    /**
     * 交货订单行
     */
    private String changxiangmu_jh;

    /**
     * 物料凭证
     */
    private String cpzcode;

    /**
     * TMR凭证
     */
    private String cpzcodeTmr;

    /**
     * SAP返回信息
     */
    private String csapmess;

    /** 批次 */
    private String cpc;

}
