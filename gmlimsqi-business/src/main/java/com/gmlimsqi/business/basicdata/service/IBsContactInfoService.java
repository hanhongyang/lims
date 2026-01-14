package com.gmlimsqi.business.basicdata.service;

import java.util.List;
import com.gmlimsqi.business.basicdata.domain.BsContactInfo;

/**
 * 通讯方式联系人子Service接口
 *
 * @author wgq
 * @date 2025-09-15
 */
public interface IBsContactInfoService
{
    /**
     * 查询通讯方式联系人子
     *
     * @param bsContactInfoId 通讯方式联系人子主键
     * @return 通讯方式联系人子
     */
    public BsContactInfo selectBsContactInfoByBsContactInfoId(String bsContactInfoId);

    /**
     * 查询通讯方式联系人子列表
     *
     * @param bsContactInfo 通讯方式联系人子
     * @return 通讯方式联系人子集合
     */
    public List<BsContactInfo> selectBsContactInfoList(BsContactInfo bsContactInfo);

    /**
     * 新增通讯方式联系人子
     *
     * @param bsContactInfo 通讯方式联系人子
     * @return 结果
     */
    public int insertBsContactInfo(BsContactInfo bsContactInfo);

    /**
     * 修改通讯方式联系人子
     *
     * @param bsContactInfo 通讯方式联系人子
     * @return 结果
     */
    public int updateBsContactInfo(BsContactInfo bsContactInfo);

    /**
     * 批量删除通讯方式联系人子
     *
     * @param bsContactInfoIds 需要删除的通讯方式联系人子主键集合
     * @return 结果
     */
    public int deleteBsContactInfoByBsContactInfoIds(String[] bsContactInfoIds);

    /**
     * 删除通讯方式联系人子信息
     *
     * @param bsContactInfoId 通讯方式联系人子主键
     * @return 结果
     */
    public int deleteBsContactInfoByBsContactInfoId(String bsContactInfoId);
}
