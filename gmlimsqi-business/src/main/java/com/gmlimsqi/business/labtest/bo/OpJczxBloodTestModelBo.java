package com.gmlimsqi.business.labtest.bo;

import com.gmlimsqi.common.annotation.Excel;
import lombok.Data;

@Data
public class OpJczxBloodTestModelBo {

    private String entrustOrderId;
    private String entrustOrderSampleId;
    //所属牧场
    private String entrustDeptName;
    /** 样品编号 */
    private String sampleNo;
    /** 牛号 */
    private String sampleName;
    @Excel(name = "母牛号")
    private String mnh;
    /** 管号 */
    private String gh;
    /** 序号 */
    private int sequence;
    private String itemName;
    private String reamark;
    private String sjh;
    //pcr检测项目类别
    private String bloodTaskItemType;
    private String remark;
    private String sp;
}
