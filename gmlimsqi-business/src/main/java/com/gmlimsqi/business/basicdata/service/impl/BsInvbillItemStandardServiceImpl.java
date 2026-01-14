package com.gmlimsqi.business.basicdata.service.impl;

import java.util.*;
import java.util.stream.Collectors;

import com.github.pagehelper.Page;
import com.gmlimsqi.business.basicdata.domain.UserLabtestItem;
import com.gmlimsqi.business.basicdata.dto.BatchAddStandardDto;
import com.gmlimsqi.business.basicdata.dto.MaterialItemDTO;
import com.gmlimsqi.business.basicdata.vo.BsInvbillItemStandardVo;
import com.gmlimsqi.business.util.UserCacheService;
import com.gmlimsqi.business.util.UserInfoProcessor;
import com.gmlimsqi.common.core.domain.entity.SysUser;
import com.gmlimsqi.common.enums.YesNo2Enum;
import com.gmlimsqi.common.exception.ServiceException;
import com.gmlimsqi.common.utils.DateUtils;
import com.gmlimsqi.common.utils.SecurityUtils;
import com.gmlimsqi.common.utils.StringUtils;
import com.gmlimsqi.common.utils.uuid.IdUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.gmlimsqi.business.basicdata.mapper.BsInvbillItemStandardMapper;
import com.gmlimsqi.business.basicdata.domain.BsInvbillItemStandard;
import com.gmlimsqi.business.basicdata.service.IBsInvbillItemStandardService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

/**
 * 物料项目标准Service业务层处理
 * 
 * @author hhy
 * @date 2025-09-08
 */
@Service
public class BsInvbillItemStandardServiceImpl implements IBsInvbillItemStandardService 
{
    @Autowired
    private BsInvbillItemStandardMapper bsInvbillItemStandardMapper;
    @Autowired
    private UserCacheService userCacheService;
    @Autowired
    private UserInfoProcessor userInfoProcessor;

    @Override
    @Transactional
    public int batchAdd(BatchAddStandardDto batchAddDto) {
        int count = 0;

        if (batchAddDto.getStandardList() != null && !batchAddDto.getStandardList().isEmpty()) {
            for (BsInvbillItemStandard standard : batchAddDto.getStandardList()) {
                // 设置公共信息
                standard.setInvbillId(batchAddDto.getInvbillId());
                standard.setInvbillCode(batchAddDto.getInvbillCode());
                standard.setDeptId(batchAddDto.getDeptId());
                standard.setBsInvbillItemStandardId(IdUtils.simpleUUID());
                count += insertBsInvbillItemStandard(standard);
            }
        }

        return count;
    }

    @Override
    @Transactional
    public int batchUpdate(BatchAddStandardDto batchUpdateDto) {
        // 1. 先删除该物料下的所有配置
        bsInvbillItemStandardMapper.updateDeleteFlagByInvbillCode(batchUpdateDto.getInvbillCode(),SecurityUtils.getUserId().toString(),batchUpdateDto.getDeptId());

        // 2. 重新添加新的配置
        return batchAdd(batchUpdateDto);
    }

    @Override
    public List<BsInvbillItemStandardVo> selectListGroupByInvbill(BsInvbillItemStandard bsInvbillItemStandard) {

        // 管理员显示所有菜单信息
        if (SysUser.isAdmin(SecurityUtils.getUserId()))
        {
            bsInvbillItemStandard.setDeptId("");
        }else {
            bsInvbillItemStandard.setDeptId(SecurityUtils.getDeptId().toString());
        }

        if (StringUtils.isBlank(bsInvbillItemStandard.getInvbillCode())) {
            // 1. 先分页查询物料编码（这里会受到startPage()的影响）
            List<String> invbillCodes = bsInvbillItemStandardMapper.selectInvbillCodesPage(bsInvbillItemStandard);

            if (invbillCodes.isEmpty()) {
                return new ArrayList<>();
            }

            // 2. 根据物料ID列表查询详细数据（这里不受分页影响）
            List<BsInvbillItemStandard> items = bsInvbillItemStandardMapper.selectStandardListByInvbillCodes(invbillCodes, bsInvbillItemStandard);

            // 3. 按物料分组组装数据
            Map<String, BsInvbillItemStandardVo> invbillMap = new LinkedHashMap<>();

            for (BsInvbillItemStandard item : items) {
                String invbillId = item.getInvbillId();
                String invbillCode = item.getInvbillCode();
                if (!invbillMap.containsKey(invbillCode)) {
                    // 创建物料VO对象
                    BsInvbillItemStandardVo invbillVo = new BsInvbillItemStandardVo();
                    invbillVo.setInvbillId(invbillId);
                    invbillVo.setInvbillCode(invbillCode);
                    invbillVo.setInvbillName(item.getInvbillName());
                    invbillVo.setDeptId(item.getDeptId());
                    invbillVo.setDeptName(item.getDeptName());
                    invbillVo.setCreateBy(item.getCreateBy());
                    invbillVo.setCreateTime(item.getCreateTime());
                    invbillVo.setUpdateBy(item.getUpdateBy());
                    invbillVo.setUpdateTime(item.getUpdateTime());
                    invbillVo.setRemark(item.getRemark());
                    invbillVo.setStandardList(new ArrayList<>());
                    invbillMap.put(invbillCode, invbillVo);
                }

                // 创建标准明细对象
                BsInvbillItemStandard standard = new BsInvbillItemStandard();
                standard.setBsInvbillItemStandardId(item.getBsInvbillItemStandardId());
                standard.setInvbillId(item.getInvbillId());
                standard.setInvbillCode(item.getInvbillCode());
                standard.setDeptId(item.getDeptId());
                standard.setItemId(item.getItemId());
                standard.setFeatureId(item.getFeatureId());
                standard.setZxbz(item.getZxbz());
                standard.setInstrumentId(item.getInstrumentId());
                standard.setIsDelete(item.getIsDelete());
                standard.setIsEnable(item.getIsEnable());
                standard.setIsBj(item.getIsBj());
                standard.setIsCj(item.getIsCj());
                standard.setResultAddType(item.getResultAddType());
                standard.setItemName(item.getItemName());
                standard.setFeatureName(item.getFeatureName());
                standard.setUpperLimit(item.getUpperLimit());
                standard.setLowerLimit(item.getLowerLimit());
                standard.setInstrumentName(item.getInstrumentName());
                standard.setCreateBy(item.getCreateBy());
                standard.setCreateTime(item.getCreateTime());
                standard.setUpdateBy(item.getUpdateBy());
                standard.setUpdateTime(item.getUpdateTime());
                standard.setRemark(item.getRemark());

                // 添加到对应物料的标准列表中
                invbillMap.get(invbillCode).getStandardList().add(standard);
            }
            // 不要直接返回 new ArrayList，而是创建一个 Page 对象来承载结果
            Page<BsInvbillItemStandardVo> resultPage = new Page<>();

            // 将原查询结果(invbillCodes)的分页参数转移过来
            if (invbillCodes instanceof Page) {
                Page<String> sourcePage = (Page<String>) invbillCodes;
                resultPage.setTotal(sourcePage.getTotal());    // 转移总数
                resultPage.setPageNum(sourcePage.getPageNum());
                resultPage.setPageSize(sourcePage.getPageSize());
                resultPage.setPages(sourcePage.getPages());
            }

            // 将组装好的 VO 数据放入这个带分页信息的 Page 对象中
            resultPage.addAll(invbillMap.values());

            return resultPage;
            //return new ArrayList<>(invbillMap.values());
        }else {
            // 1. 先分页查询物料编码（这里会受到startPage()的影响）
            List<String> invbillCodes = bsInvbillItemStandardMapper.selectInvbillCodesPage(bsInvbillItemStandard);

            if (invbillCodes.isEmpty()) {
                return new ArrayList<>();
            }

            // 2. 根据物料ID列表查询详细数据（这里不受分页影响）
            List<BsInvbillItemStandard> items = bsInvbillItemStandardMapper.selectStandardListByInvbillCodes(invbillCodes, bsInvbillItemStandard);

            // 3. 按物料分组组装数据
            Map<String, BsInvbillItemStandardVo> invbillMap = new LinkedHashMap<>();
            // 根据物料编码筛选该物料的标准
            for (BsInvbillItemStandard itemStandard : items){
                if (itemStandard.getInvbillCode().equals(bsInvbillItemStandard.getInvbillCode())){
                    // 判断是否有值，没有则不添加
                    if (StringUtils.isEmpty(itemStandard.getItemId())){
                        continue;
                    }

                    if (!invbillMap.containsKey(bsInvbillItemStandard.getInvbillCode())) {
                        // 创建物料VO对象
                        BsInvbillItemStandardVo invbillVo = new BsInvbillItemStandardVo();
                        invbillVo.setInvbillId(itemStandard.getInvbillId());
                        invbillVo.setInvbillCode(bsInvbillItemStandard.getInvbillCode());
                        invbillVo.setInvbillName(itemStandard.getInvbillName());
                        invbillVo.setDeptId(itemStandard.getDeptId());
                        invbillVo.setDeptName(itemStandard.getDeptName());
                        invbillVo.setCreateBy(itemStandard.getCreateBy());
                        invbillVo.setCreateTime(itemStandard.getCreateTime());
                        invbillVo.setUpdateBy(itemStandard.getUpdateBy());
                        invbillVo.setUpdateTime(itemStandard.getUpdateTime());
                        invbillVo.setRemark(itemStandard.getRemark());
                        invbillVo.setStandardList(new ArrayList<>());
                        invbillMap.put(bsInvbillItemStandard.getInvbillCode(), invbillVo);
                    }

                    // 创建标准明细对象
                    BsInvbillItemStandard standard = new BsInvbillItemStandard();
                    standard.setBsInvbillItemStandardId(itemStandard.getBsInvbillItemStandardId());
                    standard.setInvbillId(itemStandard.getInvbillId());
                    standard.setInvbillCode(itemStandard.getInvbillCode());
                    standard.setDeptId(itemStandard.getDeptId());
                    standard.setItemId(itemStandard.getItemId());
                    standard.setFeatureId(itemStandard.getFeatureId());
                    standard.setZxbz(itemStandard.getZxbz());
                    standard.setInstrumentId(itemStandard.getInstrumentId());
                    standard.setIsDelete(itemStandard.getIsDelete());
                    standard.setIsEnable(itemStandard.getIsEnable());
                    standard.setIsBj(itemStandard.getIsBj());
                    standard.setIsCj(itemStandard.getIsCj());
                    standard.setResultAddType(itemStandard.getResultAddType());
                    standard.setItemName(itemStandard.getItemName());
                    standard.setFeatureName(itemStandard.getFeatureName());
                    standard.setUpperLimit(itemStandard.getUpperLimit());
                    standard.setLowerLimit(itemStandard.getLowerLimit());
                    standard.setInstrumentName(itemStandard.getInstrumentName());
                    standard.setCreateBy(itemStandard.getCreateBy());
                    standard.setCreateTime(itemStandard.getCreateTime());
                    standard.setUpdateBy(itemStandard.getUpdateBy());
                    standard.setUpdateTime(itemStandard.getUpdateTime());
                    standard.setRemark(itemStandard.getRemark());

                    invbillMap.get(bsInvbillItemStandard.getInvbillCode()).getStandardList().add(itemStandard);
                }
            }

            return new ArrayList<>(invbillMap.values());
        }
    }


    /**
     * 查询物料项目标准
     * 
     * @param bsInvbillItemStandardId 物料项目标准主键
     * @return 物料项目标准
     */
    @Override
    public BsInvbillItemStandard selectBsInvbillItemStandardByBsInvbillItemStandardId(String bsInvbillItemStandardId)
    {
        return bsInvbillItemStandardMapper.selectBsInvbillItemStandardByBsInvbillItemStandardId(bsInvbillItemStandardId);
    }

    /**
     * 查询物料项目标准列表
     * 
     * @param bsInvbillItemStandard 物料项目标准
     * @return 物料项目标准
     */
    @Override
    public List<BsInvbillItemStandard> selectBsInvbillItemStandardList(BsInvbillItemStandard bsInvbillItemStandard)
    {
        List<BsInvbillItemStandard> items = bsInvbillItemStandardMapper.selectBsInvbillItemStandardList(bsInvbillItemStandard);

        // 批量处理用户名
        if (!items.isEmpty()) {
            Set<String> userIds = items.stream()
                    .map(BsInvbillItemStandard::getCreateBy)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toSet());

            Map<String, String> usernameMap = userCacheService.batchGetUsernames(userIds);

            items.forEach(vo ->
                    vo.setCreateBy(usernameMap.get(vo.getCreateBy())));
        }

        return items;
    }

    /**
     * 新增物料项目标准
     * 
     * @param bsInvbillItemStandard 物料项目标准
     * @return 结果
     */
    @Override
    public int insertBsInvbillItemStandard(BsInvbillItemStandard bsInvbillItemStandard)
    {
        if (bsInvbillItemStandard.getDeptId() == null) {
            Long deptId = SecurityUtils.getDeptId();
            bsInvbillItemStandard.setDeptId(deptId.toString());
        }
        if (StringUtils.isEmpty(bsInvbillItemStandard.getBsInvbillItemStandardId())) {
            bsInvbillItemStandard.setBsInvbillItemStandardId(IdUtils.simpleUUID());
        }
        // 自动填充创建/更新信息
        bsInvbillItemStandard.fillCreateInfo();
        return bsInvbillItemStandardMapper.insertBsInvbillItemStandard(bsInvbillItemStandard);
    }

    /**
     * 修改物料项目标准
     * 
     * @param bsInvbillItemStandard 物料项目标准
     * @return 结果
     */
    @Override
    public int updateBsInvbillItemStandard(BsInvbillItemStandard bsInvbillItemStandard)
    {
        // 自动填充更新信息
        bsInvbillItemStandard.fillUpdateInfo();
        return bsInvbillItemStandardMapper.updateBsInvbillItemStandard(bsInvbillItemStandard);
    }


    @Override
    public List<BsInvbillItemStandardVo> selectBsInvbillItemStandardByBsInvbillCode(String invbillCode) {
        BsInvbillItemStandard query = new BsInvbillItemStandard();
        query.setInvbillCode(invbillCode);
        query.setDeptId(SecurityUtils.getDeptId().toString());
        List<BsInvbillItemStandardVo> rawList = bsInvbillItemStandardMapper.selectListGroupByInvbill(query);
        // 批量处理用户名（创建人和更新人）
        userInfoProcessor.processBaseEntityUserInfo(rawList);
        return rawList;
    }

    @Override
    public int updateDeleteFlagById(String bsInvbillItemStandardId) {
        return bsInvbillItemStandardMapper.updateDeleteFlagById(bsInvbillItemStandardId,SecurityUtils.getUserId().toString());
    }

    @Override
    public BsInvbillItemStandard selectByBsInvbillItemId(String invbillCode, String itemId) {
        BsInvbillItemStandard query = new BsInvbillItemStandard();
        query.setInvbillCode(invbillCode);
        query.setItemId(itemId);
        query.setIsEnable(YesNo2Enum.YES.getCode());
        List<BsInvbillItemStandard> standardList = bsInvbillItemStandardMapper.selectBsInvbillItemStandardList(query);
        if(CollectionUtils.isEmpty(standardList)){
            return standardList.get(0);
        }
        return null;
    }

    @Override
    public List<BsInvbillItemStandard> getItemByInvbillCode(MaterialItemDTO materialItemDTO) {
        if (StringUtils.isEmpty(materialItemDTO.getInvbillCode())){
            return new ArrayList<>();
        }

        Long deptId = SecurityUtils.getDeptId();

        if (deptId == null){
            throw new ServiceException("获取部门失败");
        }

        materialItemDTO.setDeptId(deptId.toString());
        return bsInvbillItemStandardMapper.getItemByInvbillCode(materialItemDTO);
    }
}
