package com.gmlimsqi.business.basicdata.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class LabtestItemsVO {
    private String LabtestItemsId;

    /** 项目名称 */
    private String itemName;

    /** 项目编码 */
    private String itemCode;

    /** sap项目编码 */
    private String sapCode;


    /** 是否启用（0否1是） */
    private String isEnable;
    /** 创建者 */
    private String createBy;

    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /** 更新者 */
    private String updateBy;

    /** 更新时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;
}
