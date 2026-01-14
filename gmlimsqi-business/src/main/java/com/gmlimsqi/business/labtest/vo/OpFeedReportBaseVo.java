package com.gmlimsqi.business.labtest.vo;

import lombok.Data;

import java.util.Date;

/**
 * 报告主表Vo
 */
@Data
public class OpFeedReportBaseVo {
    private String sampleNo;//样品编号
    private String reportNo;//报告编号
    private String sampleName;//样品名称
    private String sampleModel;//样品规格
    private String sampleState ;//样品状态
    private String trademark;//商标
    private String sampleAmount;//样品数量
    private String entrustDeptName;//委托单位
    private String address;//委托方地址
    private String sampleSource;//样品来源
    private String sampleBatchNo ;//生产日期或批号
    private String receiveTime;//接收日期
    private String testType;//检测类型
    private String testTime;//检测日期
    private String testLocation;//检测地点
    private String itemName;//检测项目
    private String mainInstrument;//所用主要仪器
    private String conclusion;//检测结论
    private String remark ;//备注
    private String opFeedEntrustOrderSampleId ;//
    private String opJczxFeedReportBaseId;
    private String entrustDeptId;
    private String editSign;//编制签名
    private String checkSign;//审核签名
    private String approveSign;//校准签名
    private Date issuanceTime;//签发时间
    private String feedEntrustOrderId;





}
