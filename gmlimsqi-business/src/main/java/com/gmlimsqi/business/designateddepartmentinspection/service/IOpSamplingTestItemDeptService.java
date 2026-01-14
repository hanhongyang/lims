package com.gmlimsqi.business.designateddepartmentinspection.service;

import com.gmlimsqi.business.designateddepartmentinspection.domin.entity.OpSamplingTestItemDept;

import java.util.List;

/**
 * 检测项目指定部门检测Service接口
 * 
 * @author hhy
 * @date 2025-11-27
 */
public interface IOpSamplingTestItemDeptService 
{
    /**
     * 查询检测项目指定部门检测
     * 
     * @param opSamplingTestItemDeptId 检测项目指定部门检测主键
     * @return 检测项目指定部门检测
     */
    public OpSamplingTestItemDept selectOpSamplingTestItemDeptByOpSamplingTestItemDeptId(String opSamplingTestItemDeptId);

    /**
     * 查询检测项目指定部门检测列表
     * 
     * @param opSamplingTestItemDept 检测项目指定部门检测
     * @return 检测项目指定部门检测集合
     */
    public List<OpSamplingTestItemDept> selectOpSamplingTestItemDeptList(OpSamplingTestItemDept opSamplingTestItemDept);

    /**
     * 新增检测项目指定部门检测
     * 
     * @param opSamplingTestItemDept 检测项目指定部门检测
     * @return 结果
     */
    public int insertOpSamplingTestItemDept(OpSamplingTestItemDept opSamplingTestItemDept);

    /**
     * 修改检测项目指定部门检测
     * 
     * @param opSamplingTestItemDept 检测项目指定部门检测
     * @return 结果
     */
    public int updateOpSamplingTestItemDept(OpSamplingTestItemDept opSamplingTestItemDept);


}
