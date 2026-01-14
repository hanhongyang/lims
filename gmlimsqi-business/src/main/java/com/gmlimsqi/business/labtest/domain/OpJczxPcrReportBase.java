package com.gmlimsqi.business.labtest.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.gmlimsqi.business.labtest.vo.OpPcrReportItemListVo;
import com.gmlimsqi.common.annotation.Excel;
import com.gmlimsqi.common.core.domain.BaseEntity;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * pcr报告主对象 op_jczx_pcr_report_base
 *
 * @author hhy
 * @date 2025-10-21
 */
@Data
public class OpJczxPcrReportBase extends BaseEntity implements BaseEntity.CreateAware, BaseEntity.UpdateAware
{
    private static final long serialVersionUID = 1L;

    /** id */
    private String opJczxPcrReportBaseId;

    /** 委托单表id */
    @Excel(name = "委托单表id")
    private String pcrEntrustOrderId;

    /** 报告编号 */
    @Excel(name = "报告编号")
    private String reportNo;

    /** 委托单编号 */
    @Excel(name = "委托单编号")
    private String orderNo;

    /** 状态 （1：制作中，2：制作完成，3：已审核，4：已批准，5：已发送，6：作废） */
    @Excel(name = "状态 ", readConverterExp = "1=：制作中，2：制作完成，3：已审核，4：已批准，5：已发送，6：作废")
    private String status;

    /** 委托单位 */
    @Excel(name = "委托单位")
    private String entrustDeptName;

    /** 物料 */
    @Excel(name = "物料")
    private String invbillName;

    /** 样品数量 */
    @Excel(name = "样品数量")
    private String sampleAmount;

    /** 接收时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "接收时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date receiveTime;

    /** 检测时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "检测时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date testTime;

    /** 提取试剂盒批号 */
    @Excel(name = "提取试剂盒批号")
    private String tqsjh;

    /** 扩增试剂盒批号 */
    @Excel(name = "扩增试剂盒批号")
    private String kzsjh;

    /** 检测结论 */
    @Excel(name = "检测结论")
    private String conclusion;

    /** 编制人 */
    @Excel(name = "编制人")
    private String editUserId;

    /** 审核人 */
    @Excel(name = "审核人")
    private String checkUserId;

    /** 审核时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "审核时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date checkTime;

    /** 批准人 */
    @Excel(name = "批准人")
    private String approveUserId;

    /** 批准时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "批准时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date approveTime;

    /** 删除id（0为未删除，删除则为id） */
    @Excel(name = "删除id", readConverterExp = "0=为未删除，删除则为id")
    private String deleteId;

    /** 编制人 */
    @Excel(name = "编制人")
    private String editUser;

    /** 审核人 */
    @Excel(name = "审核人")
    private String checkUser;

    /** 批准人 */
    @Excel(name = "批准人")
    private String approveUser;

    /** 检测地点 */
    @Excel(name = "检测地点")
    private String testLocation;

    /** 检测项目 */
    @Excel(name = "检测项目")
    private String itemName;

//    private List<OpJczxPcrReportInfo> pcrReportInfoList;
    private List<OpPcrReportItemListVo> pcrReportItemListVoList;




}
