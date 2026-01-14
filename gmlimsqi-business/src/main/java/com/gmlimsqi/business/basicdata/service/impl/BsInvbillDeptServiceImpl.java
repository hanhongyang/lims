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
import com.gmlimsqi.business.basicdata.mapper.BsInvbillDeptMapper;
import com.gmlimsqi.business.basicdata.domain.BsInvbillDept;
import com.gmlimsqi.business.basicdata.service.IBsInvbillDeptService;

/**
 * 物料使用部门Service业务层处理
 * 
 * @author hhy
 * @date 2025-09-23
 */
@Service
public class BsInvbillDeptServiceImpl implements IBsInvbillDeptService 
{
    @Autowired
    private BsInvbillDeptMapper bsInvbillDeptMapper;


    /**
     * 查询物料使用部门
     * 
     * @param bsInvbillDeptId 物料使用部门主键
     * @return 物料使用部门
     */
    @Override
    public BsInvbillDept selectBsInvbillDeptByBsInvbillDeptId(String bsInvbillDeptId)
    {
        return bsInvbillDeptMapper.selectBsInvbillDeptByBsInvbillDeptId(bsInvbillDeptId);
    }

    /**
     * 查询物料使用部门列表
     * 
     * @param bsInvbillDept 物料使用部门
     * @return 物料使用部门
     */
    @Override
    public List<BsInvbillDept> selectBsInvbillDeptList(BsInvbillDept bsInvbillDept)
    {
        List<BsInvbillDept> items = bsInvbillDeptMapper.selectBsInvbillDeptList(bsInvbillDept);

        return items;
    }

    /**
     * 新增物料使用部门
     * 
     * @param bsInvbillDept 物料使用部门
     * @return 结果
     */
    @Override
    public int insertBsInvbillDept(BsInvbillDept bsInvbillDept)
    {
        if (StringUtils.isEmpty(bsInvbillDept.getBsInvbillDeptId())) {
            bsInvbillDept.setBsInvbillDeptId(IdUtils.simpleUUID());
        }
        // 自动填充创建/更新信息
        bsInvbillDept.fillCreateInfo();
        return bsInvbillDeptMapper.insertBsInvbillDept(bsInvbillDept);
    }

    /**
     * 修改物料使用部门
     * 
     * @param bsInvbillDept 物料使用部门
     * @return 结果
     */
    @Override
    public int updateBsInvbillDept(BsInvbillDept bsInvbillDept)
    {
        // 自动填充更新信息
        bsInvbillDept.fillUpdateInfo();
        return bsInvbillDeptMapper.updateBsInvbillDept(bsInvbillDept);
    }

}
