package com.gmlimsqi.business.basicdata.domain;

import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.gmlimsqi.common.annotation.Excel;
import com.gmlimsqi.common.core.domain.BaseEntity;

/**
 * 物料子对象 bs_invbill_info
 * 
 * @author hhy
 * @date 2025-09-29
 */
@Data
public class BsInvbillInfo extends BaseEntity implements BaseEntity.CreateAware, BaseEntity.UpdateAware
{
    private static final long serialVersionUID = 1L;

    /** id */
    private String bsInvbillInfoId;

    /** sap编号 */
    @Excel(name = "sap编号")
    private String sapCode;

    /** 是否上传随车报告 */
    @Excel(name = "是否上传随车报告")
    private String isUploadReport;

    /** 标签 */
    @Excel(name = "标签")
    private String tag;

    /** 删除id（0为未删除，删除则为id） */
    private String deleteId;

}
