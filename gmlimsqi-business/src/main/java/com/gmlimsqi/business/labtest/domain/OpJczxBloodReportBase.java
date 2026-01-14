package com.gmlimsqi.business.labtest.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.gmlimsqi.business.labtest.vo.OpBloodJHReportSampleVo;
import com.gmlimsqi.common.annotation.Excel;
import com.gmlimsqi.common.core.domain.BaseEntity;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

/**
 * 检测中心血样报告主对象 op_jczx_blood_report_base
 *
 * @author hhy
 * @date 2025-10-22
 */
@Data
public class OpJczxBloodReportBase extends BaseEntity implements BaseEntity.CreateAware, BaseEntity.UpdateAware {
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    private String opJczxBloodReportBaseId;

    /**
     * 报告编号
     */
    @Excel(name = "报告编号")
    private String reportNo;
    private Date reportDate;

    /**
     * 状态 （1：制作中，2：制作完成，3：已审核，4：已批准，5：已发送，6：作废）
     */
    @Excel(name = "状态 ", readConverterExp = "1=：制作中，2：制作完成，3：已审核，4：已批准，5：已发送，6：作废")
    private String status;

    /**
     * 委托单位
     */
    @Excel(name = "委托单位")
    private String entrustDeptName;

    /**
     * 样品名称
     */
    @Excel(name = "样品名称")
    private String sampleName;

    /**
     * 样品数量
     */
    @Excel(name = "样品数量")
    private String sampleAmount;

    /**
     * 动物类别
     */
    @Excel(name = "动物类别")
    private String animalType;

    /**
     * 接收时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "接收时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date receiveTime;

    /**
     * 检测时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "检测时间", width = 30, dateFormat = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private String testTime;

    /**
     * 样品状态
     */
    @Excel(name = "样品状态")
    private String sampleState;

    /**
     * 检测方法及依据
     */
    @Excel(name = "检测方法及依据")
    private String testBasis;

    /**
     * 检测类型
     */
    @Excel(name = "检测类型")
    private String testType;

    /**
     * 检测结果与评价
     */
    @Excel(name = "检测结果与评价")
    private String testEvaluation;

    /**
     * 仪器设备
     */
    @Excel(name = "仪器设备")
    private String insturment;

    /**
     * 样品批号
     */
    @Excel(name = "样品批号")
    private String sampleBatchNo;

    /**
     * 判定标准
     */
    @Excel(name = "判定标准")
    private String testCriterion;

    /**
     * 标准对照
     */
    @Excel(name = "标准对照")
    private String testReferStandard;

    /**
     * 样品编号
     */
    @Excel(name = "样品编号")
    private String sampleNo;

    /**
     * 样品型号/规格
     */
    @Excel(name = "样品型号/规格")
    private String sampleModel;

    /**
     * 检测结论
     */
    @Excel(name = "检测结论")
    private String conclusion;

    /**
     * 编制人
     */
    @Excel(name = "编制人")
    private String editUserId;

    /**
     * 审核人
     */
    @Excel(name = "审核人")
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
    @Excel(name = "批准人")
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
    @Excel(name = "删除id", readConverterExp = "0=为未删除，删除则为id")
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
     * 样品表id
     */
    @Excel(name = "样品表id")
    private String bloodEntrustOrderId;

    private String bloodTaskItemType;
    private String bloodTaskItemTypeNotIn;
    // 送检时间
    private String sendSampleDate;

    private OpBloodJHReportSampleVo OpBloodJHReportSampleVo;

    // 委托单号
    private String entrustOrderNo;
    private String testResult;
    // 编制人签名
    private String editSign;
    // 审核人签字
    private String checkSign;
    // 批准人签字
    private String approveSign;

    // 查询参数

    /**
     * 接收开始时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate beginReceiveTime;

    /**
     * 接收结束时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate endReceiveTime;

    /**
     * 测试开始时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate beginTestTime;

    /**
     * 测试结束时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate endTestTime;

    /**
     * 报告开始时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate beginReportTime;

    /**
     * 报告结束时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate endReportTime;

    // 查询参数
    // 报告子表是否显示A型
    private boolean aIsShow;
    // 报告子表是否显示O型
    private boolean oIsShow;
    // 编制时间
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date editTime;
    private String testDeptName;

    /**
     * pdf信息
     */
    private String pdfFileInfo;
    private String returnReason;
    // 试剂批号
    private String sjph;
    private List<String> bloodEntrustOrderIdList;

}
