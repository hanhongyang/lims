package com.gmlimsqi.business.labtest.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.gmlimsqi.common.annotation.Excel;
import lombok.Data;

import java.util.Date;

@Data
public class OpSampleTestProgressVo {

    private static final long serialVersionUID = 1L;
    /** 送检单号 */
    @Excel(name = "委托单号")
    private String entrustOrderNo;
    /** 样品名称 */
    @Excel(name = "样品名称")
    private String sampleName;
    @Excel(name = "样品编号")
    private String sampleNo;
    @Excel(name = "已化验项目")
    private String startedItems;
    @Excel(name = "未化验项目")
    private String notStartedItems;

    @Excel(name = "委托公司")
    private String deptName;

    @Excel(name = "收样时间")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date receiveTime;

    private String testMethod;
    private String bloodTaskItemType;
    //化验进度
    @Excel(name = "化验进度")
    private String progress;
}
