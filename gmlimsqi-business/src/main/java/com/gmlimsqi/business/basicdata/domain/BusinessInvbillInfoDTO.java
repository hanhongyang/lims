package com.gmlimsqi.business.basicdata.domain;

import lombok.Data;

@Data
public class BusinessInvbillInfoDTO {

    /** 主表id */
    private String id;

    /** 子表id */
    private String invbillInfoId;

    /** sap编码 */
    private String sapCode;

    /** 主表部门id */
    private String deptId;
    private String deptName;
    /** 子表部门id */
    private String infoDeptId;

}
