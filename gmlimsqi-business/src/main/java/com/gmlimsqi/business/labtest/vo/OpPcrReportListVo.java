package com.gmlimsqi.business.labtest.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.gmlimsqi.common.annotation.Excel;
import lombok.Data;

import java.util.Date;
@Data
public class OpPcrReportListVo {
    /** 报告编号 */
    @Excel(name = "报告编号")
    private String reportNo;

    /** 委托单位 */
    @Excel(name = "委托单位")
    private String entrustDeptName;

    /** 接收时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "接收时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date receiveTime;
    //pcr检测项目类别
    private String pcrTaskItemType;
    private String entrustOrderNo;

    private String receiverId;

    /** 状态 1-待受理 2-检测中 3-检测完成 4-已审核 5-已发送 6-已驳回 7-作废 */
    @Excel(name = "状态",readConverterExp=" 1=待受理, 2=检测中 3=检测完成, 4=已审核, 5=已发送, 6=已驳回, 7=作废")
    private String status;
    private String entrustContact;
    @JsonFormat(pattern = "yyyy-MM-dd ")
    private Date beginReceiveTime;
    private String opPcrEntrustOrderId;
    private String testTime;
    private String testUser;
    private String editUserId;
    private String checkUserId;
    private String checkTime;
    private String examineTime;
    private String approveUserId;
    private String approveTime;
    private String editUser;
    private String checkUser;
    private String approveUser;
    private String sampleQuantity;








}
