package com.gmlimsqi.business.designateddepartmentinspection.service.impl;

import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.List;

import com.gmlimsqi.business.designateddepartmentinspection.domin.entity.OpSamplingTestItemDept;
import com.gmlimsqi.business.designateddepartmentinspection.mapper.OpSamplingTestItemDeptMapper;
import com.gmlimsqi.business.designateddepartmentinspection.service.IOpSamplingTestItemDeptService;
import com.gmlimsqi.common.core.domain.entity.SysDept;
import com.gmlimsqi.common.utils.SecurityUtils;
import com.gmlimsqi.common.utils.StringUtils;
import com.gmlimsqi.common.utils.uuid.IdUtils;
import com.gmlimsqi.business.util.UserCacheService;
import com.gmlimsqi.common.annotation.DataScope;
import com.gmlimsqi.system.mapper.SysDeptMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 检测项目指定部门检测Service业务层处理
 * * @author hhy
 * @date 2025-11-27
 */
@Service
public class OpSamplingTestItemDeptServiceImpl implements IOpSamplingTestItemDeptService
{
    @Autowired
    private OpSamplingTestItemDeptMapper opSamplingTestItemDeptMapper;
    @Autowired
    private UserCacheService userCacheService;

    @Autowired
    private SysDeptMapper sysDeptMapper;

    /**
     * 查询检测项目指定部门检测
     * * @param opSamplingTestItemDeptId 检测项目指定部门检测主键
     * @return 检测项目指定部门检测
     */
    @Override
    public OpSamplingTestItemDept selectOpSamplingTestItemDeptByOpSamplingTestItemDeptId(String opSamplingTestItemDeptId)
    {
        OpSamplingTestItemDept opSamplingTestItemDept = opSamplingTestItemDeptMapper.selectOpSamplingTestItemDeptByOpSamplingTestItemDeptId(opSamplingTestItemDeptId);

        if (StringUtils.isNotEmpty(opSamplingTestItemDept.getDeptId()) && StringUtils.isNotEmpty(opSamplingTestItemDept.getDesignatedDeptId())){
            SysDept dept = sysDeptMapper.selectDeptById(Long.parseLong(opSamplingTestItemDept.getDeptId()));
            if (dept != null){
                opSamplingTestItemDept.setDeptName(dept.getDeptName());
            }

            SysDept designatedDept = sysDeptMapper.selectDeptById(Long.parseLong(opSamplingTestItemDept.getDesignatedDeptId()));
            if (designatedDept != null){
                opSamplingTestItemDept.setDesignatedDeptName(designatedDept.getDeptName());
            }
        }
        return opSamplingTestItemDept;
    }

    /**
     * 查询检测项目指定部门检测列表
     * * @param opSamplingTestItemDept 检测项目指定部门检测
     * @return 检测项目指定部门检测
     */
    // +++ 新增：@DataScope 注解以激活数据权限切面 +++
//    @DataScope(deptAlias = "d", permission = "basicdata:dept:list")
    @Override
    public List<OpSamplingTestItemDept> selectOpSamplingTestItemDeptList(OpSamplingTestItemDept opSamplingTestItemDept)
    {
        if (!SecurityUtils.isAdmin(SecurityUtils.getUserId())){
            opSamplingTestItemDept.setDeptId(SecurityUtils.getDeptId().toString());
        }

        List<OpSamplingTestItemDept> items = opSamplingTestItemDeptMapper.selectOpSamplingTestItemDeptList(opSamplingTestItemDept);

        // 批量处理用户名
        if (!items.isEmpty()) {
            Set<String> userIds = items.stream()
                    .map(OpSamplingTestItemDept::getCreateBy)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toSet());

            Map<String, String> usernameMap = userCacheService.batchGetUsernames(userIds);

            items.forEach(vo ->
                    vo.setCreateBy(usernameMap.get(vo.getCreateBy())));
        }

        return items;
    }

    /**
     * 新增检测项目指定部门检测
     * * @param opSamplingTestItemDept 检测项目指定部门检测
     * @return 结果
     */
    @Override
    public int insertOpSamplingTestItemDept(OpSamplingTestItemDept opSamplingTestItemDept)
    {
        if (StringUtils.isEmpty(opSamplingTestItemDept.getOpSamplingTestItemDeptId())) {
            opSamplingTestItemDept.setOpSamplingTestItemDeptId(IdUtils.simpleUUID());
        }

        // 自动填充创建/更新信息
        opSamplingTestItemDept.fillCreateInfo();
        return opSamplingTestItemDeptMapper.insertOpSamplingTestItemDept(opSamplingTestItemDept);
    }

    /**
     * 修改检测项目指定部门检测
     * * @param opSamplingTestItemDept 检测项目指定部门检测
     * @return 结果
     */
    @Override
    public int updateOpSamplingTestItemDept(OpSamplingTestItemDept opSamplingTestItemDept)
    {
        // 自动填充更新信息
        opSamplingTestItemDept.fillUpdateInfo();
        return opSamplingTestItemDeptMapper.updateOpSamplingTestItemDept(opSamplingTestItemDept);
    }

}