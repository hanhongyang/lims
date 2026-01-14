package com.gmlimsqi.business.basicdata.service;

import java.util.List;
import com.gmlimsqi.business.basicdata.domain.BsInvbillDept;

/**
 * 物料使用部门Service接口
 * 
 * @author hhy
 * @date 2025-09-23
 */
public interface IBsInvbillDeptService 
{
    /**
     * 查询物料使用部门
     * 
     * @param bsInvbillDeptId 物料使用部门主键
     * @return 物料使用部门
     */
    public BsInvbillDept selectBsInvbillDeptByBsInvbillDeptId(String bsInvbillDeptId);

    /**
     * 查询物料使用部门列表
     * 
     * @param bsInvbillDept 物料使用部门
     * @return 物料使用部门集合
     */
    public List<BsInvbillDept> selectBsInvbillDeptList(BsInvbillDept bsInvbillDept);

    /**
     * 新增物料使用部门
     * 
     * @param bsInvbillDept 物料使用部门
     * @return 结果
     */
    public int insertBsInvbillDept(BsInvbillDept bsInvbillDept);

    /**
     * 修改物料使用部门
     * 
     * @param bsInvbillDept 物料使用部门
     * @return 结果
     */
    public int updateBsInvbillDept(BsInvbillDept bsInvbillDept);

}
