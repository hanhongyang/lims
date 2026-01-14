package com.gmlimsqi.business.basicdata.domain;

import lombok.Data;

/**
 * 奶车信息-铅封信息
 */
@Data
public class BsMilkCartInfoLeadSeal {

    /** 子表id */
    private String milkCartInfoLeadSealId;

    /** 主表id */
    private String milkCartInfoId;

    /** 铅封号 */
    private String cartLeadSealNumber;

    /** 灌口 */
    private String fillingPort;

}
