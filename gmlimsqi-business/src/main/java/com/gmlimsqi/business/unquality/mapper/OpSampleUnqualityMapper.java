package com.gmlimsqi.business.unquality.mapper;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.gmlimsqi.business.unquality.domain.OpSampleUnquality;
import com.gmlimsqi.business.unquality.domain.OpSampleUnqualityDetail;
import com.gmlimsqi.business.unquality.domain.YktReadDTO;
import org.apache.ibatis.annotations.Param;

/**
 * 样品不合格处理单Mapper接口
 *
 * @author hhy
 * @date 2025-11-28
 */
public interface OpSampleUnqualityMapper
{
    /**
     * 查询样品不合格处理单
     *
     * @param opSampleUnqualityId 样品不合格处理单主键
     * @return 样品不合格处理单
     */
    public OpSampleUnquality selectOpSampleUnqualityByOpSampleUnqualityId(String opSampleUnqualityId);
    /**
     * 调用存储过程生成不合格处理单
     * * @param ctype 不合格类型 (GG: 感官不合格, HY: 化验不合格)
     * @param keyid 关联ID (GG传op_sampling_plan_sample_id, HY传op_test_result_base_id)
     * @return 存储过程执行结果 (code, msg)
     */
    Map<String, Object> callProTransCreateUnquality(@Param("ctype") String ctype, @Param("keyid") String keyid);
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
     * 通过样品不合格处理单主键更新删除标志
     *
     * @param opSampleUnqualityId 样品不合格处理单ID
     * @return 结果
     */
    public int updateDeleteFlagById(String opSampleUnqualityId);

    /**
     * 批量通过样品不合格处理单主键更新删除标志
     *
     * @param opSampleUnqualityId 样品不合格处理单ID
     * @return 结果
     */
    public int updateDeleteFlagByIds(String[] opSampleUnqualityIds);

    /**
     * 批量新增样品不合格处理单详情
     *
     * @param opSampleUnqualityDetailList 样品不合格处理单详情列表
     * @return 结果
     */
    public int batchOpSampleUnqualityDetail(List<OpSampleUnqualityDetail> opSampleUnqualityDetailList);

    /**
     * 批量更新样品不合格处理单详情删除标志
     *
     * @param opSampleUnqualityIds 需要删除的数据主键集合
     * @return 结果
     */
    public int updateDeleteFlagByIdOpSampleUnqualityIds(@Param("opSampleUnqualityIds")String[] opSampleUnqualityIds, @Param("now") Date now, @Param("updateUserId") String updateUserId);

    /**
     * 通过样品不合格处理单主键更新删除标志样品不合格处理单详情信息
     *
     * @param opSampleUnqualityId 样品不合格处理单ID
     * @return 结果
     */
    public int updateDeleteFlagByIdOpSampleUnqualityId(@Param("opSampleUnqualityId")String opSampleUnqualityId,@Param("now") Date now,@Param("updateUserId") String updateUserId);

    /**
     * 根据signId查询样品不合格处理单
     * @param yktReadDTO 读取样品不合格处理单
     * @return 样品不合格处理单集合
     */
    public List<OpSampleUnquality> selectOpSampleUnqualityBySignId(YktReadDTO yktReadDTO);

    /**
     * 根据来源id查询样品不合格处理单
     * @param sourceId 来源id
     * @return 样品不合格处理单
     */
    OpSampleUnquality selectOpSampleUnqualityBySourceId(@Param("sourceId") String sourceId,
                                                        @Param("ctype") String ctype);

    /**
     * 根据主表id删除子表
     */
    int deleteOpSampleUnqualityDetailByOpSampleUnqualityId(String opSampleUnqualityId);

    /**
     * 根据主表id查询是否有子表数据
     * @param opSampleUnqualityId 主表id
     */
    List<OpSampleUnqualityDetail> selectOpSampleUnqualityDetailByOpSampleUnqualityId(String opSampleUnqualityId);

    /**
     * 新增子表数据
     */
     int insertOpSampleUnqualityDetail(OpSampleUnqualityDetail opSampleUnqualityDetail);

    /**
     * 更新子表数据
     */
    int updateOpSampleUnqualityDetail(OpSampleUnqualityDetail opSampleUnqualityDetail);

    /**
     * 地磅修改样品不合格处理单
     * @param opSampleUnqualityIds 样品不合格处理单id列表
     * @return 结果
     */
    int updateOpSampleUnqualityByDiBang(List<String> opSampleUnqualityIds);

    /**
     * 手动关闭样品不合格处理单
     * @param opSampleUnquality 样品不合格处理单对象
     * @return
     */
    int updateOpSampleUnqualityManually(OpSampleUnquality opSampleUnquality);

    /**
     * 根据来源id删除样品不合格处理单
     * @param sourceId 来源id
     * @return 结果
     */
     int deleteOpSampleUnqualityBySourceId(@Param("sourceId") String sourceId, @Param("now") Date now, @Param("updateUserId") String updateUserId);
}
