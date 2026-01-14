package com.gmlimsqi.business.basicdata.service;

import java.util.List;
import com.gmlimsqi.business.basicdata.domain.BsItemDept;

/**
 * 项目组织对应Service接口
 * 
 * @author hhy
 * @date 2025-09-17
 */
public interface IBsItemDeptService 
{
    /**
     * 查询项目组织对应
     * 
     * @param bsItemDeptId 项目组织对应主键
     * @return 项目组织对应
     */
    public BsItemDept selectBsItemDeptByBsItemDeptId(String bsItemDeptId);

    /**
     * 查询项目组织对应列表
     * 
     * @param bsItemDept 项目组织对应
     * @return 项目组织对应集合
     */
    public List<BsItemDept> selectBsItemDeptList(BsItemDept bsItemDept);

    /**
     * 新增项目组织对应
     * 
     * @param bsItemDept 项目组织对应
     * @return 结果
     */
    public int insertBsItemDept(BsItemDept bsItemDept);

    /**
     * 修改项目组织对应
     * 
     * @param bsItemDept 项目组织对应
     * @return 结果
     */
    public int updateBsItemDept(BsItemDept bsItemDept);

    /**
     * 批量删除项目组织对应
     * 
     * @param bsItemDeptIds 需要删除的项目组织对应主键集合
     * @return 结果
     */
    public int deleteBsItemDeptByBsItemDeptIds(String[] bsItemDeptIds);

    /**
     * 删除项目组织对应信息
     * 
     * @param bsItemDeptId 项目组织对应主键
     * @return 结果
     */
    public int deleteBsItemDeptByBsItemDeptId(String bsItemDeptId);
}
