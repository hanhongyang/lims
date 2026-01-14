package com.gmlimsqi.business.basicdata.mapper;

import java.util.List;
import com.gmlimsqi.business.basicdata.domain.BsMilkCartInfo;
import org.apache.ibatis.annotations.Param;

/**
 * 奶车信息Mapper接口
 * 
 * @author hhy
 * @date 2025-11-05
 */
public interface BsMilkCartInfoMapper 
{
    /**
     * 查询奶车信息
     * 
     * @param id 奶车信息主键
     * @return 奶车信息
     */
    public BsMilkCartInfo selectBsMilkCartInfoById(String id);

    /**
     * 查询奶车信息列表
     * 
     * @param bsMilkCartInfo 奶车信息
     * @return 奶车信息集合
     */
    public List<BsMilkCartInfo> selectBsMilkCartInfoList(BsMilkCartInfo bsMilkCartInfo);

    /**
     * 新增奶车信息
     * 
     * @param bsMilkCartInfo 奶车信息
     * @return 结果
     */
    public int insertBsMilkCartInfo(BsMilkCartInfo bsMilkCartInfo);

    /**
     * 修改奶车信息
     * 
     * @param bsMilkCartInfo 奶车信息
     * @return 结果
     */
    public int updateBsMilkCartInfo(BsMilkCartInfo bsMilkCartInfo);

    /**
     * 通过奶车信息主键更新删除标志
     *
     * @param id 奶车信息ID
     * @return 结果
     */
    public int updateDeleteFlagById(String id);

    /**
     * 批量通过奶车信息主键更新删除标志
     *
     * @param id 奶车信息ID
     * @return 结果
     */
    public int updateDeleteFlagByIds(String[] ids);

    /**
     * 删除奶车信息
     * 
     * @param id 奶车信息主键
     * @return 结果
     */
    public int deleteBsMilkCartInfoById(String id);

    /**
     * 批量删除奶车信息
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteBsMilkCartInfoByIds(String[] ids);

     /**
     * 根据车牌号查询奶车信息详情
     *
     * @param driverCode 车牌号
     * @return 奶车信息
     */
    public BsMilkCartInfo selectBsMilkCartInfoByDriverCode(@Param("driverCode") String driverCode,
                                                           @Param("deptId") String deptId);
}
