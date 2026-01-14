package com.gmlimsqi.business.labtest.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.gmlimsqi.business.labtest.domain.OpJczxBloodResultInfo;
import com.gmlimsqi.common.annotation.Excel;
import com.gmlimsqi.common.core.domain.BaseEntity;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 检测中心血样化验单主对象 op_jczx_blood_result_base
 *
 * @author hhy
 * @date 2025-10-14
 */
@Data
public class OpJczxBloodResultBase extends BaseEntity implements BaseEntity.CreateAware, BaseEntity.UpdateAware
{
    private static final long serialVersionUID = 1L;

    /** id */
    private String opJczxBloodResultBaseId;

    /** 化验单号 */
    @Excel(name = "化验单号")
    private String resultNo;

    /** pcr检测项目类别 */
    @Excel(name = "pcr检测项目类别")
    private String bloodTaskItemType;

    /** 检测人 */
    @Excel(name = "检测人")
    private String testUser;

    /** 检测人id */
    @Excel(name = "检测人id")
    private String testUserId;



    /** 状态 1=开始化验,2=化验完成,3=审核完成,4=作废 */
    @Excel(name = "状态 1=开始化验,2=化验完成,3=审核完成,4=作废")
    private String status;

    /** 检测日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "检测日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date testDate;

    /** 检测完成时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "检测完成时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date testEndTime;

    /** 上传文件id */
    @Excel(name = "上传文件id")
    private String fileId;

    /** 删除id（0为未删除，删除则为id） */
    @Excel(name = "删除id", readConverterExp = "0=为未删除，删除则为id")
    private String deleteId;

    /** 审核人 */
    @Excel(name = "审核人")
    private String examineUser;

    /** 审核人id */
    @Excel(name = "审核人id")
    private String examineUserId;
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "审核日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date examineTime;
    /** 检测中心血样化验单子信息 */
    private List<OpJczxBloodResultInfo> opJczxBloodResultInfoList;

    /** 审核通过标志（1通过，0不通过） */
    private String examinePassFlag;
    //导出文件名
    private String exportFileName;

}
