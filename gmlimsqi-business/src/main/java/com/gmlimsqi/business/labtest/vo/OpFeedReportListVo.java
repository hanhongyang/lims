package com.gmlimsqi.business.labtest.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.gmlimsqi.common.annotation.Excel;
import lombok.Data;

import java.util.Date;

@Data
public class OpFeedReportListVo {

    private static final long serialVersionUID = 1L;

    //状态
    @Excel(name = "状态",readConverterExp = "1：制作中,2：制作完成,3：已审核,4：已发送,5：作废")
    private String status;

    /** 报告编号 */
    @Excel(name = "报告编号")
    private String reportNo;

    /** 报告时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "报告时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date reportTime;
    private String pdfFileInfo;
    /** 样品名称 */
    @Excel(name = "样品名称")
    private String sampleName;

    /** 样品编号 */
    @Excel(name = "样品编号")
    private String sampleNo;

    /** 委托单位 */
    @Excel(name = "委托单位")
    private String entrustDeptName;

    /** 接收时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "接收时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date receiveTime;

    /** 检测时间 */
//    @JsonFormat(pattern = "yyyy-MM-dd")
//    @Excel(name = "检测时间", width = 30, dateFormat = "yyyy-MM-dd")
    private String testTime;
    /** 审核时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "审核时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date checkTime;

     /** 送样时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "送样时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date sendSampleDate;

    /** 批准时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "批准时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date approveTime;

    /** 签发时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "签发时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date issuanceTime;

    /** 编制人 */
    @Excel(name = "编制人")
    private String editUser;

    /** 审核人 */
    @Excel(name = "审核人")
    private String checkUser;

    /** 批准人 */
    @Excel(name = "批准人")
    private String approveUser;
    //是否制作
    private String isMake;
    //饲料样品表id
    private String feedEntrustOrderSampleId;
    //饲料报告主表id
    private String opJczxFeedReportBaseId;
    //委托单id
    private String feedEntrustOrderId;
    //已校准样品数量
    private Integer ypzCount;
    //未校准样品数量
    private Integer wpzCount;
    //委托单号
    private String entrustOrderNo;
    //报告结论
    private String conclusion;
    //备注
    private String remark;
    //退回原因
    private String returnReason;

}
