package com.gmlimsqi.business.labtest.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.gmlimsqi.common.annotation.Excel;
import com.gmlimsqi.common.core.domain.BaseEntity;
import lombok.Data;

import java.util.Date;

/**
 * 检测中心检测任务对象 op_jczx_test_task
 * 
 * @author hhy
 * @date 2025-09-22
 */
@Data
public class OpJczxTestTaskVo extends BaseEntity implements BaseEntity.CreateAware, BaseEntity.UpdateAware
{
    private static final long serialVersionUID = 1L;


    /** 委托单-项目子表id */
    private String entrustOrderItemId;
    /** 委托单-样品子表id */
    private String entrustOrderSampleId;
    /** 结果 */
    @Excel(name = "结果")
    private String result;
    //近红外报告文件id
    private String fileId;
    /** 检测人id */
    private String testUserId;
    private String isTest;
    private String testMethod;
    /** 项目id */
    private String itemId;
    @Excel(name = "项目")
    private String itemName;
    /** 检测结果 */
    private String testResult;

    /** 检测人 */
    private String testUser;
    private String opBloodEntrustOrderId;
    private String invbillName;
    private String sampleName;
    /** 该类型的样品数量 */
    private Integer sampleCount;
    //检测时间/导入时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date testTime;
    /** 样品编号 */
    private String sampleNo;
    //接收时间
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date receiveTime;
    //审核时间
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date examineTime;
    //接收人id
    private String receiverId;
    //接收人
    private String receiverName;
    /** 送检单号 */
    @Excel(name = "送检单号")
    private String entrustOrderNo;
    private String entrustDeptName;
    private String invbillCode;

    //pcr检测项目类别
    private String pcrTaskItemType;
    //血样样品类型
    private String sampleType;
    //血样送样时间
    private String sendSampleDate;
    //血样检测项目类别
    private String bloodTaskItemType;
    //血样送样人
    private String sendSampleUserName;
    //委托单id
    private String entrustOrderId;

    //导入文件名
    private String fileName;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private String testDate;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private String testEndTime;
    //文件条数
    private String fileSize;
    //导出文件名
    private String exportFileName;

    private String resultNo;
    private String examinePassFlag;
    private String blTemplateType;
    private String isRetest;
    private String tag;
    private String primaryMoistureStatus;
}
