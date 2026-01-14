package com.gmlimsqi.business.report.mapper;

import com.gmlimsqi.business.report.domain.dto.ReportPublicHeaderDTO;
import com.gmlimsqi.business.report.domain.vo.ReportPublicHeaderVO;

/**
 * 报表
 */
public interface ReportMapper {

    /**
     * 查询报表左边的公共列
     */
    public ReportPublicHeaderVO queryReportPublicHeaderVO(ReportPublicHeaderDTO reportPublicHeaderDTO);

}
