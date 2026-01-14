package com.gmlimsqi.business.labtest.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.gmlimsqi.common.annotation.Excel;
import com.gmlimsqi.common.core.domain.BaseEntity;
import lombok.Data;

/**
 * 检测中心血样化验单子对象 op_jczx_blood_result_info
 *
 * @author hhy
 * @date 2025-10-14
 */
@Data
public class OpJczxBloodResultInfo extends BaseEntity implements BaseEntity.CreateAware, BaseEntity.UpdateAware
{
    private static final long serialVersionUID = 1L;

    /** id */
    private String opJczxBloodResultInfoId;

    /** 主表id */
    @Excel(name = "主表id")
    private String baseId;

    /** 所属牧场 */
    @Excel(name = "所属牧场")
    private String deptName;
    @Excel(name = "导入牛号")
    private String importCowNo;
    /** 样品名称 */
    @Excel(name = "牛号")
    private String sampleName;
    @Excel(name = "母牛号")
    private String mnh;
    private String gh;
    private String fileId;
    /** 样品编号 */
    @Excel(name = "样品编号")
    private String sampleNo;
    /** 类别 */
    @Excel(name = "类别")
    private String sampleType;

    /** 委托单样品表id */
    @Excel(name = "委托单样品表id")
    private String bloodEntrustOrderSampleId;

    /** 项目表id */
    @Excel(name = "项目表id")
    private String itemId;

    /** 委托单项目表id */
    @Excel(name = "委托单项目表id")
    private String bloodEntrustOrderItemId;

    /** 项目名称 */
    @Excel(name = "项目名称")
    private String itemName;

    /** 检测结果 */
    @Excel(name = "检测结果")
    private String testResult;
    //
    @Excel(name = "S/P值")
    private String sp;
    /** 序号 */
    @Excel(name = "序号")
    private int sequence;

    /** 删除id（0为未删除，删除则为id） */
    @Excel(name = "删除id", readConverterExp = "0=为未删除，删除则为id")
    private String deleteId;

    private String aYpxj;
    private String aTestResult;
    private String oYpxj;
    private String oTestResult;
    private String examineNote;
    private String od;
    private String testRemark;
    private String zaoyunTestResult;
    private String shZdb;
    private String shBdb;
    private String shZg;
    private String shLzg;
    private String shMlz;
    private String shWjl;
    private String shNlz;
    private String shJlz;
    private String shLlz;
    private String shTlz;
    private String shTielz;
    private String shXlz;
    private String shGysz;
    private String shQds;
    private String shFzhx;
    private String shPpt;
    private String shXynsd;
    private String shGczam;
    private String shGbzam;
    private String shJxlsm;
    private String sjph;
    //A试剂批号
    @JsonProperty("aSjph")
    private String aSjph;
    //O试剂批号
    @JsonProperty("oSjph")
    private String oSjph;
    /** BVDV抗原*/
    @Excel(name = "犊牛号")
    private String dnh;
    @Excel(name = "部位")
    private String bw;
    @Excel(name = "S-N值")
    private String sn;
    @Excel(name = "判定结果")
    private String pdjg;
}
