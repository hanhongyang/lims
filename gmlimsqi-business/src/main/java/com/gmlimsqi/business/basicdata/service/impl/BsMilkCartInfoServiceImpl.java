package com.gmlimsqi.business.basicdata.service.impl;

import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.List;

import com.gmlimsqi.business.basicdata.domain.BsMilkCartInfoLeadSeal;
import com.gmlimsqi.business.basicdata.mapper.BsMilkCartInfoLeadSealMapper;
import com.gmlimsqi.business.util.UserCacheService;
import com.gmlimsqi.common.annotation.DataScope;
import com.gmlimsqi.common.exception.ServiceException;
import com.gmlimsqi.common.utils.SecurityUtils;
import com.gmlimsqi.common.utils.StringUtils;
import com.gmlimsqi.common.utils.uuid.IdUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.gmlimsqi.business.basicdata.mapper.BsMilkCartInfoMapper;
import com.gmlimsqi.business.basicdata.domain.BsMilkCartInfo;
import com.gmlimsqi.business.basicdata.service.IBsMilkCartInfoService;
import org.springframework.transaction.annotation.Transactional;

/**
 * 奶车信息Service业务层处理
 * 
 * @author hhy
 * @date 2025-11-05
 */
@Service
public class BsMilkCartInfoServiceImpl implements IBsMilkCartInfoService 
{
    @Autowired
    private BsMilkCartInfoMapper bsMilkCartInfoMapper;
    @Autowired
    private UserCacheService userCacheService;
    @Autowired
    private BsMilkCartInfoLeadSealMapper bsMilkCartInfoLeadSealMapper;

    /**
     * 查询奶车信息
     * 
     * @param id 奶车信息主键
     * @return 奶车信息
     */
    @Override
    public BsMilkCartInfo selectBsMilkCartInfoById(String id)
    {
        BsMilkCartInfo bsMilkCartInfo = bsMilkCartInfoMapper.selectBsMilkCartInfoById(id);
        List<BsMilkCartInfoLeadSeal> bsMilkCartInfoLeadSeals = bsMilkCartInfoLeadSealMapper.selectByMilkCartInfoId(id);
        bsMilkCartInfo.setMilkCartInfoLeadSealList(bsMilkCartInfoLeadSeals);
        return bsMilkCartInfo;
    }

    /**
     * 查询奶车信息列表
     * 
     * @param bsMilkCartInfo 奶车信息
     * @return 奶车信息
     */
    @Override
    @DataScope(deptAlias = "a", permission = "basicdata:info:list")
    public List<BsMilkCartInfo> selectBsMilkCartInfoList(BsMilkCartInfo bsMilkCartInfo)
    {
//        if (!SecurityUtils.isAdmin(SecurityUtils.getUserId())){
//            bsMilkCartInfo.setDeptId(SecurityUtils.getDeptId().toString());
//        }

        List<BsMilkCartInfo> items = bsMilkCartInfoMapper.selectBsMilkCartInfoList(bsMilkCartInfo);

        // 批量处理用户名
        if (!items.isEmpty()) {
            Set<String> userIds = items.stream()
                    .map(BsMilkCartInfo::getCreateBy)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toSet());

            Map<String, String> usernameMap = userCacheService.batchGetUsernames(userIds);

            items.forEach(vo ->
                    vo.setCreateBy(usernameMap.get(vo.getCreateBy())));
        }

        return items;
    }

    /**
     * 新增奶车信息
     * 
     * @param bsMilkCartInfo 奶车信息
     * @return 结果
     */
    @Override
    @Transactional
    public int insertBsMilkCartInfo(BsMilkCartInfo bsMilkCartInfo)
    {
        if (StringUtils.isEmpty(bsMilkCartInfo.getId())) {
            bsMilkCartInfo.setId(IdUtils.simpleUUID());
        }

        Long deptId = SecurityUtils.getDeptId();

        bsMilkCartInfo.setDeptId(deptId.toString());

        /*if (!StringUtils.isEmpty(bsMilkCartInfo.getCartLeadSealNumber())){
            // 处理铅封号，去掉空格，去除重复逗号，去除首尾逗号，去除空字符串，去除首尾空格，将逗号全部换为英文逗号
            bsMilkCartInfo.setCartLeadSealNumber(bsMilkCartInfo.getCartLeadSealNumber().replaceAll("\\s+", ""));
            bsMilkCartInfo.setCartLeadSealNumber(bsMilkCartInfo.getCartLeadSealNumber().replaceAll(",+", ","));
            bsMilkCartInfo.setCartLeadSealNumber(bsMilkCartInfo.getCartLeadSealNumber().replaceAll("^,|,$", ""));
            bsMilkCartInfo.setCartLeadSealNumber(bsMilkCartInfo.getCartLeadSealNumber().replaceAll("，", ","));
        }*/

        // 填充子表数据
        List<BsMilkCartInfoLeadSeal> milkCartInfoLeadSealList = bsMilkCartInfo.getMilkCartInfoLeadSealList();
        if (!milkCartInfoLeadSealList.isEmpty()) {
            milkCartInfoLeadSealList.forEach(item -> {
                item.setMilkCartInfoLeadSealId(IdUtils.simpleUUID());
                item.setMilkCartInfoId(bsMilkCartInfo.getId());
            });
        }

        // 批量插入子表数据
        if (!milkCartInfoLeadSealList.isEmpty()) {
            bsMilkCartInfoLeadSealMapper.insertBatch(milkCartInfoLeadSealList);
        }

        // 自动填充创建/更新信息
        bsMilkCartInfo.fillCreateInfo();
        return bsMilkCartInfoMapper.insertBsMilkCartInfo(bsMilkCartInfo);
    }

    /**
     * 修改奶车信息
     * 
     * @param bsMilkCartInfo 奶车信息
     * @return 结果
     */
    @Override
    @Transactional
    public int updateBsMilkCartInfo(BsMilkCartInfo bsMilkCartInfo)
    {
        // 自动填充更新信息
        bsMilkCartInfo.fillUpdateInfo();

        // 处理子表数据
        List<BsMilkCartInfoLeadSeal> milkCartInfoLeadSealList = bsMilkCartInfo.getMilkCartInfoLeadSealList();
        // 删除子表数据
        if (!milkCartInfoLeadSealList.isEmpty()) {
            bsMilkCartInfoLeadSealMapper.deleteByMilkCartInfoId(bsMilkCartInfo.getId());
        }

        // 填充子表数据
        if (!milkCartInfoLeadSealList.isEmpty()) {
            milkCartInfoLeadSealList.forEach(item -> {
                item.setMilkCartInfoLeadSealId(IdUtils.simpleUUID());
                item.setMilkCartInfoId(bsMilkCartInfo.getId());
            });
        }

        // 批量插入子表数据
        if (!milkCartInfoLeadSealList.isEmpty()) {
            bsMilkCartInfoLeadSealMapper.insertBatch(milkCartInfoLeadSealList);
        }

        /*if (!StringUtils.isEmpty(bsMilkCartInfo.getCartLeadSealNumber())){
            // 处理铅封号，去掉空格，去除重复逗号，去除首尾逗号，去除空字符串，去除首尾空格，将逗号全部换为英文逗号
            bsMilkCartInfo.setCartLeadSealNumber(bsMilkCartInfo.getCartLeadSealNumber().replaceAll("\\s+", ""));
            bsMilkCartInfo.setCartLeadSealNumber(bsMilkCartInfo.getCartLeadSealNumber().replaceAll(",+", ","));
            bsMilkCartInfo.setCartLeadSealNumber(bsMilkCartInfo.getCartLeadSealNumber().replaceAll("^,|,$", ""));
            bsMilkCartInfo.setCartLeadSealNumber(bsMilkCartInfo.getCartLeadSealNumber().replaceAll("，", ","));
        }*/

        return bsMilkCartInfoMapper.updateBsMilkCartInfo(bsMilkCartInfo);
    }

    /**
     * 批量删除奶车信息
     * 
     * @param ids 需要删除的奶车信息主键
     * @return 结果
     */
    @Override
    public int deleteBsMilkCartInfoByIds(String[] ids)
    {
        return bsMilkCartInfoMapper.deleteBsMilkCartInfoByIds(ids);
    }

    /**
     * 删除奶车信息信息
     * 
     * @param id 奶车信息主键
     * @return 结果
     */
    @Override
    public int deleteBsMilkCartInfoById(String id)
    {
        return bsMilkCartInfoMapper.deleteBsMilkCartInfoById(id);
    }

    @Override
    public BsMilkCartInfo selectBsMilkCartInfoByDriverCode(String driverCode) {
        Long deptId = SecurityUtils.getDeptId();

        BsMilkCartInfo bsMilkCartInfo = bsMilkCartInfoMapper.selectBsMilkCartInfoByDriverCode(driverCode, deptId.toString());

        if (bsMilkCartInfo == null) {
            throw new ServiceException("奶车信息不存在");
        }

        List<BsMilkCartInfoLeadSeal> bsMilkCartInfoLeadSeals = bsMilkCartInfoLeadSealMapper.selectByMilkCartInfoId(bsMilkCartInfo.getId());
        bsMilkCartInfo.setMilkCartInfoLeadSealList(bsMilkCartInfoLeadSeals);

        return bsMilkCartInfo;
    }
}
