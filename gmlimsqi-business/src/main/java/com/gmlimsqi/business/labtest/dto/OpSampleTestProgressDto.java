package com.gmlimsqi.business.labtest.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class OpSampleTestProgressDto {
    private static final long serialVersionUID = 1L;


    /** 送检单号 */
    private String entrustOrderNo;
    //1饲料 2血样 3pcr
    private String type;
    private String sampleNo;
    private String sampleName;
    //状态"," 1化验完成、2未化验、3化验中")
    private String progress;

    //委托单位
    private String deptName;

    //"收样时间范围 - 开始"
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date beginReceiveTime;
    //"收样时间范围 - 结束"
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date endReceiveTime;
}
