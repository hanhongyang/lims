package com.gmlimsqi.business.basicdata.service;

import java.util.List;
import com.gmlimsqi.business.basicdata.domain.BsQualityInspectionSync;

/**
 * 质检信息同步Service接口
 * 
 * @author hhy
 * @date 2025-11-20
 */
public interface IBsQualityInspectionSyncService 
{
    /**
     * 查询质检信息同步
     * 
     * @param id 质检信息同步主键
     * @return 质检信息同步
     */
    public BsQualityInspectionSync selectBsQualityInspectionSyncById(String id);

    /**
     * 查询质检信息同步列表
     * 
     * @param bsQualityInspectionSync 质检信息同步
     * @return 质检信息同步集合
     */
    public List<BsQualityInspectionSync> selectBsQualityInspectionSyncList(BsQualityInspectionSync bsQualityInspectionSync);

    /**
     * 新增质检信息同步
     * 
     * @param bsQualityInspectionSync 质检信息同步
     * @return 结果
     */
    public int insertBsQualityInspectionSync(BsQualityInspectionSync bsQualityInspectionSync);

    /**
     * 修改质检信息同步
     * 
     * @param bsQualityInspectionSync 质检信息同步
     * @return 结果
     */
    public int updateBsQualityInspectionSync(BsQualityInspectionSync bsQualityInspectionSync);


}
