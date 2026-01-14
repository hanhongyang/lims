package com.gmlimsqi.business.labtest.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.gmlimsqi.business.labtest.domain.OpFeedEntrustOrderItem;
import com.gmlimsqi.common.annotation.Excel;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class OpFeedSampleReceiveVo {

    private static final long serialVersionUID = 1L;




    /** 送检单号 */
    @Excel(name = "送检单号")
    private String entrustOrderNo;

    //1饲料 2血样 3pcr
    private String type = "1";

    @Excel(name = "状态",readConverterExp=" 1=待受理, 2=检测中 3=检测完成, 4=已审核, 5=已发送, 6=已驳回, 7=作废")
    private String OrderStatus;
    /** 样品名称 */

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





    /** id */
    private String opFeedEntrustOrderSampleId;

    /** 样品委托单主表id */
    @Excel(name = "样品委托单主表id")
    private String feedEntrustOrderId;

    /** 物料id */
    @Excel(name = "物料id")
    private String invillId;
    /** 物料名称 */
    private String invbillName;

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

    private String testMethod;

    /** 样品编号 */
    private String sampleNo;
    //项目对应
    private List<OpFeedEntrustOrderItem> testItem;

}
