package com.gmlimsqi.business.disinfectionmanagement.mapper;

import java.util.Date;
import java.util.List;

import com.gmlimsqi.business.disinfectionmanagement.domain.OpDisinfectionManagement;
import com.gmlimsqi.business.disinfectionmanagement.domain.OpDisinfectionManagementRecord;
import org.apache.ibatis.annotations.Param;

/**
 * 消毒管理Mapper接口
 *
 * @author hhy
 * @date 2025-11-06
 */
public interface OpDisinfectionManagementMapper {
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
     * 通过消毒管理主键更新删除标志
     *
     * @param disinfectionManagementId 消毒管理ID
     * @return 结果
     */
    public int updateDeleteFlagById(String disinfectionManagementId);

    /**
     * 批量通过消毒管理主键更新删除标志
     *
     * @param disinfectionManagementIds 消毒管理ID
     * @return 结果
     */
    public int updateDeleteFlagByIds(String[] disinfectionManagementIds);

    /**
     * 删除消毒管理
     *
     * @param disinfectionManagementId 消毒管理主键
     * @return 结果
     */
    public int deleteOpDisinfectionManagementByDisinfectionManagementId(String disinfectionManagementId);

    /**
     * 批量删除消毒管理
     *
     * @param disinfectionManagementIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteOpDisinfectionManagementByDisinfectionManagementIds(String[] disinfectionManagementIds);

    /**
     * 批量删除消毒管理记录
     *
     * @param disinfectionManagementIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteOpDisinfectionManagementRecordByDisinfectionManagementIds(String[] disinfectionManagementIds);

    /**
     * 批量新增消毒管理记录
     *
     * @param opDisinfectionManagementRecordList 消毒管理记录列表
     * @return 结果
     */
    public int batchOpDisinfectionManagementRecord(List<OpDisinfectionManagementRecord> opDisinfectionManagementRecordList);


    /**
     * 通过消毒管理主键删除消毒管理记录信息
     *
     * @param disinfectionManagementId 消毒管理ID
     * @return 结果
     */
    public int deleteOpDisinfectionManagementRecordByDisinfectionManagementId(String disinfectionManagementId);

    /**
     * 批量更新消毒管理记录删除标志
     *
     * @param disinfectionManagementIds 需要删除的数据主键集合
     * @return 结果
     */
    // public int updateDeleteFlagByIdDisinfectionManagementIds(@Param("disinfectionManagementIds") String[] disinfectionManagementIds, @Param("now") Date now, @Param("updateUserId") String updateUserId);


    /**
     * 通过消毒管理主键更新删除标志消毒管理记录信息
     *
     * @param disinfectionManagementId 消毒管理ID
     * @return 结果
     */
    // public int updateDeleteFlagByIdDisinfectionManagementId(@Param("disinfectionManagementId") String disinfectionManagementId, @Param("now") Date now, @Param("updateUserId") String updateUserId);

    /**
     * 通过部门ID查询消毒管理信息
     *
     * @param deptId 部门ID
     * @return 消毒管理信息
     */
    List<OpDisinfectionManagement> selectOpDisinfectionManagementByDeptId(Long deptId);
}
