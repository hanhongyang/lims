package com.gmlimsqi.business.basicdata.mapper;

import com.gmlimsqi.business.basicdata.domain.BsContactInfo;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * 通讯方式联系人子Mapper接口
 *
 * @author wgq
 * @date 2025-09-15
 */
public interface BsContactInfoMapper
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
     * 通过通讯方式联系人子主键更新删除标志
     *
     * @param bsContactInfoId 通讯方式联系人子ID
     * @return 结果
     */
    public int updateDeleteFlagById(String bsContactInfoId);

    /**
     * 批量通过通讯方式联系人子主键更新删除标志
     *
     * @param bsContactInfoId 通讯方式联系人子ID
     * @return 结果
     */
    public int updateDeleteFlagByIds(@Param("nowTime") Date nowTime, @Param("usertId") String usertId, @Param("bsContactInfoIds") String[] bsContactInfoIds);


    /**
     * 删除通讯方式联系人子
     *
     * @param bsContactInfoId 通讯方式联系人子主键
     * @return 结果
     */
    public int deleteBsContactInfoByBsContactInfoId(String bsContactInfoId);

    /**
     * 批量删除通讯方式联系人子
     *
     * @param bsContactInfoIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteBsContactInfoByBsContactInfoIds(String[] bsContactInfoIds);

    /**
     * 根据主表id删除子表
     */
    int deleteWithParentId(@Param("nowTime") Date nowTime, @Param("usertId") String usertId,@Param("contactId")  String contactId);


    /**
     * 根据主表id查询子表
     */
    List<BsContactInfo> selectWithParentId(String contactId);

    /**
     * 批量插入
     */
    public int batchInsertBsContactInfo(List<BsContactInfo> list);



}
