package com.gmlimsqi.business.basicdata.domain;

import com.gmlimsqi.common.annotation.Excel;
import com.gmlimsqi.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 物料档案对象 business_invbill
 *
 * @author egap
 * @date 2024-12-16
 */
@Data
@Accessors(chain = true)
public class BusinessInvbill extends BaseEntity {
    private static final long serialVersionUID = 1L;
    
    /**  */
    private String id;
    
    /** sap编码 */
    @Excel(name = "sap编码")
    private String sapCode;
    
    /** 物料名称 */
    @Excel(name = "物料名称")
    private String invbillName;
    
    /** 1删除 */
    private String delFlag;
    
    /** 部门sap编码 */
    @Excel(name = "部门sap编码")
    private String deptId;
    /** 使用部门 */
    private List<BsInvbillDept> invbillDepts;
    /** 分类编号 */
    @Excel(name = "分类编号")
    private String cclasscode;
    
    /** 分类名称 */
    @Excel(name = "分类名称")
    private String cclassname;
    
    /** 备注 */
    @Excel(name = "备注")
    private String cremark;
    
    /** 是否扣重 */
    @Excel(name = "是否扣重")
    private String ikz;

    /** 是否上传随车报告 */
    @Excel(name = "是否上传随车报告")
    private String isUploadReport;

    /**
     * 是否跳过质检  0否 1是
     */
    private String izj;
    //标签
    private String tag;
    /** 物料子表 */
    private List<BusinessInvbillInfo> businessInvbillInfoList;

    /** 是否为sap物料，0：sap物料，1：手动新增*/
    private String isSapType;

    /** 样品状况 */
    private String sampleState;

    /** 物料编码 */
    private String invbillCode;

    /** 物料类型 */
    private String materialType;

}
