package com.gmlimsqi.business.basicdata.service.impl;

import java.util.*;
import java.util.stream.Collectors;

import com.github.pagehelper.Page;
import com.gmlimsqi.business.basicdata.domain.BsLabtestFeature;
import com.gmlimsqi.business.basicdata.dto.BatchAddItemFeatureDto;
import com.gmlimsqi.business.basicdata.vo.BsLabtestItemFeatureVo;
import com.gmlimsqi.business.util.UserCacheService;
import com.gmlimsqi.business.util.UserInfoProcessor;
import com.gmlimsqi.common.enums.YesNo2Enum;
import com.gmlimsqi.common.utils.CollectionUtils;
import com.gmlimsqi.common.utils.SecurityUtils;
import com.gmlimsqi.common.utils.StringUtils;
import com.gmlimsqi.common.utils.uuid.IdUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.gmlimsqi.business.basicdata.mapper.BsLabtestItemFeatureMapper;
import com.gmlimsqi.business.basicdata.domain.BsLabtestItemFeature;
import com.gmlimsqi.business.basicdata.service.IBsLabtestItemFeatureService;
import org.springframework.transaction.annotation.Transactional;

/**
 * 检测项目特性对应Service业务层处理
 *
 * @author hhy
 * @date 2025-09-05
 */
@Service
public class BsLabtestItemFeatureServiceImpl implements IBsLabtestItemFeatureService
{
    @Autowired
    private BsLabtestItemFeatureMapper bsLabtestItemFeatureMapper;
    @Autowired
    private UserInfoProcessor userInfoProcessor;

    /**
     * 查询检测项目特性对应
     *
     * @param bsLabtestItemFeatureId 检测项目特性对应主键
     * @return 检测项目特性对应
     */
    @Override
    public BsLabtestItemFeature selectBsLabtestItemFeatureByBsLabtestItemFeatureId(String bsLabtestItemFeatureId)
    {
        return bsLabtestItemFeatureMapper.selectBsLabtestItemFeatureByBsLabtestItemFeatureId(bsLabtestItemFeatureId);
    }

    /**
     * 查询检测项目特性对应列表
     *
     * @param bsLabtestItemFeature 检测项目特性对应
     * @return 检测项目特性对应
     */
    @Override
    public List<BsLabtestItemFeature> selectBsLabtestItemFeatureList(BsLabtestItemFeature bsLabtestItemFeature)
    {
        Long deptId = SecurityUtils.getDeptId();
        if (deptId != null){
            bsLabtestItemFeature.setDeptId(deptId.toString());
        }
        List<BsLabtestItemFeature> items = bsLabtestItemFeatureMapper.selectBsLabtestItemFeatureList(bsLabtestItemFeature);

        // 批量处理用户名（创建人和更新人）
        userInfoProcessor.processBaseEntityUserInfo(items);


        return items;
    }

    /**
     * 新增检测项目特性对应
     *
     * @param bsLabtestItemFeature 检测项目特性对应
     * @return 结果
     */
    @Override
    public int insertBsLabtestItemFeature(BsLabtestItemFeature bsLabtestItemFeature)
    {
        //插入前判断是否已有
        BsLabtestItemFeature query = new BsLabtestItemFeature();
        query.setItemId(bsLabtestItemFeature.getItemId());
        List<BsLabtestItemFeature> bsLabtestItemFeatures = bsLabtestItemFeatureMapper.selectBsLabtestItemFeatureList(query);
        if(!CollectionUtils.isAnyEmpty(bsLabtestItemFeatures)){
            throw new RuntimeException("已存在该项目，请勿重复添加");
        }

        if (StringUtils.isEmpty(bsLabtestItemFeature.getBsLabtestItemFeatureId())) {
            bsLabtestItemFeature.setBsLabtestItemFeatureId(IdUtils.simpleUUID());
        }
        if (bsLabtestItemFeature.getDeptId()==null) {
            Long deptId = SecurityUtils.getDeptId();
            if (deptId != null){
                bsLabtestItemFeature.setDeptId(deptId.toString());
            }
        }
        // 自动填充创建/更新信息
        bsLabtestItemFeature.fillCreateInfo();
        return bsLabtestItemFeatureMapper.insertBsLabtestItemFeature(bsLabtestItemFeature);
    }

    /**
     * 修改检测项目特性对应
     *
     * @param bsLabtestItemFeature 检测项目特性对应
     * @return 结果
     */
    @Override
    public int updateBsLabtestItemFeature(BsLabtestItemFeature bsLabtestItemFeature)
    {
        // 自动填充更新信息
        bsLabtestItemFeature.fillUpdateInfo();
        return bsLabtestItemFeatureMapper.updateBsLabtestItemFeature(bsLabtestItemFeature);
    }



    @Override
    public List<BsLabtestItemFeatureVo> selectListGroupByItem(BsLabtestItemFeature bsLabtestItemFeature)
    {
//        List<BsLabtestItemFeatureVo> rawList = bsLabtestItemFeatureMapper.selectListGroupByItem(bsLabtestItemFeature);
//        return new ArrayList<>(rawList);
        // 1. 先分页查询项目ID（这里会受到startPage()的影响）
        List<String> itemIds = bsLabtestItemFeatureMapper.selectItemIdsPage(bsLabtestItemFeature);

        if (itemIds.isEmpty()) {
            // 如果是 Page 对象，需要保持格式返回空 Page，否则前端可能报错
            if (itemIds instanceof Page) {
                Page<BsLabtestItemFeatureVo> emptyPage = new Page<>();
                emptyPage.setTotal(0);
                return emptyPage;
            }
            return new ArrayList<>();
        }

        // 2. 根据项目ID列表查询详细数据（这里不受分页影响）
        List<BsLabtestItemFeature> items = bsLabtestItemFeatureMapper.selectFeatureListByItemIds(itemIds, bsLabtestItemFeature);

        // 3. 按项目分组组装数据
        Map<String, BsLabtestItemFeatureVo> itemMap = new LinkedHashMap<>();

        for (BsLabtestItemFeature item : items) {
            String itemId = item.getItemId();

            if (!itemMap.containsKey(itemId)) {
                // 创建项目VO对象
                BsLabtestItemFeatureVo itemVo = new BsLabtestItemFeatureVo();
                itemVo.setItemId(itemId);
                itemVo.setItemName(item.getItemName());
                itemVo.setBsLabtestItemFeatureId(item.getBsLabtestItemFeatureId());
                itemVo.setRemark(item.getRemark());
                itemVo.setFeatureList(new ArrayList<>());
                itemMap.put(itemId, itemVo);
            }

            // 创建特性明细对象
            BsLabtestItemFeature feature = new BsLabtestItemFeature();
            feature.setBsLabtestItemFeatureId(item.getBsLabtestItemFeatureId());
            feature.setItemId(item.getItemId());
            feature.setItemName(item.getItemName());
            feature.setItemCode(item.getItemCode());
            feature.setItemSapCode(item.getItemSapCode());
            feature.setFeatureId(item.getFeatureId());
            feature.setFeatureName(item.getFeatureName());
            feature.setDeptId(item.getDeptId());
            feature.setIsDelete(item.getIsDelete());
            feature.setIsEnable(item.getIsEnable());
            feature.setUpperLimit(item.getUpperLimit());
            feature.setLowerLimit(item.getLowerLimit());
            feature.setCreateBy(item.getCreateBy());
            feature.setCreateTime(item.getCreateTime());
            feature.setUpdateBy(item.getUpdateBy());
            feature.setUpdateTime(item.getUpdateTime());
            feature.setRemark(item.getRemark());

            // 添加到对应项目的特性列表中
            itemMap.get(itemId).getFeatureList().add(feature);
        }

        // 4. 【关键修改】将 itemIds 的分页信息转移到最终结果
        List<BsLabtestItemFeatureVo> resultList = new ArrayList<>(itemMap.values());

        if (itemIds instanceof Page) {
            Page<String> idPage = (Page<String>) itemIds;
            // 创建一个新的 Page 对象作为返回结果
            Page<BsLabtestItemFeatureVo> resultPage = new Page<>();
            // 复制分页元数据
            resultPage.setTotal(idPage.getTotal());
            resultPage.setPageNum(idPage.getPageNum());
            resultPage.setPageSize(idPage.getPageSize());
            resultPage.setPages(idPage.getPages());

            // 添加实际数据
            resultPage.addAll(resultList);
            return resultPage;
        }

        return resultList;
    }

    @Override
    @Transactional
    public int batchAdd(BatchAddItemFeatureDto batchAddDto) {
        //插入前判断是否已有
        BsLabtestItemFeature query = new BsLabtestItemFeature();
        query.setItemId(batchAddDto.getItemId());
        List<BsLabtestItemFeature> bsLabtestItemFeatures = bsLabtestItemFeatureMapper.selectBsLabtestItemFeatureList(query);
        if(!CollectionUtils.isAnyEmpty(bsLabtestItemFeatures)){
            throw new RuntimeException("已存在该项目，请勿重复添加");
        }
        List<BsLabtestItemFeature> addList = new ArrayList<>();
        for (String featureId : batchAddDto.getFeatureIds()) {
            BsLabtestItemFeature bsLabtestItemFeature = new BsLabtestItemFeature();
            bsLabtestItemFeature.setFeatureId(featureId);
            bsLabtestItemFeature.setItemId(batchAddDto.getItemId());
            bsLabtestItemFeature.setIsEnable(YesNo2Enum.YES.getCode());
            bsLabtestItemFeature.setRemark(batchAddDto.getRemark());
            if (StringUtils.isEmpty(bsLabtestItemFeature.getBsLabtestItemFeatureId())) {
                bsLabtestItemFeature.setBsLabtestItemFeatureId(IdUtils.simpleUUID());
            }
            if (bsLabtestItemFeature.getDeptId()==null) {
                Long deptId = SecurityUtils.getDeptId();
                if (deptId != null){
                    bsLabtestItemFeature.setDeptId(deptId.toString());
                }
            }
            // 自动填充创建/更新信息
            bsLabtestItemFeature.fillCreateInfo();
            addList.add(bsLabtestItemFeature);
        }
        bsLabtestItemFeatureMapper.insertBatch(addList);
        return batchAddDto.getFeatureIds().size();
    }

    @Override
    @Transactional
    public int batchUpdate(BatchAddItemFeatureDto batchUpdateDto) {
        // 1. 先删除该项目下的所有特性关联
        bsLabtestItemFeatureMapper.updateDeleteFlagByItemId(batchUpdateDto.getItemId(),SecurityUtils.getUserId().toString());
        // 2. 重新添加新的特性关联
        return batchAdd(batchUpdateDto);
    }

    @Override
    public List<BsLabtestItemFeature> selectBsLabtestItemFeatureByItemId(String itemId) {
        BsLabtestItemFeature query = new BsLabtestItemFeature();
        query.setItemId(itemId);
        List<BsLabtestItemFeature> items = bsLabtestItemFeatureMapper.selectBsLabtestItemFeatureList(query);

        // 批量处理用户名（创建人和更新人）
        userInfoProcessor.processBaseEntityUserInfo(items);
        return items;
    }

    @Override
    public int updateDeleteFlagById(String bsLabtestItemFeatureId) {
        return bsLabtestItemFeatureMapper.updateDeleteFlagById(bsLabtestItemFeatureId,SecurityUtils.getUserId().toString());
    }
}