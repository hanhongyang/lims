package com.gmlimsqi.business.labtest.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.gmlimsqi.common.core.domain.BaseEntity;
import lombok.Data;
import com.gmlimsqi.common.annotation.Excel;

/**
 * 设备温湿度修改记录对象 op_device_temp_hum_record
 * 
 * @author hhy
 * @date 2025-10-26
 */
@Data
public class OpDeviceTempHumRecord extends BaseEntity implements BaseEntity.CreateAware, BaseEntity.UpdateAware
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private String id;

    /** 设备温湿度监控表主键 */
    @Excel(name = "设备温湿度监控表主键")
    private String parentId;

    /** 公司名称-修改前 */
    @Excel(name = "公司名称-修改前")
    private String companyName;

    /** 公司名称-修改后 */
    @Excel(name = "公司名称-修改后")
    private String companyNameNew;

    /** 监控点-修改前 */
    @Excel(name = "监控点-修改前")
    private String monitoringPoint;

    /** 监控点-修改后 */
    @Excel(name = "监控点-修改后")
    private String monitoringPointNew;

    /** 温度-修改前 */
    @Excel(name = "温度-修改前")
    private String temperature;

    /** 温度-修改后 */
    @Excel(name = "温度-修改后")
    private String temperatureNew;

    /** 湿度-修改前 */
    @Excel(name = "湿度-修改前")
    private String humidity;

    /** 湿度-修改后 */
    @Excel(name = "湿度-修改后")
    private String humidityNew;

    /** 是否启用，0：启用，1停用-修改前 */
    @Excel(name = "是否启用，0：启用，1停用-修改前")
    private String isEnable;

    /** 是否启用，0：启用，1停用-修改后 */
    @Excel(name = "是否启用，0：启用，1停用-修改后")
    private String isEnableNew;

    /** 部门id-修改前 */
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private String deptId;

    /** 部门id-修改后 */
    private String deptIdNew;

}
