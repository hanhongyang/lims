package com.gmlimsqi.business.labtest.mapper;

import java.util.List;
import com.gmlimsqi.business.labtest.domain.OpJczxFeedResultInfo;
import com.gmlimsqi.business.labtest.dto.OpJczxFeedResultBaseDto;
import org.apache.ibatis.annotations.Param;

/**
 * 检测中心饲料化验详情Mapper接口
 * 
 * @author hhy
 * @date 2025-09-26
 */
public interface OpJczxFeedResultInfoMapper 
{
    /**
     * 查询检测中心饲料化验详情
     * 
     * @param opJczxFeedResultInfoId 检测中心饲料化验详情主键
     * @return 检测中心饲料化验详情
     */
    public OpJczxFeedResultInfo selectOpJczxFeedResultInfoByOpJczxFeedResultInfoId(String opJczxFeedResultInfoId);

    /**
     * 查询检测中心饲料化验详情列表
     * 
     * @param opJczxFeedResultInfo 检测中心饲料化验详情
     * @return 检测中心饲料化验详情集合
     */
    public List<OpJczxFeedResultInfo> selectOpJczxFeedResultInfoList(OpJczxFeedResultInfo opJczxFeedResultInfo);

    /**
     * 新增检测中心饲料化验详情
     * 
     * @param opJczxFeedResultInfo 检测中心饲料化验详情
     * @return 结果
     */
    public int insertOpJczxFeedResultInfo(OpJczxFeedResultInfo opJczxFeedResultInfo);
    /**
     * 批量插入检测中心饲料检测结果明细
     */
    int batchInsertOpJczxFeedResultInfo(List<OpJczxFeedResultInfo> list);
    /**
     * 修改检测中心饲料化验详情
     * 
     * @param opJczxFeedResultInfo 检测中心饲料化验详情
     * @return 结果
     */
    public int updateOpJczxFeedResultInfo(OpJczxFeedResultInfo opJczxFeedResultInfo);

    /**
     * 通过检测中心饲料化验详情主键更新删除标志
     *
     * @param userId
     * @param opJczxFeedResultInfoId 检测中心饲料化验详情ID
     * @return 结果
     */
    public int updateDeleteFlagById(@Param("updateUserId") String updateUserId, @Param("opJczxFeedResultInfoId")String opJczxFeedResultInfoId);

    public List<OpJczxFeedResultInfo> selectInfoListByBaseId(@Param("opJczxFeedResultBaseId") String opJczxFeedResultBaseId);

    void updateDeleteFlagByBaseId(@Param("updateUserId") String updateUserId, @Param("baseId")String baseId);

    void updateCheckUserByBaseId(@Param("baseId")String baseId, @Param("checkUser")String checkUser);

    List<OpJczxFeedResultInfo> selectByOrderSampleIdList(@Param("orderSampleIdList")List<String> orderSampleIdList);

    List<OpJczxFeedResultInfo> selectCsfByOrderSampleIdList(@Param("orderSampleIdList")List<String> orderSampleIdList);
    void updateTestUserByBaseId(@Param("baseId")String opJczxFeedResultBaseId,  @Param("testUser")String testUser);

    int updateIsResetById(@Param("updateUserId") String updateUserId,  @Param("opJczxFeedResultInfoId")String opJczxFeedResultInfoId);

    List<OpJczxFeedResultInfo> selectJhwInfoListByBaseId(String opJczxFeedResultBaseId);

    List<OpJczxFeedResultInfo> getResultBySampleNo(OpJczxFeedResultBaseDto dto);

    List<OpJczxFeedResultInfo> getResultBySampleNo2(@Param("sampleNo") String  sampleNo);

    void updateItemId(@Param("oldItemId")String oldItemId,@Param("newItemId") String newItemId);

    void updateSampleId(@Param("oldSampleId")String oldSampleId, @Param("newSampleId")String newSampleId);
}
