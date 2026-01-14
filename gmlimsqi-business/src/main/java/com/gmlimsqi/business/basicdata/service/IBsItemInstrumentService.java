package com.gmlimsqi.business.basicdata.service;

import java.util.List;
import com.gmlimsqi.business.basicdata.domain.BsItemInstrument;

/**
 * 项目使用设备Service接口
 * 
 * @author hhy
 * @date 2025-09-29
 */
public interface IBsItemInstrumentService 
{
    /**
     * 查询项目使用设备
     * 
     * @param bsItemInstrumentId 项目使用设备主键
     * @return 项目使用设备
     */
    public BsItemInstrument selectBsItemInstrumentByBsItemInstrumentId(String bsItemInstrumentId);

    /**
     * 查询项目使用设备列表
     * 
     * @param bsItemInstrument 项目使用设备
     * @return 项目使用设备集合
     */
    public List<BsItemInstrument> selectBsItemInstrumentList(BsItemInstrument bsItemInstrument);

    /**
     * 新增项目使用设备
     * 
     * @param bsItemInstrument 项目使用设备
     * @return 结果
     */
    public int insertBsItemInstrument(BsItemInstrument bsItemInstrument);

    /**
     * 修改项目使用设备
     * 
     * @param bsItemInstrument 项目使用设备
     * @return 结果
     */
    public int updateBsItemInstrument(BsItemInstrument bsItemInstrument);


}
