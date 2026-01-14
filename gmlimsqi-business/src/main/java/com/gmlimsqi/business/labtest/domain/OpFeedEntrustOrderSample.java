package com.gmlimsqi.business.labtest.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import com.gmlimsqi.common.annotation.Excel;
import com.gmlimsqi.common.core.domain.BaseEntity;

import java.util.Date;
import java.util.List;

/**
 * 饲料样品委托单-样品对象 op_feed_entrust_order_sample
 * 
 * @author hhy
 * @date 2025-09-15
 */
@Data
public class OpFeedEntrustOrderSample extends BaseEntity implements BaseEntity.CreateAware, BaseEntity.UpdateAware
{
    private static final long serialVersionUID = 1L;

    /** id */
    private String opFeedEntrustOrderSampleId;
    private List<String> opFeedEntrustOrderSampleIdList;
    /** 样品委托单主表id */
    @Excel(name = "样品委托单主表id")
    private String feedEntrustOrderId;

    /** 物料id */
    @Excel(name = "物料编码")
    private String invbillCode;
    /** 物料类别 */
    @Excel(name = "物料类别")
    private String materialName;
    /** 物料名称 */
    private String invbillName;
    //近红外报告文件id
    private String fileId;
    /** 样品名称 */
    @Excel(name = "样品名称")
    private String name;

    /** 样品型号/规格 */
    @Excel(name = "样品型号/规格")
    private String model;

    /** 样品批号 */
    @Excel(name = "样品批号")
    private String batchNo;


    /** 样品包装（1：密闭，2：散装） */
    @Excel(name = "样品包装", readConverterExp = "1=：密闭，2：散装")
    private String packaging;

    /** 样品状况（1：鲜样，2：粉末，3：块状，4：颗粒，5：液体，6：条状） */
    @Excel(name = "样品状况", readConverterExp = "1=：鲜样，2：粉末，3：块状，4：颗粒，5：液体，6：条状")
    private String status;

    /** 存储要求（1：常温，2：冷藏） */
    @Excel(name = "存储要求", readConverterExp = "1=：常温，2：冷藏")
    private String storageRequirement;

    /** 样品顺序 */
    @Excel(name = "样品顺序")
    private Integer sequence;

    /** 是否删除 */
    private String isDelete;

    /** 检测方法 */
    private String testMethod;

    /** 样品编号 */
    private String sampleNo;
    //项目对应
    private List<OpFeedEntrustOrderItem> testItem;
    //是否接收
    private String isReceive;
    //接收人id
    private String receiverId;
    //接收人
    private String receiverName;
    //接收时间
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date receiveTime;
    //报告id
    private String reportId;
    /** 生产企业 */
    @Excel(name = "生产企业")
    private String producerUnit;
    @Excel(name = "备注")
    private String sampleRemark;
    //近红外编号
    private int jhwNo;
    private String isRetest;
    private String returnReason;
    //原料样品编号
    private String materialSampleNo;
}
