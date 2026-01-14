package com.gmlimsqi.business.basicdata.service;

import java.util.List;
import com.gmlimsqi.business.basicdata.domain.BsInvbillItemStandard;
import com.gmlimsqi.business.basicdata.dto.BatchAddStandardDto;
import com.gmlimsqi.business.basicdata.dto.MaterialItemDTO;
import com.gmlimsqi.business.basicdata.vo.BsInvbillItemStandardVo;
import org.springframework.transaction.annotation.Transactional;

/**
 * 物料项目标准Service接口
 * 
 * @author hhy
 * @date 2025-09-08
 */
public interface IBsInvbillItemStandardService 
{
    @Transactional
    int batchAdd(BatchAddStandardDto batchAddDto);

    @Transactional
    int batchUpdate(BatchAddStandardDto batchUpdateDto);

    List<BsInvbillItemStandardVo> selectListGroupByInvbill(BsInvbillItemStandard bsInvbillItemStandard);

    /**
     * 查询物料项目标准
     * 
     * @param bsInvbillItemStandardId 物料项目标准主键
     * @return 物料项目标准
     */
    public BsInvbillItemStandard selectBsInvbillItemStandardByBsInvbillItemStandardId(String bsInvbillItemStandardId);

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


    public List<BsInvbillItemStandardVo> selectBsInvbillItemStandardByBsInvbillCode(String invbillCode);

    int updateDeleteFlagById(String bsInvbillItemStandardId);

    BsInvbillItemStandard selectByBsInvbillItemId(String invbillCode, String itemId);

    /**
     * 根据物料编码查询物料检测项目
     * @param materialItemDTO
     * @return
     */
     List<BsInvbillItemStandard> getItemByInvbillCode(MaterialItemDTO materialItemDTO);
}
