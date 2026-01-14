package com.gmlimsqi.business.labtest.vo;

import com.gmlimsqi.business.labtest.domain.OpFeedEntrustOrderSample;
import com.gmlimsqi.business.labtest.domain.OpJczxFeedReportBase;
import lombok.Data;

import java.util.List;

/**
 * 发送报告弹窗初始化信息 VO
 */
@Data
public class ReportSendInfoVo {
    /**
     * 邮箱列表
     */
    private List<ReportEmailVo> emailList;

    /**
     * 样品列表
     */
    private List<OpJczxFeedReportBase> baseList;
}