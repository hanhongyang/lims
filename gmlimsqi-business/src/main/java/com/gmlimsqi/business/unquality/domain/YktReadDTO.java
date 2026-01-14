package com.gmlimsqi.business.unquality.domain;

import lombok.Data;

/**
 * 读取样品不合格处理单
 */
@Data
public class YktReadDTO {

    /** 签到id */
    private String signId;

    /** sap编码 */
    private String sapName;

    /** 物料编码 */
    private String materialCode;

    /** 物料名称 */
    private String materialName;
}
