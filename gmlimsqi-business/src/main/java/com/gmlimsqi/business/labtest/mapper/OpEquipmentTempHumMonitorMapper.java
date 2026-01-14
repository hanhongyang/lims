package com.gmlimsqi.business.labtest.mapper;


import com.gmlimsqi.business.labtest.domain.OpDeviceTempHumRecord;
import com.gmlimsqi.business.labtest.domain.OpEquipmentTempHumMonitor;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * 设备温湿度监控Mapper接口
 * 
 * @author hhy
 * @date 2025-10-26
 */
public interface OpEquipmentTempHumMonitorMapper 
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
     * 通过设备温湿度监控主键更新删除标志
     *
     * @param id 设备温湿度监控ID
     * @return 结果
     */
    public int updateDeleteFlagById(String id);

    /**
     * 批量通过设备温湿度监控主键更新删除标志
     *
     * @param ids 设备温湿度监控ID
     * @return 结果
     */
    public int updateDeleteFlagByIds(String[] ids);

    /**
     * 删除设备温湿度监控
     * 
     * @param id 设备温湿度监控主键
     * @return 结果
     */
    public int deleteOpEquipmentTempHumMonitorById(String id);

    /**
     * 批量删除设备温湿度监控
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteOpEquipmentTempHumMonitorByIds(String[] ids);

    /**
     * 批量删除设备温湿度修改记录
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteOpDeviceTempHumRecordByParentIds(String[] ids);
    
    /**
     * 批量新增设备温湿度修改记录
     * 
     * @param opDeviceTempHumRecordList 设备温湿度修改记录列表
     * @return 结果
     */
    public int batchOpDeviceTempHumRecord(List<OpDeviceTempHumRecord> opDeviceTempHumRecordList);
    

    /**
     * 通过设备温湿度监控主键删除设备温湿度修改记录信息
     * 
     * @param id 设备温湿度监控ID
     * @return 结果
     */
    public int deleteOpDeviceTempHumRecordByParentId(String id);

    /**
     * 批量更新设备温湿度修改记录删除标志
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int updateDeleteFlagByIdParentIds(@Param("ids")String[] ids, @Param("now") Date now, @Param("updateUserId") String updateUserId);



    /**
     * 通过设备温湿度监控主键更新删除标志设备温湿度修改记录信息
     *
     * @param id 设备温湿度监控ID
     * @return 结果
     */
    public int updateDeleteFlagByIdParentId(@Param("id")String id,@Param("now") Date now,@Param("updateUserId") String updateUserId);

    /**
     * 新增设备温湿度修改记录
     * @param opDeviceTempHumRecord
     * @return
     */
    int insertOpDeviceTempHumRecord(OpDeviceTempHumRecord opDeviceTempHumRecord);

     /**
     * 查询设备温湿度监控记录列表
     *
     * @param parentId 设备温湿度监控记录
     * @return 设备温湿度监控记录集合
     */
    List<OpDeviceTempHumRecord> selectOpDeviceTempHumRecordList(String parentId);
}
