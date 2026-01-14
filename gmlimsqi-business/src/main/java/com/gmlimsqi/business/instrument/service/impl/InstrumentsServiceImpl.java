package com.gmlimsqi.business.instrument.service.impl;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import com.gmlimsqi.business.instrument.domain.BsInstrumentsRecord;
import com.gmlimsqi.business.instrument.mapper.BsInstrumentsRecordMapper;
import com.gmlimsqi.business.util.UserCacheService;
import com.gmlimsqi.common.utils.SecurityUtils;
import com.gmlimsqi.common.utils.StringUtils;
import com.gmlimsqi.common.utils.uuid.IdUtils;
import com.gmlimsqi.system.mapper.SysDeptMapper;
import com.gmlimsqi.system.mapper.SysUserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.gmlimsqi.business.instrument.mapper.InstrumentsMapper;
import com.gmlimsqi.business.instrument.domain.Instruments;
import com.gmlimsqi.business.instrument.service.IInstrumentsService;

/**
 * 设备档案Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-08-06
 */
@Service
public class InstrumentsServiceImpl implements IInstrumentsService
{
    @Autowired
    private InstrumentsMapper instrumentsMapper;
    @Autowired
    private UserCacheService userCacheService;

    @Autowired
    private SysDeptMapper sysDeptMapper;

    @Autowired
    private SysUserMapper sysUserMapper;

    @Autowired
    private BsInstrumentsRecordMapper bsInstrumentsRecordMapper;

    /**
     * 查询设备档案
     * 
     * @param bsInstrumentsId 设备档案主键
     * @return 设备档案
     */
    @Override
    public Instruments selectBsInstrumentsByBsInstrumentsId(String bsInstrumentsId)
    {
        Instruments instruments = instrumentsMapper.selectBsInstrumentsByBsInstrumentsId(bsInstrumentsId);

        // 查询子表数据
        List<BsInstrumentsRecord> bsInstrumentsRecordList = bsInstrumentsRecordMapper.selectBsInstrumentsRecordByBsInstrumentsId(bsInstrumentsId);
        if (!bsInstrumentsRecordList.isEmpty()) {
            instruments.setBsInstrumentsRecordList(bsInstrumentsRecordList);
        }

        // 查询部门信息
        if (StringUtils.isNotEmpty(instruments.getDeptId())) {
            instruments.setDeptName(sysDeptMapper.selectDeptById(Long.parseLong(instruments.getDeptId())).getDeptName());
        }

        return instruments;
    }

    /**
     * 查询设备档案列表
     * 
     * @param bsInstruments 设备档案
     * @return 设备档案
     */
    @Override
    public List<Instruments> selectBsInstrumentsList(Instruments bsInstruments)
    {
        if (!SecurityUtils.isAdmin(SecurityUtils.getUserId())) {
            bsInstruments.setDeptId(SecurityUtils.getDeptId().toString());
        }

        List<Instruments> instruments = instrumentsMapper.selectBsInstrumentsList(bsInstruments);

        return instruments;
    }

    /**
     * 新增设备档案
     * 
     * @param instruments 设备档案
     * @return 结果
     */
    @Override
    public int insertBsInstruments(Instruments instruments)
    {
        if (StringUtils.isEmpty(instruments.getBsInstrumentsId())) {
            instruments.setBsInstrumentsId(IdUtils.simpleUUID());
        }

        if (!"0".equals(instruments.getStatus())){
            instruments.setIsEnable("2");
        }

        instruments.setIsDelete("0");

        // 新增子表数据
        BsInstrumentsRecord bsInstrumentsRecord = new BsInstrumentsRecord();
        bsInstrumentsRecord.setBsInstrumentsRecordId(IdUtils.simpleUUID());
        bsInstrumentsRecord.setBsInstrumentsId(instruments.getBsInstrumentsId());
        bsInstrumentsRecord.setInstrumentName(instruments.getInstrumentName());
        bsInstrumentsRecord.setInstrumentCode(instruments.getInstrumentCode());
        bsInstrumentsRecord.setDeptId(instruments.getDeptId());
        bsInstrumentsRecord.setType(instruments.getType());
        bsInstrumentsRecord.setModelNumber(instruments.getModelNumber());
        bsInstrumentsRecord.setStatus(instruments.getStatus());
        bsInstrumentsRecord.setRemark(instruments.getRemark());
        bsInstrumentsRecord.setIsDelete(instruments.getIsDelete());
        bsInstrumentsRecord.setIsEnable(instruments.getIsEnable());

        // 新增子表数据
        bsInstrumentsRecordMapper.insertBsInstrumentsRecord(bsInstrumentsRecord);

        // 自动填充创建/更新信息
        instruments.fillCreateInfo();
        return instrumentsMapper.insertBsInstruments(instruments);
    }

    /**
     * 修改设备档案
     * 
     * @param instruments 设备档案
     * @return 结果
     */
    @Override
    public int updateBsInstruments(Instruments instruments)
    {
        // 记录原数据
        Instruments originalInstruments = instrumentsMapper.selectBsInstrumentsByBsInstrumentsId(instruments.getBsInstrumentsId());

        if (originalInstruments == null) {
            throw new IllegalArgumentException("设备档案不存在");
        }

        // 比较并更新子表数据
        BsInstrumentsRecord bsInstrumentsRecord = new BsInstrumentsRecord();
        bsInstrumentsRecord.setBsInstrumentsRecordId(IdUtils.simpleUUID());
        bsInstrumentsRecord.setBsInstrumentsId(originalInstruments.getBsInstrumentsId());
        bsInstrumentsRecord.setInstrumentName(originalInstruments.getInstrumentName());
        bsInstrumentsRecord.setInstrumentCode(originalInstruments.getInstrumentCode());
        bsInstrumentsRecord.setDeptId(originalInstruments.getDeptId());
        bsInstrumentsRecord.setType(originalInstruments.getType());
        bsInstrumentsRecord.setModelNumber(originalInstruments.getModelNumber());
        bsInstrumentsRecord.setStatus(originalInstruments.getStatus());
        bsInstrumentsRecord.setRemark(originalInstruments.getRemark());
        bsInstrumentsRecord.setIsDelete(originalInstruments.getIsDelete());
        bsInstrumentsRecord.setIsEnable(originalInstruments.getIsEnable());

        if (!"0".equals(instruments.getStatus())){
            instruments.setIsEnable("2");
        }

        // 自动填充更新信息
        instruments.fillUpdateInfo();
        return instrumentsMapper.updateBsInstruments(instruments);
    }

    /**
     * 批量删除设备档案
     * 
     * @param bsInstrumentsIds 需要删除的设备档案主键
     * @return 结果
     */
    @Override
    public int deleteBsInstrumentsByBsInstrumentsIds(String[] bsInstrumentsIds)
    {
        return instrumentsMapper.deleteBsInstrumentsByBsInstrumentsIds(bsInstrumentsIds);
    }

    /**
     * 删除设备档案信息
     * 
     * @param bsInstrumentsId 设备档案主键
     * @return 结果
     */
    @Override
    public int deleteBsInstrumentsByBsInstrumentsId(String bsInstrumentsId)
    {
        return instrumentsMapper.deleteBsInstrumentsByBsInstrumentsId(bsInstrumentsId);
    }
}
