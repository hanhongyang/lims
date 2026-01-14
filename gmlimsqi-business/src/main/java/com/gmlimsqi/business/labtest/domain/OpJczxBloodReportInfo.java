package com.gmlimsqi.business.labtest.domain;

import com.gmlimsqi.common.annotation.Excel;
import com.gmlimsqi.common.core.domain.BaseEntity;
import lombok.Data;

/**
 * 检测中心血样报告子对象 op_jczx_blood_report_info
 * 
 * @author hhy
 * @date 2025-10-22
 */
@Data
public class OpJczxBloodReportInfo extends BaseEntity implements BaseEntity.CreateAware, BaseEntity.UpdateAware
{
    private static final long serialVersionUID = 1L;

    /** id */
    private String opJczxBloodReportInfoId;

    /** 主表id */
    @Excel(name = "主表id")
    private String baseId;
    @Excel(name = "管号")
    private String gh;
    /** 牛号 */
    @Excel(name = "牛号")
    private String sampleName;
    @Excel(name = "母牛号")
    private String mnh;
    private String sampleNo;
    private int sequence;
    /** 检测结果 */
    @Excel(name = "检测结果")
    private String testResult;
    /** 类别 1哺乳犊牛、2断奶犊牛、3发育牛、4成母牛、5干奶牛、6干乳牛、7后备牛、8泌乳牛、9青年牛、10育成牛*/
    @Excel(name = "类别")
    private String sampleType;
    /** SP值 */
    @Excel(name = "SP值")
    private String sp;

    /** 删除id（0为未删除，删除则为id） */
    @Excel(name = "删除id", readConverterExp = "0=为未删除，删除则为id")
    private String deleteId;

    /** A型样品效价 */
    @Excel(name = "A型样品效价")
    private String aYpxj;

    /** A型检测结果 */
    @Excel(name = "A型检测结果")
    private String aTestResult;

    /** O型样品效价 */
    @Excel(name = "O型样品效价")
    private String oYpxj;

    /** O型检测结果 */
    @Excel(name = "O型检测结果")
    private String oTestResult;
    private String od;
    private String testRemark;
    private String zaoyunTestResult;
    /** 总蛋白 */
    @Excel(name = "总蛋白")
    private String shZdb;

    /** 白蛋白 */
    @Excel(name = "白蛋白")
    private String shBdb;

    /** 总钙 */
    @Excel(name = "总钙")
    private String shZg;

    /** 离子钙 */
    @Excel(name = "离子钙")
    private String shLzg;

    /** 镁离子 */
    @Excel(name = "镁离子")
    private String shMlz;

    /** 无机磷 */
    @Excel(name = "无机磷")
    private String shWjl;

    /** 钠离子 */
    @Excel(name = "钠离子")
    private String shNlz;

    /** 钾离子 */
    @Excel(name = "钾离子")
    private String shJlz;

    /** 氯离子 */
    @Excel(name = "氯离子")
    private String shLlz;

    /** 铜离子 */
    @Excel(name = "铜离子")
    private String shTlz;

    /** 铁离子 */
    @Excel(name = "铁离子")
    private String shTielz;

    /** 锌离子 */
    @Excel(name = "锌离子")
    private String shXlz;

    /** 甘油三酯 */
    @Excel(name = "甘油三酯")
    private String shGysz;

    /** β-羟丁酸 */
    @Excel(name = "β-羟丁酸")
    private String shQds;

    /** 非酯化性脂肪酸 */
    @Excel(name = "非酯化性脂肪酸")
    private String shFzhx;

    /** 葡萄糖 */
    @Excel(name = "葡萄糖")
    private String shPpt;

    /** 血液尿素氮 */
    @Excel(name = "血液尿素氮")
    private String shXynsd;

    /** 谷草转氨酶 */
    @Excel(name = "谷草转氨酶")
    private String shGczam;

    /** 谷丙转氨酶 */
    @Excel(name = "谷丙转氨酶")
    private String shGbzam;

    /** 碱性磷酸酶 */
    @Excel(name = "碱性磷酸酶")
    private String shJxlsm;
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
