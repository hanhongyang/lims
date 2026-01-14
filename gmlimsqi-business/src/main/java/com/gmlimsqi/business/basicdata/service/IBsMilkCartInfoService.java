package com.gmlimsqi.business.basicdata.service;

import java.util.List;
import com.gmlimsqi.business.basicdata.domain.BsMilkCartInfo;

/**
 * 奶车信息Service接口
 * 
 * @author hhy
 * @date 2025-11-05
 */
public interface IBsMilkCartInfoService 
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
     * 批量删除奶车信息
     * 
     * @param ids 需要删除的奶车信息主键集合
     * @return 结果
     */
    public int deleteBsMilkCartInfoByIds(String[] ids);

    /**
     * 删除奶车信息信息
     * 
     * @param id 奶车信息主键
     * @return 结果
     */
    public int deleteBsMilkCartInfoById(String id);

     /**
     * 根据车牌号查询奶车信息详情
     *
     * @param driverCode 车牌号
     * @return 奶车信息
     */
    public BsMilkCartInfo selectBsMilkCartInfoByDriverCode(String driverCode);
}
