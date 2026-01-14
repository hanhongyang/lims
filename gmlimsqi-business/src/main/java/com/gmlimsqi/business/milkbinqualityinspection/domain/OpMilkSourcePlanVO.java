package com.gmlimsqi.business.milkbinqualityinspection.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.gmlimsqi.business.inspectionmilktankers.domain.OpInspectionMilkTankers;
import com.gmlimsqi.business.leadsealsheet.domain.OpLeadSealSheet;
import com.gmlimsqi.business.milkfillingorder.domain.OpMilkFillingOrder;
import com.gmlimsqi.business.milksamplequalityinspection.domain.OpMilkSampleQualityInspection;
import com.gmlimsqi.common.annotation.Excel;
import lombok.Data;
import java.util.Date;

/**
 * 奶源计划监控VO
 */
@Data
public class OpMilkSourcePlanVO {
    /** 奶源计划单号 */
    @Excel(name = "奶源计划单号")
    private String milkSourcePlanOrderNumber;

    /** 车牌号 */
    @Excel(name = "车牌号")
    private String licensePlateNumber;

    /** 司机姓名 */
    @Excel(name = "司机姓名")
    private String driverName;

    /** 调度日期 */
    @Excel(name = "调度日期")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date schedulingDate;

    /** 奶罐车检查数据 */
    private OpInspectionMilkTankers opInspectionMilkTankers;

    /** 装奶单数据 */
    private OpMilkFillingOrder opMilkFillingOrder;

    /** 铅封单数据 */
    private OpLeadSealSheet opLeadSealSheet;

    /** 奶样质检数据 */
    private OpMilkSampleQualityInspection opMilkSampleQualityInspection;

    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
}
