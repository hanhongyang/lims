package com.gmlimsqi.business.labtest.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.gmlimsqi.business.labtest.domain.OpBloodEntrustOrderChangeLog;
import com.gmlimsqi.business.labtest.domain.OpBloodEntrustOrderSample;
import com.gmlimsqi.common.annotation.Excel;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class OpBloodEntrustVo {

    private  String OpBloodEntrustOrderId;

    /**
     * 送检单号
     */
    @Excel(name = "送检单号")
    private String entrustOrderNo;
    /**
     * 送检单位
     */
    private String entrustDeptName;
    /** * 前端传参：是否提交
     * true: 保存并提交 (状态变待受理)
     * false/null: 仅保存 (状态变待提交)
     */
    private Boolean isSubmit;
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
     * 项目编码
     */
    @Excel(name = "项目编码")
    private String itemCode;
    /**
     * 项目名称
     */
    @Excel(name = "项目名称")
    private String itemName;

    /**
     * 检测蹄疫
     */
    @Excel(name = "检测蹄疫")
    private String itemTy;

    /**
     * 免疫后的样品免疫时间
     */

    private String immunityTime;

    /**
     * 检测布病抗体
     */
    @Excel(name = "检测布病抗体")
    private String itemBbkt;
    List<OpBloodEntrustOrderSample> sampleList;
    //是否接收
    private String isReceive = "0";
    //是否早孕
    private String isZaoyun;
    private String biochemistryItemType;

    private String bloodTaskItemType;
    private String reportId;
    /**
     * 状态 1-待受理 2-检测中 3-检测完成 4-已审核 5-已发送 6-已驳回 7-作废
     */
    @Excel(name = "状态")
    private String status;
    /**
     * 是否退回
     */
    private String isReturn;

     /**
      * 退回原因
      */
    private String returnReason;
    /** 变更日志 (新增) */
    private List<OpBloodEntrustOrderChangeLog> changeLogs;

    /** 已删除的样品列表 (新增) */
    private List<OpBloodEntrustOrderSample> deletedSampleList;
}
