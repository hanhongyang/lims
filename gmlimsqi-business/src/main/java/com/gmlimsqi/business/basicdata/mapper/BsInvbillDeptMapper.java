package com.gmlimsqi.business.basicdata.mapper;

import java.util.List;
import com.gmlimsqi.business.basicdata.domain.BsInvbillDept;
import org.apache.ibatis.annotations.Param;

/**
 * 物料使用部门Mapper接口
 * 
 * @author hhy
 * @date 2025-09-23
 */
public interface BsInvbillDeptMapper 
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


    public void updateDeleteFlagBySapCode(@Param("updateBy") String updateBy,@Param("invbillSapCode")  String invbillSapCode);
}
