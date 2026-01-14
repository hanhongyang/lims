package com.gmlimsqi.business.ranch.mapper;

import java.util.List;
import com.gmlimsqi.business.ranch.domain.OpTestResultInfo;
import org.apache.ibatis.annotations.Param;

/**
 * 化验结果信息Mapper接口
 * 
 * @author hhy
 * @date 2025-11-07
 */
public interface OpTestResultInfoMapper 
{
    /**
     * 查询化验结果信息
     * 
     * @param id 化验结果信息主键
     * @return 化验结果信息
     */
    public OpTestResultInfo selectOpTestResultInfoById(String id);

    /**
     * 查询化验结果信息列表
     * 
     * @param opTestResultInfo 化验结果信息
     * @return 化验结果信息集合
     */
    public List<OpTestResultInfo> selectOpTestResultInfoList(OpTestResultInfo opTestResultInfo);

    /**
     * 根据主表ID查询化验结果信息
     * @param baseId 父表id
     * @return 化验结果信息集合
     */
    public List<OpTestResultInfo> selectOpTestResultInfoByBaseId(String baseId);

    /**
     * 新增化验结果信息
     * 
     * @param opTestResultInfo 化验结果信息
     * @return 结果
     */
    public int insertOpTestResultInfo(OpTestResultInfo opTestResultInfo);

    /**
     * 修改化验结果信息
     * 
     * @param opTestResultInfo 化验结果信息
     * @return 结果
     */
    public int updateOpTestResultInfo(OpTestResultInfo opTestResultInfo);

    /**
     * 通过化验结果信息主键更新删除标志
     *
     * @param id 化验结果信息ID
     * @return 结果
     */
    public int updateDeleteFlagById(String id);

    /**
     * 批量通过化验结果信息主键更新删除标志
     *
     * @param id 化验结果信息ID
     * @return 结果
     */
    public int updateDeleteFlagByIds(String[] ids);

    /**
     * 根据主表ID批量软删除化验结果信息
     *
     * @param baseId 父表id
     * @param updateBy 更新人
     * @return 结果
     */
    public int updateDeleteFlagByBaseId(@Param("baseId") String baseId, @Param("updateBy") String updateBy);

    int updateIsResetById(String updateUser, String s);

    int updateRetestFlagById(@Param("updateUser") String updateUser, @Param("id") String id);


}
