package com.gmlimsqi.business.basicdata.service;

import com.gmlimsqi.business.basicdata.domain.BusinessInvbillInfo;

import java.util.List;

/**
 * 物料子Service接口
 *
 * @author egap
 * @date 2025-01-13
 */
public interface IBusinessInvbillInfoService
{
    /**
     * 查询物料子
     *
     * @param id 物料子主键
     * @return 物料子
     */
    public BusinessInvbillInfo selectBusinessInvbillInfoById(String id);

    /**
     * 查询物料子列表
     *
     * @param businessInvbillInfo 物料子
     * @return 物料子集合
     */
    public List<BusinessInvbillInfo> selectBusinessInvbillInfoList(BusinessInvbillInfo businessInvbillInfo);

    /**
     * 新增物料子
     *
     * @param businessInvbillInfo 物料子
     * @return 结果
     */
    public int insertBusinessInvbillInfo(BusinessInvbillInfo businessInvbillInfo);

    /**
     * 修改物料子
     *
     * @param businessInvbillInfo 物料子
     * @return 结果
     */
    public int updateBusinessInvbillInfo(BusinessInvbillInfo businessInvbillInfo);

    /**
     * 批量删除物料子
     *
     * @param ids 需要删除的物料子主键集合
     * @return 结果
     */
    public int deleteBusinessInvbillInfoByIds(String[] ids);

    /**
     * 删除物料子信息
     *
     * @param id 物料子主键
     * @return 结果
     */
    public int deleteBusinessInvbillInfoById(String id);
}