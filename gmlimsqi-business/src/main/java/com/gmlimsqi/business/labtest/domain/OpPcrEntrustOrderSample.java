package com.gmlimsqi.business.labtest.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.gmlimsqi.common.annotation.Excel;
import com.gmlimsqi.common.core.domain.BaseEntity;

import java.util.Date;
import java.util.List;

/**
 * pcr样品委托单-样品对象 op_pcr_entrust_order_sample
 * 
 * @author hhy
 * @date 2025-09-17
 */
@Data
public class OpPcrEntrustOrderSample extends BaseEntity implements BaseEntity.CreateAware, BaseEntity.UpdateAware
{
    private static final long serialVersionUID = 1L;

    /** id */
    private String opPcrEntrustOrderSampleId;

    /** 样品委托单主表id */
    @Excel(name = "样品委托单主表id")
    private String pcrEntrustOrderId;
    private String isSend;
    /** 物料id */
    @Excel(name = "物料编码")
    private String invbillCode;
    /** 物料名称 */
    private String invbillName;
    /** 样品名称 */
    @Excel(name = "样品名称")
    private String name;

    /** 样品顺序 */
    @Excel(name = "样品顺序")
    private Long sequence;

    /** 是否删除 */
    private String isDelete;

    /** 样品编号 */
    private String sampleNo;
    //项目对应
    private List<OpPcrEntrustOrderItem> testItem;
    //是否接收
    private String isReceive;
    //接收人id
    private String receiverId;
    //接收人
    private String receiverName;
    //接收时间
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date receiveTime;
    //检测时间/导入时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date testTime;
    //pcr检测项目类别
    private String pcrTaskItemType;
    /** 检测结果 */
    private String testResult;

    /** 检测人id */
    private String testUserId;
    /** 检测人 */
    private String testUser;
    /** 审核人 */
    @Excel(name = "审核人")
    private String examineUser;

    /** 审核人id */
    @Excel(name = "审核人id")
    private String examineUserId;
    //审核异议
    private String examineNote;
    /** 审核时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss") // 使用完整时间格式
    @Excel(name = "审核时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date examineTime;


}
