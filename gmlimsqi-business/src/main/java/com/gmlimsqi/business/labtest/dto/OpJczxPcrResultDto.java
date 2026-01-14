package com.gmlimsqi.business.labtest.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.gmlimsqi.business.labtest.domain.OpJczxPcrResultInfo;
import com.gmlimsqi.business.labtest.domain.OpPcrEntrustOrderItem;
import com.gmlimsqi.common.annotation.Excel;
import com.gmlimsqi.common.core.domain.BaseEntity;
import lombok.Data;

import java.util.Date;
import java.util.List;


@Data
public class OpJczxPcrResultDto {
    private static final long serialVersionUID = 1L;

    private String entrustOrderNo;

    /** pcr检测项目类别 */
    private String pcrTaskItemType;

    /** 检测人id */
    private String testUserId;
    /** 检测人 */
    private String testUser;

    private String examineUserId;
    private String examineUser;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date examineTime;


    /** 状态 1=开始化验,2=化验完成,3=审核完成,4=作废 */
    private String status;

    /** 检测日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date testDate;

    /** 检测完成时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date testEndTime;

    private String fileId;
    private List<OpPcrEntrustOrderItem> testItem;
    private String resultNo;
    private String examinePassFlag;
}
