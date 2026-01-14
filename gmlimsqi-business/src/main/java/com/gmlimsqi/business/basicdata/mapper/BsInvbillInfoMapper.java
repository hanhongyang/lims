package com.gmlimsqi.business.basicdata.mapper;

import java.util.List;
import com.gmlimsqi.business.basicdata.domain.BsInvbillInfo;
import org.apache.ibatis.annotations.Param;

/**
 * 物料子Mapper接口
 * 
 * @author hhy
 * @date 2025-09-29
 */
public interface BsInvbillInfoMapper 
{
    /**
     * 查询物料子
     * 
     * @param bsInvbillInfoId 物料子主键
     * @return 物料子
     */
    public BsInvbillInfo selectBsInvbillInfoByBsInvbillInfoId(String bsInvbillInfoId);

    /**
     * 查询物料子列表
     * 
     * @param bsInvbillInfo 物料子
     * @return 物料子集合
     */
    public List<BsInvbillInfo> selectBsInvbillInfoList(BsInvbillInfo bsInvbillInfo);

    /**
     * 新增物料子
     * 
     * @param bsInvbillInfo 物料子
     * @return 结果
     */
    public int insertBsInvbillInfo(BsInvbillInfo bsInvbillInfo);

    /**
     * 修改物料子
     * 
     * @param bsInvbillInfo 物料子
     * @return 结果
     */
    public int updateBsInvbillInfo(BsInvbillInfo bsInvbillInfo);

    /**
     * 通过物料子主键更新删除标志
     *
     * @param bsInvbillInfoId 物料子ID
     * @return 结果
     */
    public int updateDeleteFlagById( @Param("bsInvbillInfoId")String bsInvbillInfoId);

    /**
     * 批量通过物料子主键更新删除标志
     *
     * @param sapCode
     * @return 结果
     */
    public int updateDeleteFlagBySapCode(@Param("updateUserId") String updateUserId, @Param("sapCode") String sapCode);

}
