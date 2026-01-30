package com.gmlimsqi.business.labtest.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.gmlimsqi.common.annotation.Excel;
import com.gmlimsqi.common.core.domain.BaseEntity;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 饲料样品委托单对象 op_feed_entrust_order
 * 
 * @author hhy
 * @date 2025-09-13
 */
@Data
public class OpFeedEntrustOrder extends BaseEntity implements BaseEntity.CreateAware, BaseEntity.UpdateAware
{
    private static final long serialVersionUID = 1L;

    /** id */
    private String opFeedEntrustOrderId;
    /** * 前端传参：是否提交
     * true: 保存并提交 (状态变待受理)
     * false/null: 仅保存 (状态变待提交)
     */
    private Boolean isSubmit;
    /** 送检单号 */
    @Excel(name = "送检单号")
    private String entrustOrderNo;
    //删除样品数量
    private Integer scypsl;
    /** 委托单位id */
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long entrustDeptId;
    /** 委托单位 */
    private String entrustDeptName;
    /** 报告寄送地址 */
    @Excel(name = "报告寄送地址")
    private String reportMailingAddress;

    /** 联系id */
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

    private String producerUnitName;
    private String producerUnitId;

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
    /** 收样人id */
    private String receiverId;
    /** 收样人 */
    @Excel(name = "接收人")
    private String receiverName;
    @Excel(name = "收样时间")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date receiveTime;
    /** 发票抬头 */
    private String invoiceTitle;

    /** 发票类型（1：电子发票， 2:纸质发票，3：增值税普通发票， 4：增值税专用发票） */
    private String invoiceType;

    /** 付款状态（1：已付， 2：未付） */
    private String paymentStatus;

    /** 付款方式（1：转账， 2：现金） */
    private String paymentMethod;

    /** 检测费用合计人民币 */
    private BigDecimal totalFee;

    /** 剩余样品处理（1：返还， 2：不返还） */
    @Excel(name = "剩余样品处理", readConverterExp = "1=返还,2=不返还")
    private String sampleReturnPolicy;

    /** 样品总数量 */
    private Integer totalSampleQuantity;

    /** 报告是否需判定（0：不判定， 1：判定） */
    private String requiresJudgement;

    /** 是否同意分包（0：不同意， 1：同意） */
    private String allowsSubcontracting;

    /** 报告领取方式（1：自取，2：代邮，3：邮件） */
    private String reportReceiveType;

    /** 测试服务要求（1：普通件， 2：加快件， 3：特快件， 4：特急件） */
    private String testingServiceLevel;

    /** 是否同意实验室选定的测试方法（0：不同意， 1：同意） */
    private String allowsTestMethods;

    /** 中心经办人id */
    private String operatorUserId;

    /** 中心经办人 */
    private String operatorName;
    /** 取报告人 */
    private String reportReceiver;

    /** 取报告日期 */
    private Date reportReceiveDate;

    /** 驳回原因 */
    @Excel(name = "驳回原因")
    private String rejectReason;

    /** 删除id（0为未删除，删除则为id） */
    private String deleteId;

    /** 状态 1-待受理 2-检测中 3-检测完成 4-已审核 5-已发送 6-已驳回 7-作废 */
    @Excel(name = "状态",readConverterExp=" 1=待受理, 2=检测中 3=检测完成, 4=已审核, 5=已发送, 6=已驳回, 7=作废")
    private String status;

    //样品
    private List<OpFeedEntrustOrderSample> sampleList;
    List<OpFeedEntrustOrderChangeLog> changeLogs;
    //是否接收
    private String isReceive = "0";

    /** 执行期限 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "执行期限", width = 30, dateFormat = "yyyy-MM-dd")
    private Date executionPeriod;

    /** 是否退回 */
    private String isReturn;

    /** 退回时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss") // 使用完整时间格式
    @Excel(name = "退回时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date returnTime;

    /** 退回原因 */
    private String returnReason;
    private List<OpFeedEntrustOrderSample> deletedSampleList;
}
