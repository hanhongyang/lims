package com.gmlimsqi.business.basicdata.mapper;

import java.util.List;
import com.gmlimsqi.business.basicdata.domain.BsInvbillItemStandard;
import com.gmlimsqi.business.basicdata.dto.MaterialItemDTO;
import com.gmlimsqi.business.basicdata.vo.BsInvbillItemStandardVo;
import org.apache.ibatis.annotations.Param;

/**
 * 物料项目标准Mapper接口
 * 
 * @author hhy
 * @date 2025-09-08
 */
public interface BsInvbillItemStandardMapper 
{
    /**
     * 查询物料项目标准
     * 
     * @param bsInvbillItemStandardId 物料项目标准主键
     * @return 物料项目标准
     */
    public BsInvbillItemStandard selectBsInvbillItemStandardByBsInvbillItemStandardId(String bsInvbillItemStandardId);


    /**
     * 按物料分组查询项目标准列表
     */
    List<BsInvbillItemStandardVo> selectListGroupByInvbill(BsInvbillItemStandard bsInvbillItemStandard);

    /**
     * 查询物料项目标准列表
     * 
     * @param bsInvbillItemStandard 物料项目标准
     * @return 物料项目标准集合
     */
    public List<BsInvbillItemStandard> selectBsInvbillItemStandardList(BsInvbillItemStandard bsInvbillItemStandard);

    /**
     * 新增物料项目标准
     * 
     * @param bsInvbillItemStandard 物料项目标准
     * @return 结果
     */
    public int insertBsInvbillItemStandard(BsInvbillItemStandard bsInvbillItemStandard);

    /**
     * 修改物料项目标准
     * 
     * @param bsInvbillItemStandard 物料项目标准
     * @return 结果
     */
    public int updateBsInvbillItemStandard(BsInvbillItemStandard bsInvbillItemStandard);

    /**
     * 通过物料项目标准主键更新删除标志
     *
     * @param bsInvbillItemStandardId 物料项目标准ID
     * @return 结果
     */
    public int updateDeleteFlagById(@Param("bsInvbillItemStandardId")String bsInvbillItemStandardId,@Param("updateUserId") String updateUserId);
    /**
     * 批量通过物料项目标准主键更新删除标志
     *
     * @param bsInvbillItemStandardId 物料项目标准ID
     * @return 结果
     */
    public int updateDeleteFlagByIds(String[] bsInvbillItemStandardIds);

    /**
     * 根据物料id获取物料项目标准详细信息
     */
    public List<BsInvbillItemStandardVo> selectBsInvbillItemStandardByBsInvbillCode(String invbillCode);

    public void updateDeleteFlagByInvbillCode(@Param("invbillCode") String invbillCode,@Param("updateUserId") String updateUserId,@Param("deptId") String deptId);

    /**
     * 分页查询物料ID列表
     */
    public List<String> selectInvbillCodesPage(BsInvbillItemStandard bsInvbillItemStandard);

    /**
     * 根据物料ID列表查询物料项目标准详情
     */
    public List<BsInvbillItemStandard> selectStandardListByInvbillCodes(@Param("invbillCodes") List<String> invbillCodes,
                                                                 @Param("bsInvbillItemStandard") BsInvbillItemStandard bsInvbillItemStandard);

    void updateInstrumentsByItemIdDeptId(@Param("labtestItemsId")String labtestItemsId,
                                         @Param("instrumentIds")String instrumentIds,@Param("deptId") String deptId);

    /**
     * 根据物料编码查询物料检测项目标准列表
     * @param materialItemDTO
     * @return
     */
    List<BsInvbillItemStandard> getItemByInvbillCode(MaterialItemDTO materialItemDTO);
}
