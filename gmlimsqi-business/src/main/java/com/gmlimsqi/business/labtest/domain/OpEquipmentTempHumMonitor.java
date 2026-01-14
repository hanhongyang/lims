package com.gmlimsqi.business.labtest.domain;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import com.gmlimsqi.common.annotation.Excel;
import com.gmlimsqi.common.core.domain.BaseEntity;

/**
 * 设备温湿度监控对象 op_equipment_temp_hum_monitor
 * 
 * @author hhy
 * @date 2025-10-26
 */
@Data
public class OpEquipmentTempHumMonitor extends BaseEntity implements BaseEntity.CreateAware, BaseEntity.UpdateAware
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private String id;

    /** 公司名称 */
    @Excel(name = "公司名称")
    private String companyName;

    /** 监控点 */
    @Excel(name = "监控点")
    private String monitoringPoint;

    /** 温度 */
    @Excel(name = "温度")
    private String temperature;

    /** 湿度 */
    @Excel(name = "湿度")
    private String humidity;

    /** 是否启用，0：启用，1停用 */
    @Excel(name = "是否启用，0：启用，1停用")
    private String isEnable;

    /** 部门id */
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private String deptId;

    /** 设备温湿度修改记录信息 */
    private List<OpDeviceTempHumRecord> opDeviceTempHumRecordList;

}
