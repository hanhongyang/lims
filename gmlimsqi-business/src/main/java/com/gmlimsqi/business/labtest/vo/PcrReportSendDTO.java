package com.gmlimsqi.business.labtest.vo;

import com.gmlimsqi.business.labtest.domain.OpPcrEntrustOrder;
import lombok.Data;

import java.util.List;
@Data
public class PcrReportSendDTO {
    private List<OpPcrEntrustOrder> opJczxPcrReportBase;
    private String emails;
}
