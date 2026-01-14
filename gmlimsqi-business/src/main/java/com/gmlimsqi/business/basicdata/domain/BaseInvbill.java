package com.gmlimsqi.business.basicdata.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.gmlimsqi.common.annotation.Excel;
import lombok.Data;

import java.util.Date;

@Data
public class BaseInvbill {
    
    private String id;
    
    private String sapCode;
    
    private String invbillName;
    
    private String ikz;
    private String isUploadReport;
    private String izj;
    private String cclasscode;
    /** 部门sap编码 */
    private String deptId;
    /** 部门id */
    //private String ownDeptId;
    private String deptName;
    private String createBy;

    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /** 更新者 */
    private String updateBy;
    //标签
    private String tag;
    /** 更新时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    /** 是否为sap物料，0：sap物料，1：手动新增*/
    private String isSapType;

    /** 样品状况 */
    private String sampleState;

     /** 物料编码 */
    private String invbillCode;
    /** 物料类别 */
    private String materialType;

    private String materialTypeNotEmpty;

}
