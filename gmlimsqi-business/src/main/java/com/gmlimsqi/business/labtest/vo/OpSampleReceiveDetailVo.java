package com.gmlimsqi.business.labtest.vo;

import com.gmlimsqi.business.labtest.domain.OpBloodEntrustOrder;
import com.gmlimsqi.business.labtest.domain.OpFeedEntrustOrder;
import com.gmlimsqi.business.labtest.domain.OpPcrEntrustOrder;
import lombok.Data;

@Data
public class OpSampleReceiveDetailVo {
    //1饲料 2血样 3pcr
    private String type;
    private OpFeedEntrustOrder feedEntrustOrder;
    private OpPcrEntrustOrder pcrEntrustOrder;
    private OpBloodEntrustVo bloodEntrustVo;
}
