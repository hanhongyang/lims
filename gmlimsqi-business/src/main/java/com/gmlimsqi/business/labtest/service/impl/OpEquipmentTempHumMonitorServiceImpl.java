package com.gmlimsqi.business.labtest.service.impl;

import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.List;

import com.gmlimsqi.business.labtest.domain.OpDeviceTempHumRecord;
import com.gmlimsqi.business.labtest.domain.OpEquipmentTempHumMonitor;
import com.gmlimsqi.business.labtest.mapper.OpEquipmentTempHumMonitorMapper;
import com.gmlimsqi.business.labtest.service.IOpEquipmentTempHumMonitorService;
import com.gmlimsqi.business.util.UserCacheService;
import com.gmlimsqi.common.core.domain.entity.SysDept;
import com.gmlimsqi.common.core.domain.entity.SysUser;
import com.gmlimsqi.common.exception.ServiceException;
import com.gmlimsqi.common.utils.DateUtils;
import com.gmlimsqi.common.utils.SecurityUtils;
import com.gmlimsqi.common.utils.StringUtils;
import com.gmlimsqi.common.utils.uuid.IdUtils;
import com.gmlimsqi.system.mapper.SysDeptMapper;
import com.gmlimsqi.system.mapper.SysUserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;

import org.springframework.transaction.annotation.Transactional;

/**
 * 设备温湿度监控Service业务层处理
 * 
 * @author hhy
 * @date 2025-10-26
 */
@Service
public class OpEquipmentTempHumMonitorServiceImpl implements IOpEquipmentTempHumMonitorService
{
    @Autowired
    private OpEquipmentTempHumMonitorMapper opEquipmentTempHumMonitorMapper;

    @Autowired
    private UserCacheService userCacheService;

    @Autowired
    private SysUserMapper sysUserMapper;

    @Autowired
    private SysDeptMapper sysDeptMapper;

    /**
     * 查询设备温湿度监控
     * 
     * @param id 设备温湿度监控主键
     * @return 设备温湿度监控
     */
    @Override
    public OpEquipmentTempHumMonitor selectOpEquipmentTempHumMonitorById(String id)
    {
        return opEquipmentTempHumMonitorMapper.selectOpEquipmentTempHumMonitorById(id);
    }

    /**
     * 查询设备温湿度监控列表
     * 
     * @param opEquipmentTempHumMonitor 设备温湿度监控
     * @return 设备温湿度监控
     */
    @Override
    public List<OpEquipmentTempHumMonitor> selectOpEquipmentTempHumMonitorList(OpEquipmentTempHumMonitor opEquipmentTempHumMonitor)
    {
        boolean admin = SecurityUtils.isAdmin(SecurityUtils.getUserId());

        if (!admin) {
            opEquipmentTempHumMonitor.setDeptId(SecurityUtils.getDeptId().toString());
        }

        List<OpEquipmentTempHumMonitor> items = opEquipmentTempHumMonitorMapper.selectOpEquipmentTempHumMonitorList(opEquipmentTempHumMonitor);

        // 批量处理用户名
        if (!items.isEmpty()) {
            Set<String> userIds = items.stream()
                    .map(OpEquipmentTempHumMonitor::getCreateBy)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toSet());

            Map<String, String> usernameMap = userCacheService.batchGetUsernames(userIds);

            items.forEach(vo ->
                    vo.setCreateBy(usernameMap.get(vo.getCreateBy())));
        }

        return items;
    }

    /**
     * 新增设备温湿度监控
     * 
     * @param opEquipmentTempHumMonitor 设备温湿度监控
     * @return 结果
     */
    @Transactional
    @Override
    public int insertOpEquipmentTempHumMonitor(OpEquipmentTempHumMonitor opEquipmentTempHumMonitor)
    {
        // 查询当前登录人部门
        SysDept sysDept = sysDeptMapper.selectDeptById(SecurityUtils.getDeptId());
        // 设置部门名称
        opEquipmentTempHumMonitor.setCompanyName(sysDept.getDeptName());

        if (StringUtils.isEmpty(opEquipmentTempHumMonitor.getId())) {
            opEquipmentTempHumMonitor.setId(IdUtils.simpleUUID());

            OpDeviceTempHumRecord opDeviceTempHumRecord = new OpDeviceTempHumRecord();
            opDeviceTempHumRecord.setId(IdUtils.simpleUUID());
            opDeviceTempHumRecord.setParentId(opEquipmentTempHumMonitor.getId());
            opDeviceTempHumRecord.fillCreateInfo();
            opDeviceTempHumRecord.setCompanyName(opEquipmentTempHumMonitor.getCompanyName());
            opDeviceTempHumRecord.setMonitoringPoint(opEquipmentTempHumMonitor.getMonitoringPoint());
            opDeviceTempHumRecord.setTemperature(opEquipmentTempHumMonitor.getTemperature());
            opDeviceTempHumRecord.setHumidity(opEquipmentTempHumMonitor.getHumidity());
            opDeviceTempHumRecord.setIsEnable(opEquipmentTempHumMonitor.getIsEnable());

            // 插入子表数据
            opEquipmentTempHumMonitorMapper.insertOpDeviceTempHumRecord(opDeviceTempHumRecord);
        }

        // 自动填充创建/更新信息
        opEquipmentTempHumMonitor.fillCreateInfo();
        return opEquipmentTempHumMonitorMapper.insertOpEquipmentTempHumMonitor(opEquipmentTempHumMonitor);
    }

    /**
     * 修改设备温湿度监控
     * 
     * @param opEquipmentTempHumMonitor 设备温湿度监控
     * @return 结果
     */
    @Transactional
    @Override
    public int updateOpEquipmentTempHumMonitor(OpEquipmentTempHumMonitor opEquipmentTempHumMonitor)
    {
        // 自动填充更新信息
        opEquipmentTempHumMonitor.fillUpdateInfo();
        // 根据id查询主表修改前数据
        OpEquipmentTempHumMonitor oldMonitor = selectOpEquipmentTempHumMonitorById(opEquipmentTempHumMonitor.getId());

        // 查询当前登录人部门
        SysDept sysDept = sysDeptMapper.selectDeptById(SecurityUtils.getDeptId());
        // 设置部门名称
        opEquipmentTempHumMonitor.setCompanyName(sysDept.getDeptName());

        // 记录修改前数据
        OpDeviceTempHumRecord opDeviceTempHumRecord = new OpDeviceTempHumRecord();
        opDeviceTempHumRecord.setId(IdUtils.simpleUUID());
        opDeviceTempHumRecord.setParentId(oldMonitor.getId());
        opDeviceTempHumRecord.fillCreateInfo();
        opDeviceTempHumRecord.setCompanyName(oldMonitor.getCompanyName());
        opDeviceTempHumRecord.setCompanyNameNew(opEquipmentTempHumMonitor.getCompanyName());
        opDeviceTempHumRecord.setMonitoringPoint(oldMonitor.getMonitoringPoint());
        opDeviceTempHumRecord.setMonitoringPointNew(opEquipmentTempHumMonitor.getMonitoringPoint());
        opDeviceTempHumRecord.setTemperature(oldMonitor.getTemperature());
        opDeviceTempHumRecord.setTemperatureNew(opEquipmentTempHumMonitor.getTemperature());
        opDeviceTempHumRecord.setHumidity(oldMonitor.getHumidity());
        opDeviceTempHumRecord.setHumidityNew(opEquipmentTempHumMonitor.getHumidity());
        opDeviceTempHumRecord.setIsEnable(oldMonitor.getIsEnable());
        opDeviceTempHumRecord.setIsEnableNew(opEquipmentTempHumMonitor.getIsEnable());
        opDeviceTempHumRecord.setUpdateBy(SecurityUtils.getUserId().toString());
        opDeviceTempHumRecord.setUpdateTime(DateUtils.getNowDate());
        // 插入子表数据
        opEquipmentTempHumMonitorMapper.insertOpDeviceTempHumRecord(opDeviceTempHumRecord);

        // 更新主表数据
        return opEquipmentTempHumMonitorMapper.updateOpEquipmentTempHumMonitor(opEquipmentTempHumMonitor);
    }

    /**
     * 批量删除设备温湿度监控
     * 
     * @param ids 需要删除的设备温湿度监控主键
     * @return 结果
     */
    @Transactional
    @Override
    public int deleteOpEquipmentTempHumMonitorByIds(String[] ids)
    {
        opEquipmentTempHumMonitorMapper.deleteOpDeviceTempHumRecordByParentIds(ids);
        return opEquipmentTempHumMonitorMapper.deleteOpEquipmentTempHumMonitorByIds(ids);
    }

    /**
     * 删除设备温湿度监控信息
     * 
     * @param id 设备温湿度监控主键
     * @return 结果
     */
    @Transactional
    @Override
    public int deleteOpEquipmentTempHumMonitorById(String id)
    {
        opEquipmentTempHumMonitorMapper.deleteOpDeviceTempHumRecordByParentId(id);
        return opEquipmentTempHumMonitorMapper.deleteOpEquipmentTempHumMonitorById(id);
    }

    /**
     * 查询设备温湿度监控记录列表
     *
     * @param parentId 设备温湿度监控记录
     * @return 设备温湿度监控记录集合
     */
    @Override
    public List<OpDeviceTempHumRecord> selectOpDeviceTempHumRecordList(String parentId) {
        // 根据主表id查询记录
        List<OpDeviceTempHumRecord> opDeviceTempHumRecords = opEquipmentTempHumMonitorMapper.selectOpDeviceTempHumRecordList(parentId);

        if (opDeviceTempHumRecords.isEmpty()){
            throw new ServiceException("设备温湿度监控记录不存在");
        }

        for (OpDeviceTempHumRecord opDeviceTempHumRecord : opDeviceTempHumRecords)
        {
            // 查询名称
            SysUser sysUser = sysUserMapper.selectUserById(Long.valueOf(opDeviceTempHumRecord.getCreateBy()));

            opDeviceTempHumRecord.setCreateBy(sysUser.getNickName());
            opDeviceTempHumRecord.setUpdateBy(sysUser.getNickName());
        }

        return opDeviceTempHumRecords;
    }

    /**
     * 新增设备温湿度修改记录信息
     * 
     * @param opEquipmentTempHumMonitor 设备温湿度监控对象
     */
    public void insertOpDeviceTempHumRecord(OpEquipmentTempHumMonitor opEquipmentTempHumMonitor)
    {
        List<OpDeviceTempHumRecord> opDeviceTempHumRecordList = opEquipmentTempHumMonitor.getOpDeviceTempHumRecordList();
        String id = opEquipmentTempHumMonitor.getId();
        if (StringUtils.isNotNull(opDeviceTempHumRecordList))
        {
            List<OpDeviceTempHumRecord> list = new ArrayList<OpDeviceTempHumRecord>();
            for (OpDeviceTempHumRecord opDeviceTempHumRecord : opDeviceTempHumRecordList)
            {
                opDeviceTempHumRecord.setId(IdUtils.simpleUUID());
                opDeviceTempHumRecord.setParentId(id);
                list.add(opDeviceTempHumRecord);
            }
            if (!list.isEmpty())
            {
                opEquipmentTempHumMonitorMapper.batchOpDeviceTempHumRecord(list);
            }
        }
    }
}
