package com.gmlimsqi.business.labtest.bo;

import com.gmlimsqi.common.annotation.Excel;
import lombok.Data;

/**
 * PCR检测结果导入BO
 */
@Data
public class BloodResultImportBo {
    
    @Excel(name = "管号")
    private String gh;

    
    @Excel(name = "序号")
    private String sequence;
    @Excel(name = "委托单号")
    private String entrustOrderNo;
    @Excel(name = "所属牧场")
    private String deptName;
    @Excel(name = "试剂批号")
    private String sjph;
    @Excel(name = "A型试剂批号")
    private String aSjph;
    @Excel(name = "O型试剂批号")
    private String oSjph;
    @Excel(name = "牛号")
    private String sampleName;
    @Excel(name = "母牛号")
    private String mnh;
    @Excel(name = "样品编号")
    private String sampleNo;
    
    @Excel(name = "委托备注")
    private String remark;
    /** 结核*/
    @Excel(name = "S/P值")
    private String sp;
    
    @Excel(name = "结果判定")
    private String testResult;
    /** 结核*/

    /** 口蹄疫*/
    @Excel(name = "A型口蹄疫样品效价")
    private String aYpxj;
    @Excel(name = "A型口蹄疫结果判定")
    private String aTestResult;
    @Excel(name = "O型口蹄疫样品效价")
    private String oYpxj;
    @Excel(name = "O型口蹄疫结果判定")
    private String oTestResult;
    /** 口蹄疫*/

    /** 早孕*/
    @Excel(name = "导入牛号")
    private String importCowNo;
    @Excel(name = "BioPRYN OD")
    private String od;
    @Excel(name = "备注")
    private String testRemark;
    @Excel(name = "结果")
    private String zaoyunTestResult;
    /** 早孕*/

    /** 生化*/
    @Excel(name = "总蛋白")
    private String shZdb;

    @Excel(name = "白蛋白")
    private String shBdb;

    @Excel(name = "总钙")
    private String shZg;

    @Excel(name = "离子钙")
    private String shLzg;

    @Excel(name = "镁离子")
    private String shMlz;

    @Excel(name = "无机磷")
    private String shWjl;

    @Excel(name = "钠离子")
    private String shNlz;

    @Excel(name = "钾离子")
    private String shJlz;

    @Excel(name = "氯离子")
    private String shLlz;

    @Excel(name = "铜离子")
    private String shTlz;

    @Excel(name = "铁离子")
    private String shTielz;

    @Excel(name = "锌离子")
    private String shXlz;

    @Excel(name = "甘油三酯")
    private String shGysz;

    @Excel(name = "β-羟丁酸")
    private String shQds;

    @Excel(name = "非酯化性脂肪酸")
    private String shFzhx;

    @Excel(name = "葡萄糖")
    private String shPpt;

    @Excel(name = "血液尿素氮")
    private String shXynsd;

    @Excel(name = "谷草转氨酶")
    private String shGczam;

    @Excel(name = "谷丙转氨酶")
    private String shGbzam;

    @Excel(name = "碱性磷酸酶")
    private String shJxlsm;
    /** 生化*/

    /** BVDV抗原*/
    @Excel(name = "犊牛号")
    private String dnh;
    @Excel(name = "部位")
    private String bw;
    @Excel(name = "S-N值")
    private String sn;
    @Excel(name = "判定结果")
    private String pdjg;


    /** BVDV抗原*/

}