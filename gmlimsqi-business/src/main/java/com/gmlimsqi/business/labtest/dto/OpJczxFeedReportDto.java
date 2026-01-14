package com.gmlimsqi.business.labtest.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Data
public class OpJczxFeedReportDto {

    private static final long serialVersionUID = 1L;

    private String opJczxFeedReportBaseId;
    //状态
    /**
     * 状态
     */
    private String status;
    /**
     * 报告编号
     */
    private String reportNo;

    /**
     * 样品名称
     */
    private String sampleName;

    /**
     * 样品编号
     */
    private String sampleNo;

    /**
     * 委托单位
     */
    private String entrustDeptName;

    /**
     * 委托单号
     */
    private String entrustOrderNo;

    /**
     * 接收时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date receiveTime;

    /**
     * 接收时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private String endReceiveTime;

    /**
     * 接收时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private String beginReceiveTime;

    /**
     * 检测时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date testTime;

    /**
     * 检测时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private String beginTestTime;

    /**
     * 检测时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private String endTestTime;

    /**
     * 审核时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date checkTime;

    /**
     * 审核时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private String beginCheckTime;

    /**
     * 审核时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private String endCheckTime;


    /**
     * 批准时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date approveTime;

    /**
     * 批准时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private String beginApproveTime;

    /**
     * 批准时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private String endApproveTime;

    /**
     * 编制人
     */
    private String editUser;

    /**
     * 审核人
     */
    private String checkUser;

    /**
     * 批准人
     */
    private String approveUser;

    /**
     * tab页类型
     */
    private String tabType;

    /**
     * 检测方法
     */
    private String testMethod;

    /**
     * 送样开始时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate beginSendSampleTime;

    /**
     * 送样结束时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate endSendSampleTime;

    /**
     * 报告开始时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate beginReportTime;

    /**
     * 报告结束时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate endReportTime;

    /**
     * 签发开始时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate beginIssuanceTime;

    /**
     * 签发结束时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate endIssuanceTime;

    /**
     * 检测中心饲料报告主ID
     */
    private String feedEntrustOrderId;

    /**
     * 检测中心饲料报告主ID
     */
    private List<String> sampleIdList;
}
