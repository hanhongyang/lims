package com.gmlimsqi.business.basicdata.dto;

import com.gmlimsqi.business.basicdata.domain.BsInvbillItemStandard;
import lombok.Data;
import java.util.List;

/**
 * 批量添加物料项目标准DTO
 */
@Data
public class BatchAddStandardDto {

    /** 物料ID */
    private String invbillId;
    private String invbillCode;

    /** 是否启用 */
    private String isEnable;
    private String deptId;
    private List<BsInvbillItemStandard> standardList; // 直接传递标准对象列表

}