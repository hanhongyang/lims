package com.gmlimsqi.business.basicdata.service;

import com.gmlimsqi.business.basicdata.domain.BsContact;
import com.gmlimsqi.business.basicdata.dto.BsContactDto;

import java.util.List;

/**
 * 通讯方式Service接口
 *
 * @author wgq
 * @date 2025-09-15
 */
public interface IBsContactService
{
    /**
     * 查询通讯方式
     *
     * @param id 通讯方式主键
     * @return 通讯方式
     */
    public BsContactDto selectBsContactById(String id);

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
    public int insertBsContact(BsContactDto bsContact);

    /**
     * 修改通讯方式
     *
     * @param bsContact 通讯方式
     * @return 结果
     */
    public int updateBsContact(BsContactDto bsContact);

    /**
     * 批量删除通讯方式
     *
     * @param ids 需要删除的通讯方式主键集合
     * @return 结果
     */
    public int deleteBsContactByIds(String[] ids);

    /**
     * 删除通讯方式信息
     *
     * @param id 通讯方式主键
     * @return 结果
     */
    public int deleteBsContactById(String id);

    /**
     * 启用/弃用通讯方式信息
     *
     * @param bsContact 通讯方式
     * @return 结果
     */
    int enableBsContact(BsContact bsContact);

    BsContactDto getInfoByDeptId(String deptId);
}
