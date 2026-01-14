package com.gmlimsqi.business.basicdata.mapper;

import java.util.Date;
import java.util.List;
import com.gmlimsqi.business.basicdata.domain.LabtestMethodInstrument;
import org.apache.ibatis.annotations.Param;

/**
 * 检测方法设备对应Mapper接口
 * 
 * @author hhy
 * @date 2025-08-07
 */
public interface LabtestMethodInstrumentMapper 
{
    /**
     * 查询检测方法设备对应
     * 
     * @param labtestMethodInstrumentId 检测方法设备对应主键
     * @return 检测方法设备对应
     */
    public LabtestMethodInstrument selectLabtestMethodInstrumentByLabtestMethodInstrumentId(String labtestMethodInstrumentId);

    /**
     * 查询检测方法设备对应列表
     * 
     * @param labtestMethodInstrument 检测方法设备对应
     * @return 检测方法设备对应集合
     */
    public List<LabtestMethodInstrument> selectLabtestMethodInstrumentList(LabtestMethodInstrument labtestMethodInstrument);

    /**
     * 新增检测方法设备对应
     * 
     * @param labtestMethodInstrument 检测方法设备对应
     * @return 结果
     */
    public int insertLabtestMethodInstrument(LabtestMethodInstrument labtestMethodInstrument);

    /**
     * 修改检测方法设备对应
     * 
     * @param labtestMethodInstrument 检测方法设备对应
     * @return 结果
     */
    public int updateLabtestMethodInstrument(LabtestMethodInstrument labtestMethodInstrument);

    /**
     * 通过检测方法设备对应主键更新删除标志
     *
     * @param labtestMethodInstrumentId 检测方法设备对应ID
     * @return 结果
     */
    public int updateDeleteById(@Param("labtestMethodInstrumentId")String labtestMethodInstrumentId,@Param("now") Date now,@Param("updateUserId") String updateUserId);

    /**
     * 批量通过检测方法设备对应主键更新删除标志
     *
     * @param labtestMethodInstrumentId 检测方法设备对应ID
     * @return 结果
     */
    public int updateDeleteByIds(@Param("labtestMethodInstrumentIds") String[] labtestMethodInstrumentIds,@Param("now") Date now,@Param("updateUserId") String updateUserId);

    /**
     * 删除检测方法设备对应
     * 
     * @param labtestMethodInstrumentId 检测方法设备对应主键
     * @return 结果
     */
    public int deleteLabtestMethodInstrumentByLabtestMethodInstrumentId(String labtestMethodInstrumentId);

    /**
     * 批量删除检测方法设备对应
     * 
     * @param labtestMethodInstrumentIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteLabtestMethodInstrumentByLabtestMethodInstrumentIds(String[] labtestMethodInstrumentIds);
    /**
     * 批量通过检测方法id更新删除标志
     *
     * @param bsLabtestMethodsId 检测方法ID
     * @return 结果
     */
    public void updateDeleteByMethodId(@Param("bsLabtestMethodsId")String bsLabtestMethodsId, @Param("now") Date now, @Param("updateUserId") String updateUserId);
}
