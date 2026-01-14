package com.gmlimsqi.business.bsweighbridge.service.impl;

import java.util.*;
import java.util.stream.Collectors;

import com.gmlimsqi.business.bsweighbridge.domain.BsWeighBridge;
import com.gmlimsqi.business.bsweighbridge.mapper.BsWeighBridgeMapper;
import com.gmlimsqi.business.bsweighbridge.service.IBsWeighBridgeService;
import com.gmlimsqi.common.utils.StringUtils;
import com.gmlimsqi.common.utils.uuid.IdUtils;
import com.gmlimsqi.business.util.UserCacheService;
import com.gmlimsqi.common.utils.SecurityUtils;
import com.gmlimsqi.common.core.domain.model.LoginUser;
import com.gmlimsqi.common.annotation.DataScope;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 过磅单Service业务层处理
 * * @author hhy
 * @date 2025-11-13
 */
@Service
public class BsWeighBridgeServiceImpl implements IBsWeighBridgeService
{
    @Autowired
    private BsWeighBridgeMapper bsWeighBridgeMapper;
    @Autowired
    private UserCacheService userCacheService;

    /**
     * 查询过磅单
     * * @param id 过磅单主键
     * @return 过磅单
     */
    @Override
    public BsWeighBridge selectBsWeighBridgeById(String id)
    {
        return bsWeighBridgeMapper.selectBsWeighBridgeById(id);
    }

    /**
     * 查询过磅单列表
     * * @param bsWeighBridge 过磅单
     * @return 过磅单
     */
    // +++ 新增：@DataScope 注解以激活数据权限切面 +++
    @Override
    public List<BsWeighBridge> selectBsWeighBridgeList(BsWeighBridge bsWeighBridge)
    {
        List<BsWeighBridge> items = bsWeighBridgeMapper.selectBsWeighBridgeList(bsWeighBridge);

        // 批量处理用户名
        if (!items.isEmpty()) {
            Set<String> userIds = items.stream()
                    .map(BsWeighBridge::getCreateBy)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toSet());

            Map<String, String> usernameMap = userCacheService.batchGetUsernames(userIds);

            items.forEach(vo ->
                    vo.setCreateBy(usernameMap.get(vo.getCreateBy())));
        }

        return items;
    }

    /**
     * 新增过磅单
     * * @param bsWeighBridge 过磅单
     * @return 结果
     */
    @Override
    public int insertBsWeighBridge(BsWeighBridge bsWeighBridge)
    {
        if (StringUtils.isEmpty(bsWeighBridge.getId())) {
        bsWeighBridge.setId(IdUtils.simpleUUID());
    }

        // +++ 自动填充部门ID，用于数据权限 +++
        /*LoginUser loginUser = SecurityUtils.getLoginUser();
        if (StringUtils.isNotNull(loginUser))
        {
            // 假设您的实体类中有名为 setDeptId 的方法
            // (请确保您的 BsWeighBridge 实体类中有 setDeptId(String deptId) 方法)
            bsWeighBridge.set(loginUser.getDeptId().toString());
        }*/
        // +++  +++

        // 自动填充创建/更新信息
        bsWeighBridge.fillCreateInfo();
            return bsWeighBridgeMapper.insertBsWeighBridge(bsWeighBridge);
    }

    /**
     * 修改过磅单
     * * @param bsWeighBridge 过磅单
     * @return 结果
     */
    @Override
    public int updateBsWeighBridge(BsWeighBridge bsWeighBridge)
    {
        // 自动填充更新信息
        bsWeighBridge.fillUpdateInfo();
        return bsWeighBridgeMapper.updateBsWeighBridge(bsWeighBridge);
    }

    @Override
    public int uploadBsWeighBridge(BsWeighBridge bsWeighBridge) {
        bsWeighBridge.setVoidOrNot("0");
        bsWeighBridge.setIsend("0");
        bsWeighBridge.setPrintCount("0");
        bsWeighBridge.setIsUpload("0");

        bsWeighBridge.setGmFlag(
                StringUtils.isEmpty(bsWeighBridge.getGmweight())
                        || "0.0".equals(bsWeighBridge.getGmweight())
                        ? "0" : "1");
        bsWeighBridge.setGpFlag(
                StringUtils.isEmpty(bsWeighBridge.getGpweight())
                        || "0.0".equals(bsWeighBridge.getGpweight())
                        ? "0" : "1");

        String cweightno = bsWeighBridge.getCweightno();
        if (StringUtils.isEmpty(cweightno)) {
            throw new RuntimeException("磅单号不能为空");
        }

        String weightId = "";
        // 判断磅单号是否已经上传过，未上传过保存，已上传过修改
        BsWeighBridge weightBridge =
                bsWeighBridgeMapper.IsExist(cweightno);

        if (ObjectUtils.isNotEmpty(weightBridge)) {
            weightId = weightBridge.getId();
        } else {
            weightId = IdUtils.fastUUID();
            bsWeighBridge.setId(weightId);
        }

        // 车牌号
        String driverCode = bsWeighBridge.getDriverCode();

        bsWeighBridge.setIsUsed("0");

        if (StringUtils.isEmpty(driverCode)) {
            throw new RuntimeException("司机车牌号不能为空");
        }

        int row;

        if (ObjectUtils.isNotEmpty(weightBridge)) {
            bsWeighBridge.setUpdateTime(new Date());
            row = bsWeighBridgeMapper.updateBsWeighBridgeByCweightno(bsWeighBridge);
        } else {
            bsWeighBridge.setCreateTime(new Date());
            row = bsWeighBridgeMapper.insertBsWeighBridge(bsWeighBridge);
        }

        return row;
    }

}