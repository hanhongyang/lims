package com.gmlimsqi.business.basicdata.mapper;

import java.util.List;
import com.gmlimsqi.business.basicdata.domain.BsItemInstrument;
import org.apache.ibatis.annotations.Param;

/**
 * 项目使用设备Mapper接口
 * 
 * @author hhy
 * @date 2025-09-29
 */
public interface BsItemInstrumentMapper 
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

    /**
     * 通过项目使用设备主键更新删除标志
     *
     * @param bsItemInstrumentId 项目使用设备ID
     * @return 结果
     */
    public int updateDeleteFlagById(@Param("updateUserId") String updateUserId,@Param("bsItemInstrumentId")String bsItemInstrumentId);

    /**
     * 批量通过项目使用设备主键更新删除标志
     *
     * @param bsItemInstrumentId 项目使用设备ID
     * @return 结果
     */
    public int updateDeleteFlagByItemId(@Param("updateUserId") String updateUserId,@Param("itemId") String itemId);


}
