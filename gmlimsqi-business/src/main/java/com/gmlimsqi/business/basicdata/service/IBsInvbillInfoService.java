package com.gmlimsqi.business.basicdata.service;

import java.util.List;
import com.gmlimsqi.business.basicdata.domain.BsInvbillInfo;

/**
 * 物料子Service接口
 * 
 * @author hhy
 * @date 2025-09-29
 */
public interface IBsInvbillInfoService 
{
    /**
     * 查询物料子
     * 
     * @param bsInvbillInfoId 物料子主键
     * @return 物料子
     */
    public BsInvbillInfo selectBsInvbillInfoByBsInvbillInfoId(String bsInvbillInfoId);

    /**
     * 查询物料子列表
     * 
     * @param bsInvbillInfo 物料子
     * @return 物料子集合
     */
    public List<BsInvbillInfo> selectBsInvbillInfoList(BsInvbillInfo bsInvbillInfo);

    /**
     * 新增物料子
     * 
     * @param bsInvbillInfo 物料子
     * @return 结果
     */
    public int insertBsInvbillInfo(BsInvbillInfo bsInvbillInfo);

    /**
     * 修改物料子
     * 
     * @param bsInvbillInfo 物料子
     * @return 结果
     */
    public int updateBsInvbillInfo(BsInvbillInfo bsInvbillInfo);


}
