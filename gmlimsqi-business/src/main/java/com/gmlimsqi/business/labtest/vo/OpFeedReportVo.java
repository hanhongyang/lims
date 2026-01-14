package com.gmlimsqi.business.labtest.vo;

import lombok.Data;

import java.util.List;

/**
 * 饲养报告Vo
 */
@Data
public class OpFeedReportVo {
    private OpFeedReportBaseVo feedReportBase;
    private List<OpFeedReportInfoVo> opJczxFeedReportInfoList;
}
