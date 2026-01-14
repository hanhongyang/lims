package com.gmlimsqi.business.disinfection.controller.vo;

import com.gmlimsqi.business.disinfection.domain.DisinfectionPool;
import com.gmlimsqi.common.annotation.Excel;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 消毒池管理响应Vo Response
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class DisinfectionRespVo extends DisinfectionPool {

    // 部门名称
    @Excel(name = "所属部门")
    private String deptName;

}
