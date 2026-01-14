package com.gmlimsqi.business.instrument.mapper;

import java.util.List;
import com.gmlimsqi.business.instrument.domain.Instruments;

/**
 * 设备档案Mapper接口
 * 
 * @author ruoyi
 * @date 2025-08-06
 */
public interface InstrumentsMapper
{
    /**
     * 查询设备档案
     * 
     * @param bsInstrumentsId 设备档案主键
     * @return 设备档案
     */
    public Instruments selectBsInstrumentsByBsInstrumentsId(String bsInstrumentsId);

    /**
     * 查询设备档案列表
     * 
     * @param instruments 设备档案
     * @return 设备档案集合
     */
    public List<Instruments> selectBsInstrumentsList(Instruments instruments);

    /**
     * 新增设备档案
     * 
     * @param instruments 设备档案
     * @return 结果
     */
    public int insertBsInstruments(Instruments instruments);

    /**
     * 修改设备档案
     * 
     * @param instruments 设备档案
     * @return 结果
     */
    public int updateBsInstruments(Instruments instruments);

    /**
     * 删除设备档案
     * 
     * @param bsInstrumentsId 设备档案主键
     * @return 结果
     */
    public int deleteBsInstrumentsByBsInstrumentsId(String bsInstrumentsId);

    /**
     * 批量删除设备档案
     * 
     * @param bsInstrumentsIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteBsInstrumentsByBsInstrumentsIds(String[] bsInstrumentsIds);
}
