package com.gmlimsqi.business.labtest.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.gmlimsqi.common.annotation.Excel;
import lombok.Data;

import java.util.Date;

@Data
public class OpPcrSampleReceiveVo {

    private static final long serialVersionUID = 1L;


    /** id */
    private String opEntrustOrderId;

    /** 送检单号 */
    @Excel(name = "送检单号")
    private String entrustOrderNo;

    //1饲料 2血样 3pcr
    private String type = "3";

    @Excel(name = "状态",readConverterExp=" 1=待受理, 2=检测中 3=检测完成, 4=已审核, 5=已发送, 6=已驳回, 7=作废")
    private String status;
    /** 样品名称 */
    @Excel(name = "样品名称")
    private String sampleName;
    @Excel(name = "项目")
    private String itemName;
    @Excel(name = "备注")
    private String remark;
    @Excel(name = "组织")
    private String deptName;
    /** 报告是否需判定（0：不判定， 1：判定） */
    private String requiresJudgement;

    /** 是否同意分包（0：不同意， 1：同意） */
    private String allowsSubcontracting;

    /** 报告领取方式（1：自取，2：代邮，3：邮件） */
    private String reportReceiveType;
    @Excel(name = "送检时间")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date sendSampleDate;
    @Excel(name = "检测时间")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date testTime;
    @Excel(name = "检测人")
    private String testUser;
    @Excel(name = "收样时间")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date receiveTime;


}
