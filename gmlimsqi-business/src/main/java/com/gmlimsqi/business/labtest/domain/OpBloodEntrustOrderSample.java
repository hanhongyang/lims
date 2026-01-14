package com.gmlimsqi.business.labtest.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.gmlimsqi.common.annotation.Excel;
import com.gmlimsqi.common.core.domain.BaseEntity;
import lombok.Data;

import java.util.Date;

/**
 * 血样样品委托-样品对应对象 op_blood_entrust_order_sample
 * 
 * @author hhy
 * @date 2025-09-20
 */
@Data
public class OpBloodEntrustOrderSample extends BaseEntity implements BaseEntity.CreateAware, BaseEntity.UpdateAware
{
    private static final long serialVersionUID = 1L;

    /** id */
    private String opBloodEntrustOrderId;

    /** 样品委托单样品表id */
    @Excel(name = "样品委托单样品表id")
    private String opBloodEntrustOrderSampleId;

    /** 是否删除 */
    @Excel(name = "是否删除")
    private String isDelete;

    /** 牛号 */
    @Excel(name = "牛号")
    private String sampleName;
    @Excel(name = "母牛号")
    private String mnh;
    /** 类别 1哺乳犊牛、2断奶犊牛、3发育牛、4成母牛、5干奶牛、6干乳牛、7后备牛、8泌乳牛、9青年牛、10育成牛*/
    @Excel(name = "类别")
    private String sampleType;

    //是否接收
    private String isReceive;
    //接收人id
    private String receiverId;
    //接收人
    private String receiverName;
    //接收时间
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date receiveTime;

    /** 样品编号 */
    private String sampleNo;
    //序号
    private int sequence;
    /** 管号 */
    private String gh;
    private String fileId;
    //配种天数
    private String pzts;
    /** 检测结果 */
    @Excel(name = "检测结果")
    private String testResult;

    /** 导入时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss") // 使用完整时间格式
    @Excel(name = "检测完成时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date testTime;

    /** 检测人 */
    @Excel(name = "检测人")
    private String testUser;

    /** 检测人id */
    @Excel(name = "检测人id")
    private String testUserId;

    /** 审核人 */
    @Excel(name = "审核人")
    private String examineUser;

    /** 审核人id */
    @Excel(name = "审核人id")
    private String examineUserId;
    //审核异议
    private String examineNote;
    /** 审核时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss") // 使用完整时间格式
    @Excel(name = "审核时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date examineTime;
    //血样检测项目类别
    private String bloodTaskItemType;
    //S/P值
    private String sp;
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
    private String entrustDeptName;
    private String examinePassFlag;
    private String od;
    private String testRemark;
    private String zaoyunTestResult;
    private String importCowNo;
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

    /**
     * 报告id
     */
    private String reportId;
    //试剂批号
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
