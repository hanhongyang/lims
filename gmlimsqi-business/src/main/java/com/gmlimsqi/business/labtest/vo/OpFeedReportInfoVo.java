package com.gmlimsqi.business.labtest.vo;

import lombok.Data;

@Data
public class OpFeedReportInfoVo {
    private String itemName;//检测项目
    private String itemId;//检测项目
    private String unit;//单位
    private String sapecification;//技术要求
    private String valueOfTest;//实测值
    private String standard;


    private String sampleNo;
    private String entrustOrderItemId;

}
