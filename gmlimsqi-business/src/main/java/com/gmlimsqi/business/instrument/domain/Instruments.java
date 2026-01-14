package com.gmlimsqi.business.instrument.domain;

import lombok.Data;
import com.gmlimsqi.common.annotation.Excel;
import com.gmlimsqi.common.core.domain.BaseEntity;

import java.util.List;

/**
 * 设备档案对象 bs_instruments
 * 
 * @author ruoyi
 * @date 2025-08-06
 */
@Data
public class Instruments extends BaseEntity implements BaseEntity.CreateAware, BaseEntity.UpdateAware
{
    private static final long serialVersionUID = 1L;

    /** id */
    private String bsInstrumentsId;

    /** 设备名称 */
    @Excel(name = "设备名称")
    private String instrumentName;

    /** 设备编码 */
    @Excel(name = "设备编码")
    private String instrumentCode;

    /** 公司id */
    private String deptId;

    @Excel(name = "公司")
    private String deptName;
    /** 类型 */
    @Excel(name = "类型")
    private String type;

    /** 责任人id */
    @Excel(name = "责任人")
    private String responsiblePerson;

    /** 设备型号 */
    @Excel(name = "设备型号")
    private String modelNumber;

    /** 是否启用 */
    @Excel(name = "是否启用")
    private String isEnable;

    /** 是否删除 */
    private String isDelete;

    /** 安装位置 */
    @Excel(name = "安装位置")
    private String installationLocation;

    /** 用途 */
    @Excel(name = "用途")
    private String purpose;

    /** 状态，0：正常，1：维修，2：送检，3：报废 */
    @Excel(name = "状态", readConverterExp = "0=正常,1=维修,2=送检,3=报废")
    private String status;

    private List<BsInstrumentsRecord> bsInstrumentsRecordList;

}
