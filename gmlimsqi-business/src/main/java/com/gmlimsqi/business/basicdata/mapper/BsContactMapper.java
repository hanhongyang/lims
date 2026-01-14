package com.gmlimsqi.business.basicdata.mapper;

import com.gmlimsqi.business.basicdata.domain.BsContact;
import com.gmlimsqi.business.labtest.vo.ReportEmailVo;

import java.util.List;

/**
 * 通讯方式Mapper接口
 *
 * @author wgq
 * @date 2025-09-15
 */
public interface BsContactMapper
{
    /**
     * 查询通讯方式
     *
     * @param id 通讯方式主键
     * @return 通讯方式
     */
    public BsContact selectBsContactById(String id);

    /**
     * 查询通讯方式列表
     *
     * @param bsContact 通讯方式
     * @return 通讯方式集合
     */
    public List<BsContact> selectBsContactList(BsContact bsContact);

    /**
     * 新增通讯方式
     *
     * @param bsContact 通讯方式
     * @return 结果
     */
    public int insertBsContact(BsContact bsContact);

    /**
     * 修改通讯方式
     *
     * @param bsContact 通讯方式
     * @return 结果
     */
    public int updateBsContact(BsContact bsContact);

    /**
     * 通过通讯方式主键更新删除标志
     *
     * @param id 通讯方式ID
     * @return 结果
     */
    public int updateDeleteFlagById(String id);

    /**
     * 批量通过通讯方式主键更新删除标志
     *
     * @param id 通讯方式ID
     * @return 结果
     */
    public int updateDeleteFlagByIds(String[] ids);

    /**
     * 删除通讯方式
     *
     * @param id 通讯方式主键
     * @return 结果
     */
    public int deleteBsContactById(String id);

    /**
     * 批量删除通讯方式
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteBsContactByIds(String[] ids);

    /**
     * 查询饲料通讯方式子表邮箱
     * @param deptId
     * @return
     */
    List<ReportEmailVo> selectFeedEmailByDeptId(Long deptId);
    List<ReportEmailVo> selectPcrEmailByDeptId(Long deptId);
    List<ReportEmailVo> selectShEmailByDeptId(Long deptId);
    List<ReportEmailVo> selectZyEmailByDeptId(Long deptId);
    List<ReportEmailVo> selectYbEmailByDeptId(Long deptId);
}
