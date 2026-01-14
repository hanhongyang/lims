package com.gmlimsqi.business.unquality.domain;

import java.util.List;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.gmlimsqi.common.annotation.Excel;
import com.gmlimsqi.common.core.domain.BaseEntity;

/**
 * 样品不合格处理单对象 op_sample_unquality
 * 
 * @author hhy
 * @date 2025-11-28
 */
@Data
public class OpSampleUnquality extends BaseEntity implements BaseEntity.CreateAware, BaseEntity.UpdateAware
{
    private static final long serialVersionUID = 1L;

    /** id */
    private String opSampleUnqualityId;

    /** 单号 */
    @Excel(name = "单号")
    private String unqualityNo;

    /** 签到id */
    private String signInId;

    /** 来源表id */
    @Excel(name = "来源表id")
    private String sourceId;

    /** 物料id */
    @Excel(name = "物料id")
    private String materialId;

    /** 物料编码 */
    @Excel(name = "物料编码")
    private String materialCode;

    /** 物料名称 */
    @Excel(name = "物料名称")
    private String materialName;

    /** 供应商编码 */
    private String supplierCode;

    /** 供应商名称 */
    private String supplierName;

    /** 是否删除 */
    @Excel(name = "是否删除")
    private String isDelete;

    /** 检测人id */
    @Excel(name = "检测人id")
    private String testUserId;

    /** 检测结果 */
    @Excel(name = "检测结果")
    private String testResult;

    /** 状态 */
    @Excel(name = "状态")
    private String status;

     /** 部门id */
    private String deptId;

    /** 不合格类别：感官不合格 化验不合格 */
    @Excel(name = "不合格类别：感官不合格 化验不合格")
    private String ctype;

    /** 处理方式：拒收、让步接收 */
    private String ccltype;

    /** 扣重 */
    private String ikjweight;

    /** 按比例扣重 */
    private String iProportionkjweight;

    /** 扣款 */
    private String ikjmoney;

    /** 不合格描述 */
    private String cunqualityinfo;

     /** 处理类型，1：未处理，2：已处理，3：手工处理 */
    private String processingType;

    /** sap编码 */
    private String sapName;

    /** 不合格处理单文件路径 */
    private String cfilepath;

    /** 批次 */
    private String batch;

    /** 原始磅单号 */
    private String originalCweightno;

    /** 核销磅单号 */
    private String writeOffCweightno;

    /** 样品不合格处理单详情信息 */
    private List<OpSampleUnqualityDetail> opSampleUnqualityDetailList;

}
