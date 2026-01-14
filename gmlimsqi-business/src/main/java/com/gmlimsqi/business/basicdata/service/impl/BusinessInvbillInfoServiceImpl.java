package com.gmlimsqi.business.basicdata.service.impl;

import com.gmlimsqi.business.basicdata.domain.BusinessInvbillInfo;
import com.gmlimsqi.business.basicdata.mapper.BusinessInvbillInfoMapper;
import com.gmlimsqi.business.basicdata.service.IBusinessInvbillInfoService;
import com.gmlimsqi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 物料子Service业务层处理
 *
 * @author gmlimsqi
 * @date 2025-01-13
 */
@Service
public class BusinessInvbillInfoServiceImpl implements IBusinessInvbillInfoService
{
    @Autowired
    private BusinessInvbillInfoMapper businessInvbillInfoMapper;

    /**
     * 查询物料子
     *
     * @param id 物料子主键
     * @return 物料子
     */
    @Override
    public BusinessInvbillInfo selectBusinessInvbillInfoById(String id)
    {
        return businessInvbillInfoMapper.selectBusinessInvbillInfoById(id);
    }

    /**
     * 查询物料子列表
     *
     * @param businessInvbillInfo 物料子
     * @return 物料子
     */
    @Override
    public List<BusinessInvbillInfo> selectBusinessInvbillInfoList(BusinessInvbillInfo businessInvbillInfo)
    {
        return businessInvbillInfoMapper.selectBusinessInvbillInfoList(businessInvbillInfo);
    }

    /**
     * 新增物料子
     *
     * @param businessInvbillInfo 物料子
     * @return 结果
     */
    @Override
    public int insertBusinessInvbillInfo(BusinessInvbillInfo businessInvbillInfo)
    {
                businessInvbillInfo.setCreateTime(DateUtils.getNowDate());
            return businessInvbillInfoMapper.insertBusinessInvbillInfo(businessInvbillInfo);
    }

    /**
     * 修改物料子
     *
     * @param businessInvbillInfo 物料子
     * @return 结果
     */
    @Override
    public int updateBusinessInvbillInfo(BusinessInvbillInfo businessInvbillInfo)
    {
                businessInvbillInfo.setUpdateTime(DateUtils.getNowDate());
        return businessInvbillInfoMapper.updateBusinessInvbillInfo(businessInvbillInfo);
    }

    /**
     * 批量删除物料子
     *
     * @param ids 需要删除的物料子主键
     * @return 结果
     */
    @Override
    public int deleteBusinessInvbillInfoByIds(String[] ids)
    {
        return businessInvbillInfoMapper.deleteBusinessInvbillInfoByIds(ids);
    }

    /**
     * 删除物料子信息
     *
     * @param id 物料子主键
     * @return 结果
     */
    @Override
    public int deleteBusinessInvbillInfoById(String id)
    {
        return businessInvbillInfoMapper.deleteBusinessInvbillInfoById(id);
    }
}
