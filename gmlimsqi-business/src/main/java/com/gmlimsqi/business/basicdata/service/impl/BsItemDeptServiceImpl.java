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
import com.gmlimsqi.business.basicdata.mapper.BsItemDeptMapper;
import com.gmlimsqi.business.basicdata.domain.BsItemDept;
import com.gmlimsqi.business.basicdata.service.IBsItemDeptService;

/**
 * 项目组织对应Service业务层处理
 * 
 * @author hhy
 * @date 2025-09-17
 */
@Service
public class BsItemDeptServiceImpl implements IBsItemDeptService 
{
    @Autowired
    private BsItemDeptMapper bsItemDeptMapper;


    /**
     * 查询项目组织对应
     * 
     * @param bsItemDeptId 项目组织对应主键
     * @return 项目组织对应
     */
    @Override
    public BsItemDept selectBsItemDeptByBsItemDeptId(String bsItemDeptId)
    {
        return bsItemDeptMapper.selectBsItemDeptByBsItemDeptId(bsItemDeptId);
    }

    /**
     * 查询项目组织对应列表
     * 
     * @param bsItemDept 项目组织对应
     * @return 项目组织对应
     */
    @Override
    public List<BsItemDept> selectBsItemDeptList(BsItemDept bsItemDept)
    {
        List<BsItemDept> items = bsItemDeptMapper.selectBsItemDeptList(bsItemDept);


        return items;
    }

    /**
     * 新增项目组织对应
     * 
     * @param bsItemDept 项目组织对应
     * @return 结果
     */
    @Override
    public int insertBsItemDept(BsItemDept bsItemDept)
    {
        if (StringUtils.isEmpty(bsItemDept.getBsItemDeptId())) {
            bsItemDept.setBsItemDeptId(IdUtils.simpleUUID());
        }
        // 自动填充创建/更新信息
        bsItemDept.fillCreateInfo();
        return bsItemDeptMapper.insertBsItemDept(bsItemDept);
    }

    /**
     * 修改项目组织对应
     * 
     * @param bsItemDept 项目组织对应
     * @return 结果
     */
    @Override
    public int updateBsItemDept(BsItemDept bsItemDept)
    {
        // 自动填充更新信息
        bsItemDept.fillUpdateInfo();
        return bsItemDeptMapper.updateBsItemDept(bsItemDept);
    }

    /**
     * 批量删除项目组织对应
     * 
     * @param bsItemDeptIds 需要删除的项目组织对应主键
     * @return 结果
     */
    @Override
    public int deleteBsItemDeptByBsItemDeptIds(String[] bsItemDeptIds)
    {
        return bsItemDeptMapper.deleteBsItemDeptByBsItemDeptIds(bsItemDeptIds);
    }

    /**
     * 删除项目组织对应信息
     * 
     * @param bsItemDeptId 项目组织对应主键
     * @return 结果
     */
    @Override
    public int deleteBsItemDeptByBsItemDeptId(String bsItemDeptId)
    {
        return bsItemDeptMapper.deleteBsItemDeptByBsItemDeptId(bsItemDeptId);
    }
}
