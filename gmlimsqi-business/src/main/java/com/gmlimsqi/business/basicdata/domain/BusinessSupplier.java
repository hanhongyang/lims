package com.gmlimsqi.business.basicdata.domain;

import com.gmlimsqi.common.annotation.Excel;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.gmlimsqi.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Date;

/**
 * 供应商对象 business_supplier
 *
 * @author egap
 * @date 2023-04-13
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class BusinessSupplier extends BaseEntity implements BaseEntity.CreateAware, BaseEntity.UpdateAware{
    private static final long serialVersionUID = 1L;

    private String id;

    private Long userId;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long deptId;
    /** 编号 */
    @Excel(name = "编号")
    private String sapCode;
    
    /** 名称 */
    @Excel(name = "名称")
    private String name;
    
    /** 简称 */
    @Excel(name = "简称")
    private String abbreviation;
    
    /** 联系人 */
    @Excel(name = "联系人")
    private String linkman;
    
    /** 邮箱 */
    @Excel(name = "邮箱")
    private String mailbox;
    
    /** 公司电话 */
    @Excel(name = "公司电话")
    private String companyTel;
    
    /** 联系电话 */
    @Excel(name = "联系电话")
    private String phone;
    
    /** 银行行号 */
    @Excel(name = "银行行号")
    private String bankCard;
    
    /** 银行名称 */
    @Excel(name = "银行名称")
    private String bankName;
    
    /** 银行账号 */
    @Excel(name = "银行账号")
    private String bankAccount;
    
    /** 税号 */
    @Excel(name = "税号")
    private String dutyParagraph;
    
    /** 经度 */
    @Excel(name = "经度")
    private String longitude;
    
    /** 纬度 */
    @Excel(name = "纬度")
    private String latitude;
    
    /** 签到半径 */
    @Excel(name = "签到半径")
    private Integer signInRadius;
    
    /** 是否停用 */
    @Excel(name = "是否停用")
    private String ifDiscontinue;
    
    /** 停用人 */
    @Excel(name = "停用人")
    private String discontinueUser;
    
    /** 停用时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "停用时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date discontinueTime;
    
    /** 停用原因 */
    @Excel(name = "停用原因")
    @Deprecated
    private String discontinueCause;
    
    /** 是否开通账号 */
    @Excel(name = "是否开通账号")
//    @Deprecated
    private String ifAccount;
    
    /** 关联用户名称 */
    @Excel(name = "关联用户名称")
    @Deprecated
    private String userNickname;
    
    private String type;

    /** 是否为承运商 */
    private String cifcarrier;
    
    /** 是否删除 */
    private String delFlag;
    

}
