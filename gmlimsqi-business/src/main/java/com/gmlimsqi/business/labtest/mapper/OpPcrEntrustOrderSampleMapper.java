package com.gmlimsqi.business.labtest.mapper;

import java.util.Date;
import java.util.List;
import java.util.Set;

import com.gmlimsqi.business.labtest.bo.OpJczxPcrTestModelBo;
import com.gmlimsqi.business.labtest.domain.OpPcrEntrustOrder;
import com.gmlimsqi.business.labtest.domain.OpPcrEntrustOrderItem;
import com.gmlimsqi.business.labtest.domain.OpPcrEntrustOrderSample;
import org.apache.ibatis.annotations.Param;

/**
 * pcr样品委托单-样品Mapper接口
 * 
 * @author hhy
 * @date 2025-09-17
 */
public interface OpPcrEntrustOrderSampleMapper 
{

    /**
     * 查询pcr样品委托单-样品
     * 
     * @param opPcrEntrustOrderSampleId pcr样品委托单-样品主键
     * @return pcr样品委托单-样品
     */
    public OpPcrEntrustOrderSample selectOpPcrEntrustOrderSampleByOpPcrEntrustOrderSampleId(String opPcrEntrustOrderSampleId);
    List<OpPcrEntrustOrderSample> selectDeletedSamplesByOrderId(String orderId);
    /**
     * 查询pcr样品委托单-样品列表
     * 
     * @param opPcrEntrustOrderSample pcr样品委托单-样品
     * @return pcr样品委托单-样品集合
     */
    public List<OpPcrEntrustOrderSample> selectOpPcrEntrustOrderSampleList(OpPcrEntrustOrderSample opPcrEntrustOrderSample);

    /**
     * 新增pcr样品委托单-样品
     * 
     * @param opPcrEntrustOrderSample pcr样品委托单-样品
     * @return 结果
     */
    public int insertOpPcrEntrustOrderSample(OpPcrEntrustOrderSample opPcrEntrustOrderSample);


    void updateDeleteByOrderId(@Param("updateBy") String updateBy, @Param("opPcrEntrustOrderId") String opPcrEntrustOrderId);

    void updateDeleteById(@Param("updateBy") String updateBy, @Param("opPcrEntrustOrderSampleId")String opPcrEntrustOrderSampleId);

    void updateReceiveById(@Param("receiverId") String receiverId, @Param("opPcrEntrustOrderSampleId")String opPcrEntrustOrderSampleId);

    String selectOrderIdById(@Param("sampleId") String sampleId);

    public int  updateOpPcrEntrustOrderSample(OpPcrEntrustOrderSample opPcrEntrustOrderSample);

    public List<OpPcrEntrustOrderSample> selectOrderIdByIdList(List<String> entrustOrderSampleIdList);

    public List<OpJczxPcrTestModelBo>  selectSampleModelByIdList(List<String> entrustOrderSampleIdList);

    public void updateTestUserBySampleNoSet(@Param("sampleNoSet")Set<String> sampleNoSet,@Param("testUserId") String testUserId,@Param("testTime") Date testTime);

    List<OpJczxPcrTestModelBo> selectSampleModelByNoList(@Param("entrustOrderNoList")List<String> entrustOrderNoList, @Param("pcrTaskItemType")String pcrTaskItemType);

    //查询已审核的样品
    List<OpPcrEntrustOrderSample> selectExamineBySampleNoList(@Param("sampleNoList")List<String> sampleNoList);

    void updateExamineNoteBySampleNo(@Param("sampleNo")String sampleNo, @Param("examineNote") String examineNote);

    List<OpJczxPcrTestModelBo> selectSampleModelByResultNo(@Param("resultNo")String resultNo);

    void updateSendStatusByOrderIdAndPcrTaskItemType(@Param("opPcrEntrustOrderId")String opPcrEntrustOrderId, @Param("pcrTaskItemType")String pcrTaskItemType);

    void batchUpdateExamineFields(@Param("orderId") String orderId,
                                  @Param("examineUserId") String examineUserId,
                                  @Param("examineUser") String examineUser,
                                  @Param("examineTime") Date examineTime);
}
