package com.gmlimsqi.business.labtest.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

/**
 * PCR报告Excel生成结果（包含MultipartFile）
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PcrReportExcelResult {

    /**
     * 委托单ID
     */
    private String opPcrEntrustOrderId;

    /**
     * Excel文件的MultipartFile对象
     */
    private MultipartFile excelFile;

}