package com.gmlimsqi.business.disinfectionmanagement.service;

import java.util.List;
import com.gmlimsqi.business.disinfectionmanagement.domain.OpDisinfectionManagement;

/**
 * 消毒管理Service接口
 * 
 * @author hhy
 * @date 2025-11-06
 */
public interface IOpDisinfectionManagementService 
{
    /**
     * 查询消毒管理
     * 
     * @param disinfectionManagementId 消毒管理主键
     * @return 消毒管理
     */
    public OpDisinfectionManagement selectOpDisinfectionManagementByDisinfectionManagementId(String disinfectionManagementId);

    /**
     * 查询消毒管理列表
     * 
     * @param opDisinfectionManagement 消毒管理
     * @return 消毒管理集合
     */
    public List<OpDisinfectionManagement> selectOpDisinfectionManagementList(OpDisinfectionManagement opDisinfectionManagement);

    /**
     * 根据部门查询唯一可通过
     * @param deptId 部门id
     * @return OpDisinfectionManagement 对象
     */
    public OpDisinfectionManagement selectOnePassedByDeptId(Long deptId);

    /**
     * 新增消毒管理
     * 
     * @param opDisinfectionManagement 消毒管理
     * @return 结果
     */
    public int insertOpDisinfectionManagement(OpDisinfectionManagement opDisinfectionManagement);

    /**
     * 修改消毒管理
     * 
     * @param opDisinfectionManagement 消毒管理
     * @return 结果
     */
    public int updateOpDisinfectionManagement(OpDisinfectionManagement opDisinfectionManagement);

    /**
     * 批量删除消毒管理
     * 
     * @param disinfectionManagementIds 需要删除的消毒管理主键集合
     * @return 结果
     */
    public int deleteOpDisinfectionManagementByDisinfectionManagementIds(String[] disinfectionManagementIds);

    /**
     * 删除消毒管理信息
     * 
     * @param disinfectionManagementId 消毒管理主键
     * @return 结果
     */
    public int deleteOpDisinfectionManagementByDisinfectionManagementId(String disinfectionManagementId);
}
