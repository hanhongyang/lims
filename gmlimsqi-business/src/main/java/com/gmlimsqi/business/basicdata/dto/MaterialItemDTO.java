package com.gmlimsqi.business.basicdata.dto;

import lombok.Data;

/**
 * 根据物料查询物料检测项目
 */
@Data
public class MaterialItemDTO {

    /**
     * 物料编码
     */
    private String invbillCode;

     /**
      * 检测项目名称
      */
    private String itemName;

    /**
     * 检测项目主键
     */
    private String itemId;

    /**
     * 部门主键
     */
    private String deptId;
}
