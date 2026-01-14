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
 * 外部委托检测单对象 op_outentrust_register
 *
 * @author wgq
 * @date 2025-09-17
 */
@Data
public class OpOutentrustRegister extends BaseEntity implements BaseEntity.CreateAware, BaseEntity.UpdateAware
{
    private static final long serialVersionUID = 1L;

    /** 通讯信息表id */
    private String id;

    /** 公司id */
    @Excel(name = "公司id")
    private String deptId;

    /** 是否删除（0否1是） */
    @Excel(name = "是否删除", readConverterExp = "0=否1是")
    private String isDelete;

    /** 是否启用（0否1是） */
    @Excel(name = "是否启用", readConverterExp = "0=否1是")
    private String isEnable;

    /** 公司名称 */
    @Excel(name = "公司名称")
    private String deptName;

    /** 送检单号 */
    @Excel(name = "送检单号")
    private String entrustOrderNo;

    /** 送检时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "送检时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date entrustTime;

    /** 样品数量 */
    @Excel(name = "样品数量")
    private Long sampleQuantity;

    /** 委托化验室 */
    @Excel(name = "委托化验室")
    private String entrustLab;

    /** 报告地址 */
    @Excel(name = "报告地址")
    private String fileAddres;

    /** 外部委托检测单样品子信息 */
    private List<OpOutentrustRegisterSample> opOutentrustRegisterSampleList;

    /** 送样时间开始 */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate sendSampleDateStart;

     /** 送样时间结束 */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate sendSampleDateEnd;

}
