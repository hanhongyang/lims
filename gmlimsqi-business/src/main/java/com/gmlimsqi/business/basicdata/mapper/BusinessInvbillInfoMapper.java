package com.gmlimsqi.business.basicdata.mapper;

import com.gmlimsqi.business.basicdata.domain.BusinessInvbillInfo;

import java.util.List;

/**
 * 物料子Mapper接口
 *
 * @author egap
 * @date 2025-01-13
 */
public interface BusinessInvbillInfoMapper
{
    /**
     * 查询物料子表
     *
     * @param id 物料子主键
     * @return 物料子
     */
    public BusinessInvbillInfo selectBusinessInvbillInfoById(String id);

    /**
     * 查询物料子表列表
     *
     * @param businessInvbillInfo 物料子
     * @return 物料子集合
     */
    public List<BusinessInvbillInfo> selectBusinessInvbillInfoList(BusinessInvbillInfo businessInvbillInfo);

    /**
     * 新增物料子表
     *
     * @param businessInvbillInfo 物料子
     * @return 结果
     */
    public int insertBusinessInvbillInfo(BusinessInvbillInfo businessInvbillInfo);

    /**
     * 修改物料子表
     *
     * @param businessInvbillInfo 物料子
     * @return 结果
     */
    public int updateBusinessInvbillInfo(BusinessInvbillInfo businessInvbillInfo);

    /**
     * 删除物料子表
     *
     * @param id 物料子主键
     * @return 结果
     */
    public int deleteBusinessInvbillInfoById(String id);

    /**
     * 根据主表id删除子表
     * @param invbillId
     * @return
     */
    public int deleteBusinessInvbillInfoByInvbillId(String invbillId);

    /**
     * 批量删除物料子表
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteBusinessInvbillInfoByIds(String[] ids);

    /**
     * 批量新增物料子表
     * @param list
     * @return
     */
    public int batchInsertBusinessInvbillInfo(List<BusinessInvbillInfo> list);

    /**
     * 根据主表id查询物料子表
     * @param invbillId
     * @return
     */
    public BusinessInvbillInfo selectBusinessInvbillInfoByInvbillId(String invbillId);
}