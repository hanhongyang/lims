package com.gmlimsqi.business.basicdata.service.impl;

import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.List;
import com.gmlimsqi.common.utils.StringUtils;
import com.gmlimsqi.common.utils.uuid.IdUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.gmlimsqi.business.basicdata.mapper.BsInvbillInfoMapper;
import com.gmlimsqi.business.basicdata.domain.BsInvbillInfo;
import com.gmlimsqi.business.basicdata.service.IBsInvbillInfoService;

/**
 * 物料子Service业务层处理
 * 
 * @author hhy
 * @date 2025-09-29
 */
@Service
public class BsInvbillInfoServiceImpl implements IBsInvbillInfoService 
{
    @Autowired
    private BsInvbillInfoMapper bsInvbillInfoMapper;


    /**
     * 查询物料子
     * 
     * @param bsInvbillInfoId 物料子主键
     * @return 物料子
     */
    @Override
    public BsInvbillInfo selectBsInvbillInfoByBsInvbillInfoId(String bsInvbillInfoId)
    {
        return bsInvbillInfoMapper.selectBsInvbillInfoByBsInvbillInfoId(bsInvbillInfoId);
    }

    /**
     * 查询物料子列表
     * 
     * @param bsInvbillInfo 物料子
     * @return 物料子
     */
    @Override
    public List<BsInvbillInfo> selectBsInvbillInfoList(BsInvbillInfo bsInvbillInfo)
    {
        List<BsInvbillInfo> items = bsInvbillInfoMapper.selectBsInvbillInfoList(bsInvbillInfo);



        return items;
    }

    /**
     * 新增物料子
     * 
     * @param bsInvbillInfo 物料子
     * @return 结果
     */
    @Override
    public int insertBsInvbillInfo(BsInvbillInfo bsInvbillInfo)
    {
        if (StringUtils.isEmpty(bsInvbillInfo.getBsInvbillInfoId())) {
            bsInvbillInfo.setBsInvbillInfoId(IdUtils.simpleUUID());
        }
        // 自动填充创建/更新信息
        bsInvbillInfo.fillCreateInfo();
        return bsInvbillInfoMapper.insertBsInvbillInfo(bsInvbillInfo);
    }

    /**
     * 修改物料子
     * 
     * @param bsInvbillInfo 物料子
     * @return 结果
     */
    @Override
    public int updateBsInvbillInfo(BsInvbillInfo bsInvbillInfo)
    {
        // 自动填充更新信息
        bsInvbillInfo.fillUpdateInfo();
        return bsInvbillInfoMapper.updateBsInvbillInfo(bsInvbillInfo);
    }

}
