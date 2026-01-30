package com.gmlimsqi.business.unquality.service;

import java.util.List;
import com.gmlimsqi.business.unquality.domain.OpSampleUnquality;
import com.gmlimsqi.business.unquality.domain.YktReadDTO;

/**
 * 样品不合格处理单Service接口
 *
 * @author hhy
 * @date 2025-11-28
 */
public interface IOpSampleUnqualityService
{
    /**
     * 查询样品不合格处理单
     *
     * @param opSampleUnqualityId 样品不合格处理单主键
     * @return 样品不合格处理单
     */
    public OpSampleUnquality selectOpSampleUnqualityByOpSampleUnqualityId(String opSampleUnqualityId);

    /**
     * 查询样品不合格处理单列表
     *
     * @param opSampleUnquality 样品不合格处理单
     * @return 样品不合格处理单集合
     */
    public List<OpSampleUnquality> selectOpSampleUnqualityList(OpSampleUnquality opSampleUnquality);

    /**
     * 新增样品不合格处理单
     *
     * @param opSampleUnquality 样品不合格处理单
     * @return 结果
     */
    public int insertOpSampleUnquality(OpSampleUnquality opSampleUnquality);

    /**
     * 修改样品不合格处理单
     *
     * @param opSampleUnquality 样品不合格处理单
     * @return 结果
     */
    public int updateOpSampleUnquality(OpSampleUnquality opSampleUnquality);


     /**
     * 根据signId查询样品不合格处理单
     *
     * @param yktReadDTO 读取样品不合格处理单
     * @return 样品不合格处理单集合
     */
    public List<OpSampleUnquality> selectOpSampleUnqualityBySignId(YktReadDTO yktReadDTO);

    /**
     * 地磅修改样品不合格处理单
     *
     * @param opSampleUnqualityIds 样品不合格处理单id列表
     * @return 结果
     */
    int updateOpSampleUnqualityByDiBang(List<String> opSampleUnqualityIds);

    /**
     * 手动修改样品不合格处理单
     * @param opSampleUnquality
     * @return
     */
    int updateOpSampleUnqualityManually(OpSampleUnquality opSampleUnquality);

    /**
     * 根据来源id删除样品不合格处理单
     * @param sourceId 来源id
     * @return 结果
     */
    int deleteOpSampleUnqualityBySourceId(String sourceId);
}
