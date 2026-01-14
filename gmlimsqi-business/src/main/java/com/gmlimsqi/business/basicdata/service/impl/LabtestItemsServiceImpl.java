package com.gmlimsqi.business.basicdata.service.impl;

import java.util.*;
import java.util.stream.Collectors;

import cn.hutool.core.collection.CollectionUtil;
import com.gmlimsqi.business.basicdata.domain.BsItemDept;
import com.gmlimsqi.business.basicdata.domain.BsItemInstrument;
import com.gmlimsqi.business.basicdata.mapper.BsInvbillItemStandardMapper;
import com.gmlimsqi.business.basicdata.mapper.BsItemDeptMapper;
import com.gmlimsqi.business.basicdata.mapper.BsItemInstrumentMapper;
import com.gmlimsqi.business.util.UserInfoProcessor;
import com.gmlimsqi.common.exception.ServiceException;
import com.gmlimsqi.common.utils.SecurityUtils;
import com.gmlimsqi.common.utils.StringUtils;
import com.gmlimsqi.common.utils.uuid.IdUtils;
import com.gmlimsqi.system.mapper.SysUploadFileMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.gmlimsqi.business.basicdata.mapper.LabtestItemsMapper;
import com.gmlimsqi.business.basicdata.domain.LabtestItems;
import com.gmlimsqi.business.basicdata.service.ILabtestItemsService;
import org.springframework.transaction.annotation.Transactional;
// 新增引入
import com.gmlimsqi.business.basicdata.mapper.BsItemDeptConfigMapper;
import com.gmlimsqi.business.basicdata.domain.BsItemDeptConfig;
import cn.hutool.core.collection.CollectionUtil;


/**
 * 检测项目Service业务层处理
 * 
 * @author hhy
 * @date 2025-08-05
 */
@Service
public class LabtestItemsServiceImpl implements ILabtestItemsService
{
    @Autowired
    private LabtestItemsMapper labtestItemsMapper;
    @Autowired
    private BsItemDeptMapper itemDeptMapper;
    @Autowired
    private UserInfoProcessor userInfoProcessor;
    @Autowired
    private BsItemInstrumentMapper instrumentMapper;
    @Autowired
    private BsInvbillItemStandardMapper invbillItemStandardMapper;
    // 注入新Mapper
    @Autowired
    private BsItemDeptConfigMapper bsItemDeptConfigMapper;

    /**
     * 查询检测项目
     * * @param LabtestItemsId 检测项目主键
     * @return 检测项目
     */
    @Override
    public LabtestItems selectBsLabtestItemsByLabtestItemsId(String LabtestItemsId)
    {
        // 1. 查询主表信息
        LabtestItems items = labtestItemsMapper.selectBsLabtestItemsByLabtestItemsId(LabtestItemsId);

        if (items != null) {
            // 2. 查询配置列表
            // 因为 Mapper XML 中已经直接查询了 instrument_name, instrument_code, location_name 字段
            // 所以这里直接获取到的 list 对象中，名称和编码字段已经是逗号分隔的字符串了，无需额外处理。
            List<BsItemDeptConfig> configList = bsItemDeptConfigMapper.selectConfigListByItemId(LabtestItemsId);

            items.setBsItemDeptConfigList(configList);
        }
        return items;
    }
    /**
     * 查询检测项目列表
     * 
     * @param labtestItems 检测项目
     * @return 检测项目
     */
    @Override
    public List<LabtestItems> selectBsLabtestItemsList(LabtestItems labtestItems)
    {

        List<LabtestItems> items = labtestItemsMapper.selectBsLabtestItemsList(labtestItems);

        // 批量处理用户名（创建人和更新人）
        userInfoProcessor.processBaseEntityUserInfo(items);

        return items;
    }

    /**
     * 新增检测项目
     * 
     * @param labtestItems 检测项目
     * @return 结果
     */
    @Override
    public int insertBsLabtestItems(LabtestItems labtestItems)
    {
        if (StringUtils.isEmpty(labtestItems.getLabtestItemsId())) {
            labtestItems.setLabtestItemsId(IdUtils.simpleUUID());
        }
        // 1. 验证itemCode唯一性
        if (labtestItemsMapper.isItemCodeExist(labtestItems.getItemCode(), null)) {
            throw new ServiceException("检测项目编码已存在");
        }
        // 自动填充创建/更新信息
        labtestItems.fillCreateInfo();
        int count = labtestItemsMapper.insertBsLabtestItems(labtestItems);
        insertItemDeptConfig(labtestItems);
        return count ;
    }

    /**
     * 修改检测项目
     * 
     * @param labtestItems 检测项目
     * @return 结果
     */
    @Override
    @Transactional
    public int updateBsLabtestItems(LabtestItems labtestItems)
    {
        // 更新时需要排除自身ID
        if (labtestItemsMapper.isItemCodeExist(labtestItems.getItemCode(), labtestItems.getLabtestItemsId())) {
            throw new ServiceException("检测项目编码已存在");
        }
        // 自动填充更新信息
        labtestItems.fillUpdateInfo();
        int count = labtestItemsMapper.updateBsLabtestItems(labtestItems);
        // 2. 更新配置表：先删后增
        bsItemDeptConfigMapper.deleteByItemId(labtestItems.getLabtestItemsId());
        insertItemDeptConfig(labtestItems);

        return count ;
    }
    /**
     * 辅助方法：批量插入配置
     */
    private void insertItemDeptConfig(LabtestItems labtestItems) {
        List<BsItemDeptConfig> configList = labtestItems.getBsItemDeptConfigList();
        if (CollectionUtil.isNotEmpty(configList)) {
            String loginName = SecurityUtils.getUsername();
            for (BsItemDeptConfig config : configList) {
                // 生成主键
                config.setConfigId(IdUtils.simpleUUID());
                // 关联项目ID
                config.setItemId(labtestItems.getLabtestItemsId());
                // 设置创建人
                config.setCreateBy(loginName);

                // 注意：config.getInstrumentId() 和 config.getLocationId()
                // 现在已经是逗号分隔的字符串了，直接存即可，不需要额外处理。
            }
            // 批量插入
            bsItemDeptConfigMapper.batchInsert(configList);

            // 同步更新标准表中的设备信息
            syncInstrumentsToStandard(labtestItems, configList);
        }
    }
    /**
     * 同步设备信息到标准表 (更新逻辑修改：直接使用逗号分隔的字符串)
     */
    private void syncInstrumentsToStandard(LabtestItems labtestItems, List<BsItemDeptConfig> configList) {
        if (CollectionUtil.isEmpty(configList)) {
            return;
        }

        // 遍历每个部门的配置
        for (BsItemDeptConfig config : configList) {
            // 获取该部门下的设备ID字符串 (如 "1,2,3")
            String instrumentIdsStr = config.getInstrumentId();
            String deptId = config.getDeptId();

            // 如果有设备配置，则更新标准表
            if (StringUtils.isNotEmpty(instrumentIdsStr)) {
                // 调用 Mapper，直接传入逗号分隔的字符串
                invbillItemStandardMapper.updateInstrumentsByItemIdDeptId(
                        labtestItems.getLabtestItemsId(),
                        instrumentIdsStr,
                        String.valueOf(deptId)
                );
            } else {
                // 如果设备为空（可能是用户清空了配置），也可以选择清空标准表里的字段
                // 视业务需求而定，这里加上清空逻辑更严谨
                invbillItemStandardMapper.updateInstrumentsByItemIdDeptId(
                        labtestItems.getLabtestItemsId(),
                        "", // 传空串或 null
                        String.valueOf(deptId)
                );
            }
        }
    }
    /**
     * 校验项目编码唯一性 (保留原有逻辑)
     */
    private void checkItemCodeUnique(LabtestItems labtestItems) {
        if(labtestItemsMapper.isItemCodeExist(labtestItems.getItemCode(), labtestItems.getLabtestItemsId())){
            throw new ServiceException("项目编码已存在");
        }
    }
    /**
     * 修改启用状态
     *
     * @param labtestItems 检测项目
     * @return 结果
     */
    @Override
    public int updateEnableById(LabtestItems labtestItems)
    {
        // 自动填充更新信息
        labtestItems.fillUpdateInfo();
        return labtestItemsMapper.updateEnableById(labtestItems);
    }

    /**
     * 批量删除检测项目
     * 
     * @param LabtestItemsIds 需要删除的检测项目主键
     * @return 结果
     */
    @Override
    public int deleteBsLabtestItemsByLabtestItemsIds(String[] LabtestItemsIds)
    {
        // 删除配置表数据
        for (String itemId : LabtestItemsIds) {
            bsItemDeptConfigMapper.deleteByItemId(itemId);
        }
        // 删除主表数据
        return labtestItemsMapper.deleteBsLabtestItemsByLabtestItemsIds(LabtestItemsIds);
    }

    /**
     * 删除检测项目信息
     * 
     * @param LabtestItemsId 检测项目主键
     * @return 结果
     */
    @Override
    public int deleteBsLabtestItemsByLabtestItemsId(String LabtestItemsId)
    {
        return labtestItemsMapper.deleteBsLabtestItemsByLabtestItemsId(LabtestItemsId);
    }


}
