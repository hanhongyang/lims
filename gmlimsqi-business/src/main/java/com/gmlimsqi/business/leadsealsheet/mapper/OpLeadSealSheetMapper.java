package com.gmlimsqi.business.leadsealsheet.mapper;

import com.gmlimsqi.business.leadsealsheet.domain.OpLeadSealSheet;

import java.util.List;

/**
 * 铅封单Mapper接口
 * 
 * @author hhy
 * @date 2025-11-10
 */
public interface OpLeadSealSheetMapper 
{
    /**
     * 查询铅封单
     * 
     * @param opLeadSealSheetId 铅封单主键
     * @return 铅封单
     */
    public OpLeadSealSheet selectOpLeadSealSheetByOpLeadSealSheetId(String opLeadSealSheetId);

    /**
     * 查询铅封单列表
     * 
     * @param opLeadSealSheet 铅封单
     * @return 铅封单集合
     */
    public List<OpLeadSealSheet> selectOpLeadSealSheetList(OpLeadSealSheet opLeadSealSheet);

    /**
     * 新增铅封单
     * 
     * @param opLeadSealSheet 铅封单
     * @return 结果
     */
    public int insertOpLeadSealSheet(OpLeadSealSheet opLeadSealSheet);

    /**
     * 修改铅封单
     * 
     * @param opLeadSealSheet 铅封单
     * @return 结果
     */
    public int updateOpLeadSealSheet(OpLeadSealSheet opLeadSealSheet);

    /**
     * 通过铅封单主键更新删除标志
     *
     * @param opLeadSealSheetId 铅封单ID
     * @return 结果
     */
    public int updateDeleteFlagById(String opLeadSealSheetId);

    /**
     * 批量通过铅封单主键更新删除标志
     *
     * @param opLeadSealSheetId 铅封单ID
     * @return 结果
     */
    public int updateDeleteFlagByIds(String[] opLeadSealSheetIds);

    /**
     * 根据奶罐车id查询铅封单
     */
     public OpLeadSealSheet selectOpLeadSealSheetByMilkTankersId(String milkTankersId);

}
