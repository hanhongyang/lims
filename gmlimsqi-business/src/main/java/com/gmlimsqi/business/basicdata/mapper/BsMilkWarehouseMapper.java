package com.gmlimsqi.business.basicdata.mapper;

import java.util.List;
import com.gmlimsqi.business.basicdata.domain.BsMilkWarehouse;

/**
 * 奶仓档案Mapper接口
 * 
 * @author hhy
 * @date 2025-11-05
 */
public interface BsMilkWarehouseMapper 
{
    /**
     * 查询奶仓档案
     * 
     * @param id 奶仓档案主键
     * @return 奶仓档案
     */
    public BsMilkWarehouse selectBsMilkWarehouseById(String id);

    /**
     * 查询奶仓档案列表
     * 
     * @param bsMilkWarehouse 奶仓档案
     * @return 奶仓档案集合
     */
    public List<BsMilkWarehouse> selectBsMilkWarehouseList(BsMilkWarehouse bsMilkWarehouse);

    /**
     * 新增奶仓档案
     * 
     * @param bsMilkWarehouse 奶仓档案
     * @return 结果
     */
    public int insertBsMilkWarehouse(BsMilkWarehouse bsMilkWarehouse);

    /**
     * 修改奶仓档案
     * 
     * @param bsMilkWarehouse 奶仓档案
     * @return 结果
     */
    public int updateBsMilkWarehouse(BsMilkWarehouse bsMilkWarehouse);

    /**
     * 通过奶仓档案主键更新删除标志
     *
     * @param id 奶仓档案ID
     * @return 结果
     */
    public int updateDeleteFlagById(String id);

    /**
     * 批量通过奶仓档案主键更新删除标志
     *
     * @param id 奶仓档案ID
     * @return 结果
     */
    public int updateDeleteFlagByIds(String[] ids);

    /**
     * 删除奶仓档案
     * 
     * @param id 奶仓档案主键
     * @return 结果
     */
    public int deleteBsMilkWarehouseById(String id);

    /**
     * 批量删除奶仓档案
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteBsMilkWarehouseByIds(String[] ids);
}
