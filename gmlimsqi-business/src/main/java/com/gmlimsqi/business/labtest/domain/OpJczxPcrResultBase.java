package com.gmlimsqi.business.labtest.domain;

import java.util.List;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.gmlimsqi.common.annotation.Excel;
import com.gmlimsqi.common.core.domain.BaseEntity;

/**
 * 样品化验PCR对象 op_jczx_pcr_result_base
 * 
 * @author hhy
 * @date 2025-10-13
 */
@Data
public class OpJczxPcrResultBase extends BaseEntity implements BaseEntity.CreateAware, BaseEntity.UpdateAware
{
    private static final long serialVersionUID = 1L;

    /** id */
    private String opJczxPcrResultBaseId;

    /** 化验单号 */
    @Excel(name = "化验单号")
    private String resultNo;

    /** pcr检测项目类别 */
    private String pcrTaskItemType;

    /** 检测人id */
    @Excel(name = "检测人id")
    private String testUserId;
    /** 检测人 */
    @Excel(name = "检测人")
    private String testUser;

    @Excel(name = "审核人id")
    private String examineUserId;
    @Excel(name = "审核人")
    private String examineUser;
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "审核日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date examineTime;


    /** 状态 1=开始化验,2=化验完成,3=审核完成,4=作废 */
    @Excel(name = "状态 1=开始化验,2=化验完成,3=审核完成,4=作废")
    private String status;

    /** 检测日期 */
    @JsonFormat(pattern = "yyyy-MM-dd  HH:mm:ss")
    @Excel(name = "检测日期", width = 30, dateFormat = "yyyy-MM-dd  HH:mm:ss")
    private Date testDate;

    /** 检测完成时间 */
    @JsonFormat(pattern = "yyyy-MM-dd  HH:mm:ss")
    @Excel(name = "检测完成时间", width = 30, dateFormat = "yyyy-MM-dd  HH:mm:ss")
    private Date testEndTime;
    //导出文件名
    private String exportFileName;
    /** 上传文件id */
    @Excel(name = "上传文件id")
    private String fileId;

    /** 删除id（0为未删除，删除则为id） */
    private String deleteId;

    /** 检测中心pce化验单子信息 */
    private List<OpJczxPcrResultInfo> opJczxPcrResultInfoList;

    /** 审核通过标志（1通过，0不通过） */
    private String examinePassFlag;
    //8联模板类型 1中文2英文
    private String blTemplateType;
}
