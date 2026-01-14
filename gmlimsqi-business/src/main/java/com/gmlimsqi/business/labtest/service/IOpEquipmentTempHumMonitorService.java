package com.gmlimsqi.business.labtest.service;

import com.gmlimsqi.business.labtest.domain.OpDeviceTempHumRecord;
import com.gmlimsqi.business.labtest.domain.OpEquipmentTempHumMonitor;

import java.util.List;

/**
 * 设备温湿度监控Service接口
 * 
 * @author hhy
 * @date 2025-10-26
 */
public interface IOpEquipmentTempHumMonitorService 
{
    /**
     * 查询设备温湿度监控
     * 
     * @param id 设备温湿度监控主键
     * @return 设备温湿度监控
     */
    public OpEquipmentTempHumMonitor selectOpEquipmentTempHumMonitorById(String id);

    /**
     * 查询设备温湿度监控列表
     * 
     * @param opEquipmentTempHumMonitor 设备温湿度监控
     * @return 设备温湿度监控集合
     */
    public List<OpEquipmentTempHumMonitor> selectOpEquipmentTempHumMonitorList(OpEquipmentTempHumMonitor opEquipmentTempHumMonitor);

    /**
     * 新增设备温湿度监控
     * 
     * @param opEquipmentTempHumMonitor 设备温湿度监控
     * @return 结果
     */
    public int insertOpEquipmentTempHumMonitor(OpEquipmentTempHumMonitor opEquipmentTempHumMonitor);

    /**
     * 修改设备温湿度监控
     * 
     * @param opEquipmentTempHumMonitor 设备温湿度监控
     * @return 结果
     */
    public int updateOpEquipmentTempHumMonitor(OpEquipmentTempHumMonitor opEquipmentTempHumMonitor);

    /**
     * 批量删除设备温湿度监控
     * 
     * @param ids 需要删除的设备温湿度监控主键集合
     * @return 结果
     */
    public int deleteOpEquipmentTempHumMonitorByIds(String[] ids);

    /**
     * 删除设备温湿度监控信息
     * 
     * @param id 设备温湿度监控主键
     * @return 结果
     */
    public int deleteOpEquipmentTempHumMonitorById(String id);

     /**
     * 查询设备温湿度监控记录列表
     *
     * @param parentId 设备温湿度监控记录
     * @return 设备温湿度监控记录集合
     */
    List<OpDeviceTempHumRecord> selectOpDeviceTempHumRecordList(String parentId);

}
