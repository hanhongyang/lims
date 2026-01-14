package com.gmlimsqi.business.labtest.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import com.gmlimsqi.common.annotation.Excel;
import com.gmlimsqi.common.core.domain.BaseEntity;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

/**
 * 检测中心检测任务对象 op_jczx_test_task
 *
 * @author hhy
 * @date 2025-09-22
 */
@Data
public class OpJczxTestTaskDto extends BaseEntity implements BaseEntity.CreateAware, BaseEntity.UpdateAware {
    private static final long serialVersionUID = 1L;


    /**
     * 委托单-项目子表id
     */
    private String entrustOrderItemId;
    /**
     * 委托单-样品子表id
     */
    private String entrustOrderSampleId;
    private List<String> entrustOrderSampleIdList;
    private List<String> entrustOrderIdList;
    /**
     * 结果
     */
    private String result;
    private String itemId;
    private String testMethod;
    private String itemName;
    /**
     * 化验单号
     */
    private String resultNo;
    // 是否开始化验
    private String isTest;
    private String status;
    /**
     * 检测人
     */
    private String testUser;
    // 当前检测人id
    private String userId;
    private String invbillName;
    private String sampleName;
    // 检测时间/导入时间
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date begintestTime;
    // 检测时间/导入时间
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date endtestTime;
    // 审核时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date beginExamineTime;
    // 审核时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endExamineTime;
    /**
     * 样品编号
     */
    private String sampleNo;
    // 接收时间开始
    @JsonFormat(pattern = "yyyy-MM-dd ")
    private Date beginReceiveTime;
    // 接收时间结束
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date endReceiveTime;
    // 接收人
    private String receiverName;
    /**
     * 送检单号
     */
    @Excel(name = "送检单号")
    private String entrustOrderNo;
    private String entrustDeptName;
    // 类型 "1", "饲料","2", "血样","3", "PCR"
    private String entrustOrderType;
    private String bloodTaskItemTypeNotIn;
    // pcr检测项目类别
    private String pcrTaskItemType;
    // 血样牛只类别
    private String sampleType;
    // 血样送样时间
    private String sendSampleDate;
    // 血样检测项目类别
    private String bloodTaskItemType;
    // 血样送样人
    private String sendSampleUserName;
    private List<String> entrustOrderId;
    private List<String> entrustOrderNoList;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date beginTestDate;
    // 检测时间/导入时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTestDate;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date beginTestEndTime;
    // 检测时间/导入时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTestEndTime;
    // 查询初水分 0否1是
    private String queryCsf;

    /**
     * 导入文件名
     */
    private String fileName;
    private String examineUser;
    /**
     * 导出文件名
     */
    private String exportFileName;
    /**
     * 文件大小
     */
    private Integer fileSize;
    // 8联模板类型 1中文2英文
    private String blTemplateType;
    // 初水分项目id
    private String csfItemId;
}
