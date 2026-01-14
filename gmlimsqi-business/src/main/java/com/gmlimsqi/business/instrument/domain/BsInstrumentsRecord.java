package com.gmlimsqi.business.instrument.domain;

import lombok.Data;
import com.gmlimsqi.common.annotation.Excel;
import com.gmlimsqi.common.core.domain.BaseEntity;

/**
 * 设备档案记录对象 bs_instruments_record
 * 
 * @author hhy
 * @date 2025-11-17
 */
@Data
public class BsInstrumentsRecord extends BaseEntity implements BaseEntity.CreateAware, BaseEntity.UpdateAware
{
    private static final long serialVersionUID = 1L;

    /** 设备档案记录表主键 */
    private String bsInstrumentsRecordId;

    /** 设备档案主表 */
    @Excel(name = "设备档案主表")
    private String bsInstrumentsId;

    /** 设备名称 */
    @Excel(name = "设备名称")
    private String instrumentName;

    /** 设备编码 */
    @Excel(name = "设备编码")
    private String instrumentCode;

    /** 公司id */
    @Excel(name = "公司id")
    private String deptId;

    /** 类型 */
    @Excel(name = "类型")
    private String type;

    /** 设备型号(规格型号) */
    @Excel(name = "设备型号(规格型号)")
    private String modelNumber;

    /** 状态 */
    @Excel(name = "状态")
    private String status;

    /** 是否启用 */
    @Excel(name = "是否启用")
    private String isEnable;

    /** 是否删除 */
    @Excel(name = "是否删除")
    private String isDelete;

}
