package com.gmlimsqi.business.labtest.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.gmlimsqi.common.annotation.Excel;
import com.gmlimsqi.common.core.domain.BaseEntity;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

/**
 * 血样样品委托单对象 op_blood_entrust_order
 *
 * @author hhy
 * @date 2025-09-20
 */
@Data
public class OpBloodEntrustOrder extends BaseEntity implements BaseEntity.CreateAware, BaseEntity.UpdateAware {
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    private String opBloodEntrustOrderId;

    /**
     * 送检单号
     */
    @Excel(name = "送检单号")
    private String entrustOrderNo;

    /**
     * 委托单位id
     */
    @Excel(name = "委托单位id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long entrustDeptId;

    /**
     * 地址
     */
    @Excel(name = "地址")
    private String address;

    /**
     * 委托方联系人
     */
    @Excel(name = "委托方联系人")
    private String entrustContact;

    /**
     * 委托方联系电话
     */
    @Excel(name = "委托方联系电话")
    private String entrustContactPhone;

    /**
     * 委托方邮箱
     */
    @Excel(name = "委托方邮箱")
    private String entrustContactEmail;

    /**
     * 送样时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "送样时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date sendSampleDate;

    /**
     * 送样人id
     */
    @Excel(name = "送样人id")
    private String sendSampleUserId;

    /**
     * 送样人
     */
    @Excel(name = "送样人")
    private String sendSampleUserName;

    /**
     * 样品总数量
     */
    @Excel(name = "样品总数量")
    private Long totalSampleQuantity;

    /**
     * 接收人
     */
    @Excel(name = "接收人")
    private String receiver;

    /**
     * 接收人id
     */
    @Excel(name = "接收人id")
    private String receiverId;
    @Excel(name = "收样时间")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date receiveTime;
    /**
     * 驳回原因
     */
    @Excel(name = "驳回原因")
    private String rejectReason;

    /**
     * 状态 1-待受理 2-检测中 3-检测完成 4-已审核 5-已发送 6-已驳回 7-作废
     */
    @Excel(name = "状态")
    private String status;

    /**
     * 删除id（0为未删除，删除则为id）
     */
    private String deleteId;
    /**
     * 送检单位
     */
    private String entrustDeptName;
    List<OpBloodEntrustOrderItem> opBloodEntrustOrderItemList;
    List<OpBloodEntrustOrderSample> opBloodEntrustOrderSampleList;

    //是否早孕
    private String isZaoyun;
    private String bloodTaskItemType;

    /**
     * 送样时间开始
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate sendSampleDateStart;

    /**
     * 送样时间结束
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate sendSampleDateEnd;
    /**
     * 审核人
     */
    @Excel(name = "审核人")
    private String examineUser;

    /**
     * 审核人id
     */
    @Excel(name = "审核人id")
    private String examineUserId;
    /**
     * 审核时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss") // 使用完整时间格式
    @Excel(name = "审核时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date examineTime;

    //生化项目类别
    private String biochemistryItemType;
    //报告id
    private String reportId;
    //查询条件疾病
    private String jibing;

    /** 是否退回 */
    private String isReturn;

    /** 退回时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss") // 使用完整时间格式
    @Excel(name = "退回时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date returnTime;

    /** 退回原因 */
    private String returnReason;

}
