package com.gmlimsqi.business.ranch.domain;

import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.gmlimsqi.common.annotation.Excel;
import com.gmlimsqi.common.core.domain.BaseEntity;

import java.util.Date;

/**
 * 化验结果信息对象 op_test_result_info
 * 
 * @author hhy
 * @date 2025-11-07
 */
@Data
public class OpTestResultInfo extends BaseEntity implements BaseEntity.CreateAware, BaseEntity.UpdateAware
{
    private static final long serialVersionUID = 1L;

    /** 化验结果信息表id */
    private String id;

    /** 父表id */
    @Excel(name = "父表id")
    private String baseId;

    /** 部门id */
    @Excel(name = "部门id")
    private String deptId;

    /** 部门名称 */
    @Excel(name = "部门名称")
    private String deptName;

    /** 样品表id */
    @Excel(name = "样品表id")
    private String sampleId;

    /** 样品编号 */
    @Excel(name = "样品编号")
    private String sampleNo;

    /** 样品名称 */
    @Excel(name = "样品名称")
    private String sampleName;

    /** 项目名称 */
    @Excel(name = "项目名称")
    private String itemName;

    /** 项目id */
    @Excel(name = "项目id")
    private String itemId;


    /** 物料id */
    @Excel(name = "物料id")
    private String invbillId;
    @Excel(name = "物料编码")
    private String invbillCode;
    /** 物料名称 */
    @Excel(name = "物料名称")
    private String invbillName;

    /** 取样计划表id */
    @Excel(name = "取样计划表id")
    private String planId;
    //参考范围，给定性的结果判定范围用的
    private String referenceRange;
    /** 取样计划样品表id */
    @Excel(name = "取样计划样品表id")
    private String planSampleId;

    /** 取样计划项目表id */
    @Excel(name = "取样计划项目表id")
    private String planItemId;

    /** 检测结果 */
    @Excel(name = "检测结果")
    private String result;

    /** 试剂批号 */
    @Excel(name = "试剂批号")
    private String sjph;

    /** 试剂条文件id */
    @Excel(name = "试剂条文件id")
    private String sjtFileId;
    private String sjtFileUrl;
    /** 判断结果 */
    @Excel(name = "判断结果")
    private String checkResult;

    /** 删除id（0为未删除，删除则为id） */
    private String deleteId;
    private String retestFlag;

    /** 是否推送SAP（0为未推送，1为已推送） */
    private String isPushSap;

    /** 推送人 id */
    private String pushPersonId;

    /** 推送人名称 */
    private String pushPersonName;

    /** 推送时间 */
    private Date pushTime;

    /** 项目特性id */
    @Excel(name = "项目特性id")
    private String featureId;

    /** 项目特性 */
    private String featureName;

    /** 上限 */
    @Excel(name = "上限")
    private String upperLimit;

    /** 下限 */
    @Excel(name = "下限")
    private String lowerLimit;

    /** 定性或定量 1-定性 2-定量 */
    private String qualitativeOrQuantitative;

    /** 样品化验项目信息 */
    private OpSamplingPlanItem opSamplingPlanItem;

}
