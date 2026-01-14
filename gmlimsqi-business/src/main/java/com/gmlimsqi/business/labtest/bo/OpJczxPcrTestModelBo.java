package com.gmlimsqi.business.labtest.bo;

import com.gmlimsqi.common.annotation.Excel;
import lombok.Data;

@Data
public class OpJczxPcrTestModelBo {
    private String entrustOrderNo;
    private String entrustOrderSampleId;
    //所属牧场
    private String entrustDeptName;
    /** 样品编号 */
    private String sampleNo;
    /** 物料名称 */
    private String invbillName;
    /** 样品名称 */
    private String sampleName;
    /** 序号 */
    private int sequence;
    private String itemName;
    private String remark;
    //提取试剂盒批号
    private String tqsjh;
    //扩增试剂盒批号
    private String kzsjh;
    //pcr检测项目类别
    private String pcrTaskItemType;
    private String entrustOrderItemIds;
}
