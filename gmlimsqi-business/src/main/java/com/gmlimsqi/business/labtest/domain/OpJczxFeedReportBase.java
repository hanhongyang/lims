package com.gmlimsqi.business.labtest.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.gmlimsqi.common.annotation.Excel;
import com.gmlimsqi.common.core.domain.BaseEntity;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 检测中心饲料报告主对象 op_jczx_feed_report_base
 *
 * @author hhy
 * @date 2025-10-14
 */
@Data
public class OpJczxFeedReportBase extends BaseEntity implements BaseEntity.CreateAware, BaseEntity.UpdateAware {
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    private String opJczxFeedReportBaseId;

    /**
     * 报告编号
     */
    @Excel(name = "报告编号")
    private String reportNo;

    // 状态 （0:待制作，1：待提交，2：待审核，3：待批准，4：待发送，5：已发送，6：作废）
    private String status;
    /**
     * 样品名称
     */
    @Excel(name = "样品名称")
    private String sampleName;

    /**
     * 样品编号
     */
    @Excel(name = "样品编号")
    private String sampleNo;

    /**
     * 委托单位
     */
    @Excel(name = "委托单位")
    private String entrustDeptName;

    /**
     * 样品型号/规格
     */
    private String sampleModel;

    /**
     * 样品状况（1：鲜样，2：粉末，3：块状，4：颗粒，5：液体，6：条状）
     */
    private String sampleState;

    /**
     * 商标
     */
    private String trademark;

    /**
     * 样品数量
     */
    private String sampleAmount;

    /**
     * 接收时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "接收时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date receiveTime;

    /**
     * 检测时间
     */

    private String testTime;

    /**
     * 所用主要仪器
     */
    private String mainInsturment;
    private String mainInstrument;// 所用主要仪器

    /**
     * 检测结论
     */
    private String conclusion;

    /**
     * 编制人
     */
    private String editUserId;
    
    /**
     * 审核人
     */
    private String checkUserId;

    /**
     * 审核时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "审核时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date checkTime;

    /**
     * 批准人
     */
    private String approveUserId;

    /**
     * 批准时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "批准时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date approveTime;

    /**
     * 删除id（0为未删除，删除则为id）
     */
    private String deleteId;

    /**
     * 编制人
     */
    @Excel(name = "编制人")
    private String editUser;

    /**
     * 审核人
     */
    @Excel(name = "审核人")
    private String checkUser;

    /**
     * 批准人
     */
    @Excel(name = "批准人")
    private String approveUser;

    /**
     * pdf文件信息
     */
    private String pdfFileInfo;

    /**
     * 签发时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "签发时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date issuanceTime;
    /**
     * 发送时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "发送时间", width = 30, dateFormat = "yyyy-MM-dd  HH:mm:ss")
    private Date sendEmailTime;
    /**
     * 发送人
     */
    @Excel(name = "发送人")
    private String sendEmailUserId;

    /**
     * 检测中心饲料报告子信息
     */
    private List<OpJczxFeedReportInfo> opJczxFeedReportInfoList;
    // 饲料子表id
//    private String opFeedEntrustOrderSampleId;
    private String feedEntrustOrderSampleId;
    private String sampleBatchNo;
    private String testType;
    private String sampleSource;
    private String testLocation;
    private String itemName;
    private String opJczxFeedResultBaseId;
    // 委托单号
    private String feedEntrustOrderId;
    // 退回原因
    private String returnReason;
    // 委托方地址
    private String address;


}
