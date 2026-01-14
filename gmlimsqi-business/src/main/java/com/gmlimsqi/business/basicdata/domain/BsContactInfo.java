package com.gmlimsqi.business.basicdata.domain;

import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.gmlimsqi.common.annotation.Excel;
import com.gmlimsqi.common.core.domain.BaseEntity;

import javax.validation.constraints.Email;

/**
 * 通讯方式联系人子对象 bs_contact_info
 *
 * @author wgq
 * @date 2025-09-15
 */
@Data
public class BsContactInfo extends BaseEntity implements BaseEntity.CreateAware, BaseEntity.UpdateAware
{
    private static final long serialVersionUID = 1L;

    /** 通讯方式主表id */
    @Excel(name = "通讯方式主表id")
    private String bsContactId;

    /** 通讯方式子表id */
    private String bsContactInfoId;

    /** 联系人 */
    @Excel(name = "联系人")
    private String contactPerson;

    /** 联系电话 */
    @Excel(name = "联系电话")
    private String contactPhone;

    /** 联系人邮箱 */
    @Email(message = "邮箱格式不正确")
    @Excel(name = "联系人邮箱")
    private String contactEmail;

    /** 是否接收报告 */
    @Excel(name = "是否接收报告")
    private String isReceive;

    /** 是否删除 */
    @Excel(name = "是否删除")
    private String isDelete;
    //是否默认送样人
    private String isDefaultSendSampleUser;
}
