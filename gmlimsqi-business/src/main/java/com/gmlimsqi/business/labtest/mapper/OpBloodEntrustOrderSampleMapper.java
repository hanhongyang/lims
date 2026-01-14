package com.gmlimsqi.business.labtest.mapper;


import com.gmlimsqi.business.labtest.bo.OpJczxBloodTestModelBo;
import com.gmlimsqi.business.labtest.domain.OpBloodEntrustOrderSample;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * 血样样品委托-样品对应Mapper接口
 * 
 * @author hhy
 * @date 2025-09-20
 */
public interface OpBloodEntrustOrderSampleMapper 
{
    /**
     * 查询血样样品委托-样品对应
     * 
     * @param opBloodEntrustOrderId 血样样品委托-样品对应主键
     * @return 血样样品委托-样品对应
     */
    public OpBloodEntrustOrderSample selectOpBloodEntrustOrderSampleByOpBloodEntrustOrderId(String opBloodEntrustOrderId);

    /**
     * 查询血样样品委托-样品对应列表
     * 
     * @param opBloodEntrustOrderSample 血样样品委托-样品对应
     * @return 血样样品委托-样品对应集合
     */
    public List<OpBloodEntrustOrderSample> selectOpBloodEntrustOrderSampleList(OpBloodEntrustOrderSample opBloodEntrustOrderSample);

    /**
     * 新增血样样品委托-样品对应
     * 
     * @param opBloodEntrustOrderSample 血样样品委托-样品对应
     * @return 结果
     */
    public int insertOpBloodEntrustOrderSample(OpBloodEntrustOrderSample opBloodEntrustOrderSample);

    /**
     * 修改血样样品委托-样品对应
     * 
     * @param opBloodEntrustOrderSample 血样样品委托-样品对应
     * @return 结果
     */
    public int updateOpBloodEntrustOrderSample(OpBloodEntrustOrderSample opBloodEntrustOrderSample);

    /**
     * 通过血样样品委托-样品对应主键更新删除标志
     *
     * @param opBloodEntrustOrderId 血样样品委托-样品对应ID
     * @return 结果
     */
    public int updateDeleteFlagById(@Param("opBloodEntrustOrderSampleId") String opBloodEntrustOrderSampleId,@Param("updateUserId") String updateUserId);



    List<OpBloodEntrustOrderSample> selectAllByOpBloodEntrustOrderId(String opBloodEntrustOrderId);

    int updateDeleteFlagByOrderSample(@Param("id")String id, @Param("now") Date now, @Param("updateUserId") String updateUserId);

    void updateDeleteById(@Param("updateBy") String updateBy, @Param("opBloodEntrustOrderSampleId")String opBloodEntrustOrderSampleId);

    void updateReceiveById(@Param("receiverId") String receiverId, @Param("opBloodEntrustOrderSampleId")String opBloodEntrustOrderSampleId);

    String selectOrderIdById(@Param("opBloodEntrustOrderSampleId") String opBloodEntrustOrderSampleId);

    OpBloodEntrustOrderSample selectById(@Param("opBloodEntrustOrderSampleId") String opBloodEntrustOrderSampleId);

    List<OpJczxBloodTestModelBo> selectSampleModelByIdList(@Param("entrustOrderIdList")List<String> entrustOrderIdList);

    OpBloodEntrustOrderSample selectBySampleNo(@Param("sampleNo") String sampleNo);

    void updateResultBySampleNo(OpBloodEntrustOrderSample sampleUpdate);

    void updateTestUserBySampleNoSet(Set<String> sampleNoSet, String testUserId, Date testTime);

    int countUntestedSamplesByOrderId(@Param("orderId") String orderId);

    List<OpBloodEntrustOrderSample> getBaseByResultNo(@Param("resultNo") String resultNo);

    int selectExaminCountByIdList(@Param("list")List<String> list);

    int countUnexaminedItemsByOrderId(@Param("orderId")String orderId);

    int batchUpdateExamineFields(@Param("passedIds")List<String> passedIds, @Param("examineUserId")String examineUserId,@Param("examineUser") String examineUser, @Param("examineTime")Date examineTime);

    int updateExamineNoteById(OpBloodEntrustOrderSample sample);

    int batchClearExamineFields(@Param("sampleIdList")List<String> sampleIdList, @Param("updateUserId")String updateUserId, @Param("updateTime")Date updateTime);

    List<OpBloodEntrustOrderSample> selectExamineBySampleNoList(@Param("sampleNoList")List<String> sampleNoList);

    List<OpJczxBloodTestModelBo> selectSampleModelByResultNo(@Param("resultNo")String resultNo);
}
