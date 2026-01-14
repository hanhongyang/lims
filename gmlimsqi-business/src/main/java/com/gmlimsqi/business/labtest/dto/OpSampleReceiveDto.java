package com.gmlimsqi.business.labtest.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.gmlimsqi.common.annotation.Excel;
import lombok.Data;

import java.util.Date;

@Data
public class OpSampleReceiveDto {
    private static final long serialVersionUID = 1L;

    /** 样品子表id */
    private String[] sampleIds;
    /** 送检单号 */
    private String entrustOrderNo;
    //1饲料 2疾病 3pcr 4早孕 5生化
    private String type;
    //状态"," 1=待受理, 2=检测中 3=检测完成, 4=已审核, 5=已发送, 6=已驳回, 7=作废")
    private String status;
    private String statusNotIn;
    //是否接收 0否1是
    private String isReceive;
    //委托单位
    private String deptName;
    //"送检时间范围 - 开始"
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date sendSampleDateStart;
    //"送检时间范围 - 结束"
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date sendSampleDateEnd;
}
