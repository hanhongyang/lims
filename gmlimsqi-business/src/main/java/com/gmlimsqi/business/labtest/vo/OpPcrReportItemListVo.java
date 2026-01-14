package com.gmlimsqi.business.labtest.vo;

import com.gmlimsqi.business.labtest.domain.OpPcrEntrustOrderItem;
import lombok.Data;

import java.util.List;

@Data
public class OpPcrReportItemListVo {
 private String itemType;
 private List<OpPcrEntrustOrderItem> opPcrEntrustOrderItemList;
}
