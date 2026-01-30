package com.gmlimsqi.business.basicdata.service.impl;

import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.List;

import com.gmlimsqi.business.util.UserCacheService;
import com.gmlimsqi.common.annotation.DataScope;
import com.gmlimsqi.common.core.domain.entity.SysDept;
import com.gmlimsqi.common.utils.SecurityUtils;
import com.gmlimsqi.common.utils.StringUtils;
import com.gmlimsqi.common.utils.uuid.IdUtils;
import com.gmlimsqi.system.mapper.SysDeptMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.gmlimsqi.business.basicdata.mapper.BsMilkWarehouseMapper;
import com.gmlimsqi.business.basicdata.domain.BsMilkWarehouse;
import com.gmlimsqi.business.basicdata.service.IBsMilkWarehouseService;

/**
 * 奶仓档案Service业务层处理
 * 
 * @author hhy
 * @date 2025-11-05
 */
@Service
public class BsMilkWarehouseServiceImpl implements IBsMilkWarehouseService 
{
    @Autowired
    private BsMilkWarehouseMapper bsMilkWarehouseMapper;
    @Autowired
    private UserCacheService userCacheService;

    @Autowired
    private SysDeptMapper sysDeptMapper;

    /**
     * 查询奶仓档案
     * 
     * @param id 奶仓档案主键
     * @return 奶仓档案
     */
    @Override
    public BsMilkWarehouse selectBsMilkWarehouseById(String id)
    {
        BsMilkWarehouse bsMilkWarehouse = bsMilkWarehouseMapper.selectBsMilkWarehouseById(id);

        if (bsMilkWarehouse != null) {
            SysDept dept = sysDeptMapper.selectDeptById(Long.parseLong(bsMilkWarehouse.getDeptId()));
            bsMilkWarehouse.setDeptName(dept.getDeptName());
        }

        return bsMilkWarehouse;
    }

    /**
     * 查询奶仓档案列表
     * 
     * @param bsMilkWarehouse 奶仓档案
     * @return 奶仓档案
     */
    @Override
//    @DataScope(deptAlias = "d")
    public List<BsMilkWarehouse> selectBsMilkWarehouseList(BsMilkWarehouse bsMilkWarehouse)
    {
        if (!SecurityUtils.isAdmin(SecurityUtils.getUserId())){
            bsMilkWarehouse.setDeptId(SecurityUtils.getDeptId().toString());
        }
        List<BsMilkWarehouse> items = bsMilkWarehouseMapper.selectBsMilkWarehouseList(bsMilkWarehouse);

        // 批量处理用户名
        if (!items.isEmpty()) {
            Set<String> userIds = items.stream()
                    .map(BsMilkWarehouse::getCreateBy)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toSet());

            Map<String, String> usernameMap = userCacheService.batchGetUsernames(userIds);

            items.forEach(vo ->
                    vo.setCreateBy(usernameMap.get(vo.getCreateBy())));
        }

        return items;
    }

    /**
     * 新增奶仓档案
     * 
     * @param bsMilkWarehouse 奶仓档案
     * @return 结果
     */
    @Override
    public int insertBsMilkWarehouse(BsMilkWarehouse bsMilkWarehouse)
    {
        if (StringUtils.isEmpty(bsMilkWarehouse.getId())) {
            bsMilkWarehouse.setId(IdUtils.simpleUUID());
        }

        Long deptId = SecurityUtils.getDeptId();

        bsMilkWarehouse.setDeptId(deptId.toString());
        bsMilkWarehouse.setWarehouseStatus("0");

        // 自动填充创建/更新信息
        bsMilkWarehouse.fillCreateInfo();
        return bsMilkWarehouseMapper.insertBsMilkWarehouse(bsMilkWarehouse);
    }

    /**
     * 修改奶仓档案
     * 
     * @param bsMilkWarehouse 奶仓档案
     * @return 结果
     */
    @Override
    public int updateBsMilkWarehouse(BsMilkWarehouse bsMilkWarehouse)
    {
        // 自动填充更新信息
        bsMilkWarehouse.fillUpdateInfo();
        return bsMilkWarehouseMapper.updateBsMilkWarehouse(bsMilkWarehouse);
    }

    /**
     * 批量删除奶仓档案
     * 
     * @param ids 需要删除的奶仓档案主键
     * @return 结果
     */
    @Override
    public int deleteBsMilkWarehouseByIds(String[] ids)
    {
        return bsMilkWarehouseMapper.deleteBsMilkWarehouseByIds(ids);
    }

    /**
     * 删除奶仓档案信息
     * 
     * @param id 奶仓档案主键
     * @return 结果
     */
    @Override
    public int deleteBsMilkWarehouseById(String id)
    {
        return bsMilkWarehouseMapper.deleteBsMilkWarehouseById(id);
    }
}
