package com.gmlimsqi.business.labtest.vo;

import com.gmlimsqi.business.labtest.domain.OpJczxBloodReportInfo;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 结核（牛分枝杆菌PPD）、结核抗体
 */
@Data
public class OpBloodJHReportSampleVo {
    List<OpJczxBloodReportInfo> yinList = new ArrayList<>();//阴性
    List<OpJczxBloodReportInfo> yangList = new ArrayList<>();//阳性
    List<OpJczxBloodReportInfo> ktyList = new ArrayList<>();//口蹄疫
    List<OpJczxBloodReportInfo> zaoyunList = new ArrayList<>();//早孕
    List<OpJczxBloodReportInfo> shList = new ArrayList<>();//生化
    List<OpJczxBloodReportInfo> fjhList = new ArrayList<>();//副结核

    List<OpJczxBloodReportInfo> bvdvkyList = new ArrayList<>();//BVDV抗原

}
