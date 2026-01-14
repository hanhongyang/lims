package com.gmlimsqi.business.basicdata.dto;

import com.gmlimsqi.business.basicdata.domain.BsContactInfo;
import com.gmlimsqi.common.annotation.Excel;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.List;
@Data
public class BsContactDto {
    /** 通讯信息表id */
    private String id;

    /** 公司id */
    @Excel(name = "公司id")
    @NotBlank(message = "公司id不能为空")
    private String deptId;

    /** 是否删除（0否1是） */
    @Excel(name = "是否删除", readConverterExp = "0=否1是")
    private String isDelete;

    /** 是否启用（0否1是） */
    @Excel(name = "是否启用", readConverterExp = "0=否1是")
    @NotBlank(message = "是否启用不能为空")
    private String isEnable;

    /** 公司名称 */
    @Excel(name = "公司名称")
    @NotBlank(message = "公司名称不能为空")
    private String deptName;

    /** 邮箱 */
    @Excel(name = "邮箱")
    @NotBlank(message = "邮箱不能为空")
    @Email(message = "邮箱格式不正确")
    private String email;

    private String remark;


    private List<BsContactInfo> bsContactInfoList;
}
