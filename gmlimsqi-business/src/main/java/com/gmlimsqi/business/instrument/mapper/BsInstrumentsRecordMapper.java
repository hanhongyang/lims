package com.gmlimsqi.business.instrument.mapper;

import com.gmlimsqi.business.instrument.domain.BsInstrumentsRecord;

import java.util.List;

/**
 * 设备档案记录Mapper接口
 * 
 * @author hhy
 * @date 2025-11-17
 */
public interface BsInstrumentsRecordMapper 
{
    /**
     * 查询设备档案记录
     * 
     * @param bsInstrumentsRecordId 设备档案记录主键
     * @return 设备档案记录
     */
    public BsInstrumentsRecord selectBsInstrumentsRecordByBsInstrumentsRecordId(String bsInstrumentsRecordId);

    /**
     *  根据主表id查询设备档案记录
     *
     * @param bsInstrumentsId 设备档案主表主键
     * @return 设备档案记录
     */
     public List<BsInstrumentsRecord> selectBsInstrumentsRecordByBsInstrumentsId(String bsInstrumentsId);

    /**
     * 查询设备档案记录列表
     * 
     * @param bsInstrumentsRecord 设备档案记录
     * @return 设备档案记录集合
     */
    public List<BsInstrumentsRecord> selectBsInstrumentsRecordList(BsInstrumentsRecord bsInstrumentsRecord);

    /**
     * 新增设备档案记录
     * 
     * @param bsInstrumentsRecord 设备档案记录
     * @return 结果
     */
    public int insertBsInstrumentsRecord(BsInstrumentsRecord bsInstrumentsRecord);

    /**
     * 修改设备档案记录
     * 
     * @param bsInstrumentsRecord 设备档案记录
     * @return 结果
     */
    public int updateBsInstrumentsRecord(BsInstrumentsRecord bsInstrumentsRecord);

    /**
     * 通过设备档案记录主键更新删除标志
     *
     * @param bsInstrumentsRecordId 设备档案记录ID
     * @return 结果
     */
    public int updateDeleteFlagById(String bsInstrumentsRecordId);

    /**
     * 批量通过设备档案记录主键更新删除标志
     *
     * @param bsInstrumentsRecordId 设备档案记录ID
     * @return 结果
     */
    public int updateDeleteFlagByIds(String[] bsInstrumentsRecordIds);



}
