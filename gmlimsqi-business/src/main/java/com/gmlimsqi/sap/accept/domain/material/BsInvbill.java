package com.gmlimsqi.sap.accept.domain.material;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.gmlimsqi.common.annotation.Excel;
import com.gmlimsqi.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;
import java.util.List;

/**
 * 物料档案对象 bs_invbill
 * 
 * @author EGP
 * @date 2024-02-26
 */
@Data
@Accessors(chain = true)
public class BsInvbill extends BaseEntity
{
    private static final long serialVersionUID = 1L;
    /** id */
    private String id;
    private String previewBy;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "GMT+8")
    private Date previewTime;
    /** $column.columnComment */
    @Excel(name = "创建时间")
    private Date dcreatetime;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private Date dmodifytime;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String bsysdel;

    /** 物料分类编号 */
    @Excel(name = "物料分类编号")
    private String cclasscode;

    /** 物料分类名称 */
    @Excel(name = "物料分类名称")
    private String cclassname;

    /** 单据编号 */
    @Excel(name = "单据编号")
    private String ccode;

    /** 提交人 */
    @Excel(name = "提交人")
    private String ccommit;

    /** 公司编号 */
    @Excel(name = "公司编号")
    private String ccorpcode;

    /** 部门编号 */
    @Excel(name = "部门编号")
    private String cdepcode;

    /** 物料编号 */
    @Excel(name = "物料编号")
    private String cinvcode;

    /** 物料规格 */
    @Excel(name = "物料规格")
    private String cinvmodel;

    /** 物料名称 */
    @Excel(name = "物料名称")
    private String cinvname;

    /** 制单人 */
    @Excel(name = "制单人")
    private String cmaker;

    /** 制单人编号 */
    @Excel(name = "制单人编号")
    private String cmakercode;

    /** 职位编号 */
    @Excel(name = "职位编号")
    private String cposcode;

    /** 备注 */
    @Excel(name = "备注")
    private String cremark;

    /** 单据状态 */
    @Excel(name = "单据状态")
    private String cstate;

    /** 停用人编号 */
    @Excel(name = "停用人编号")
    private String ctypecode;

    /** 单位 */
    @Excel(name = "单位")
    private String cunit;

    /** 审批版本 */
    @Excel(name = "审批版本")
    private String cversion;

    /** 审核人 */
    @Excel(name = "审核人")
    private String cvery;

    /** 提交日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "提交日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date dcommit;

    /** 单据日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "单据日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date ddate;

    /** 审核日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "审核日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date dverydate;

    /** 停用 */
    @Excel(name = "停用")
    private String istop;

    /** cid */
    @Excel(name = "cid")
    private String cid;

    /** 停用人编号 */
    @Excel(name = "停用人编号")
    private String cstopname;

    /** 停用人 */
    @Excel(name = "停用人")
    private String cstopcode;

    /** 停用日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "停用日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date dstoptime;

    /** 是否有包装 */
    @Excel(name = "是否有包装")
    private String ibz;

    /** 是否质检 */
    @Excel(name = "是否质检")
    private String iquality;

    /** 按送货重量结算 */
    @Excel(name = "按送货重量结算")
    private String csh;

    /** 标准磅差 */
    @Excel(name = "标准磅差")
    private Long ibc;

    /** 公司名称 */
    @Excel(name = "公司名称")
    private String ccorpname;

    /** TMR物料编码 */
    @Excel(name = "TMR物料编码")
    private String ctmrcode;

    /** 必须上传送货单 */
    @Excel(name = "必须上传送货单")
    private String cinfosh;

    /** 更新 */
    @Excel(name = "更新")
    private String iupdate;
    private String sapcode;
    private String isshort;

    private String MATNR;

    private String MAKTX;

    /**
     * 是否收贮物料
     */
    @Excel(name = "是否收贮物料")
    private String cisstoreinv;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String isold;
    /**
     * 物料档案供应商信息
     */
    private List<BsInvbillCorp> bsInvbillCorps;
}
