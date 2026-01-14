package com.gmlimsqi.business.labtest.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.gmlimsqi.common.annotation.Excel;
import com.gmlimsqi.common.core.domain.BaseEntity;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * PCR样品委托单对象 op_pcr_entrust_order
 * 
 * @author hhy
 * @date 2025-09-17
 */
@Data
public class OpPcrEntrustOrder extends BaseEntity implements BaseEntity.CreateAware, BaseEntity.UpdateAware
{
    private static final long serialVersionUID = 1L;

    /** id */
    private String opPcrEntrustOrderId;

    /** 送检单号 */
    @Excel(name = "送检单号")
    private String entrustOrderNo;
    private String address;
    /** 委托单位id */
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long entrustDeptId;
    private String entrustDeptName;
    /** 联系方式id */
    private String entrustContactInfoId;

    /** 委托方联系人 */
    @Excel(name = "委托方联系人")
    private String entrustContact;

    /** 委托方联系电话 */
    @Excel(name = "委托方联系电话")
    private String entrustContactPhone;

    /** 委托方邮箱 */
    @Excel(name = "委托方邮箱")
    private String entrustContactEmail;

    /** 送样时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "送样时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date sendSampleDate;
    //"送检时间范围 - 开始"
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date sendSampleDateStart;
    //"送检时间范围 - 结束"
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date sendSampleDateEnd;
    /** 送样人id */
    private String sendSampleUserId;

    /** 送样人 */
    @Excel(name = "送样人")
    private String sendSampleUserName;
    //接收人id
    private String receiverId;
    //接收人
    private String receiver;
    @Excel(name = "收样时间")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date receiveTime;
    /** 样品总数量 */
    @Excel(name = "样品总数量")
    private Long totalSampleQuantity;

    /** 驳回原因 */
    @Excel(name = "驳回原因")
    private String rejectReason;

    /** 状态 1-待受理 2-检测中 3-检测完成 4-已审核 5-已发送 6-已驳回 7-作废 */
    @Excel(name = "状态",readConverterExp=" 1=待受理, 2=检测中 3=检测完成, 4=已审核, 5=已发送, 6=已驳回, 7=作废")
    private String status;

    /** 删除id（0为未删除，删除则为id） */
    private String deleteId;

    /** pcr样品委托单-样品信息 */
    private List<OpPcrEntrustOrderSample> sampleList;
    private List<String> sampleIdList;
    //是否接收
    private String isReceive = "0";
    /** 审核人 */
    @Excel(name = "审核人")
    private String examineUser;

    /** 审核人id */
    @Excel(name = "审核人id")
    private String examineUserId;
    /** 审核时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss") // 使用完整时间格式
    @Excel(name = "审核时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date examineTime;

    /** 是否退回 */
    private String isReturn;

    /** 退回时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss") // 使用完整时间格式
    @Excel(name = "退回时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date returnTime;

    /** 退回原因 */
    private String returnReason;
    //pcr检测项目类别
    private String pcrTaskItemType;

}
