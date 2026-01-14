package com.gmlimsqi.business.basicdata.service;

import java.util.List;
import com.gmlimsqi.business.basicdata.domain.BsMilkWarehouse;

/**
 * 奶仓档案Service接口
 * 
 * @author hhy
 * @date 2025-11-05
 */
public interface IBsMilkWarehouseService 
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
     * 批量删除奶仓档案
     * 
     * @param ids 需要删除的奶仓档案主键集合
     * @return 结果
     */
    public int deleteBsMilkWarehouseByIds(String[] ids);

    /**
     * 删除奶仓档案信息
     * 
     * @param id 奶仓档案主键
     * @return 结果
     */
    public int deleteBsMilkWarehouseById(String id);
}
