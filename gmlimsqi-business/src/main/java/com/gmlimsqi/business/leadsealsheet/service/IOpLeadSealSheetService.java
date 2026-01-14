package com.gmlimsqi.business.leadsealsheet.service;

import com.gmlimsqi.business.leadsealsheet.domain.OpLeadSealSheet;

import java.util.List;

/**
 * 铅封单Service接口
 * 
 * @author hhy
 * @date 2025-11-10
 */
public interface IOpLeadSealSheetService 
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
     * 审核铅封单
     *
     * @param opLeadSealSheetId 铅封单主键
     * @return 结果
     */
    int audit(String opLeadSealSheetId);

    /**
     * 推送奶源
     * @param opLeadSealSheetId
     * @return
     */
    int pushMilkSource(String opLeadSealSheetId);
}
