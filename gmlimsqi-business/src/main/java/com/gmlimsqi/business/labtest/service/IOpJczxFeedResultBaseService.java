package com.gmlimsqi.business.labtest.service;

import java.util.List;
import com.gmlimsqi.business.labtest.domain.OpJczxFeedResultBase;
import com.gmlimsqi.business.labtest.domain.OpJczxFeedResultInfo;
import com.gmlimsqi.business.labtest.dto.OpJczxFeedResultBaseDto;
import com.gmlimsqi.business.labtest.vo.OpJczxFeedResultInitVo;
import com.gmlimsqi.business.labtest.vo.OpJczxFeedResultVo;

/**
 * 检测中心饲料检测结果基础Service接口
 * 
 * @author hhy
 * @date 2025-09-25
 */
public interface IOpJczxFeedResultBaseService 
{
    /**
     * 查询检测中心饲料检测结果基础
     * 
     * @param opJczxFeedResultBaseId 检测中心饲料检测结果基础主键
     * @return 检测中心饲料检测结果基础
     */
    public OpJczxFeedResultVo selectOpJczxFeedResultBaseByOpJczxFeedResultBaseId(String opJczxFeedResultBaseId);

    /**
     * 查询检测中心饲料检测结果基础列表
     * 
     * @param opJczxFeedResultBase 检测中心饲料检测结果基础
     * @return 检测中心饲料检测结果基础集合
     */
    public List<OpJczxFeedResultBase> selectOpJczxFeedResultBaseList(OpJczxFeedResultBaseDto dto);

    /**
     * 新增检测中心饲料检测结果基础
     * 
     * @param opJczxFeedResultBase 检测中心饲料检测结果基础
     * @return 结果
     */
    public String insertOpJczxFeedResultBase(OpJczxFeedResultBaseDto opJczxFeedResultBase) throws Exception;

    /**
     * 修改检测中心饲料检测结果基础
     * 
     * @param opJczxFeedResultBase 检测中心饲料检测结果基础
     * @return 结果
     */
    public int updateOpJczxFeedResultBase(OpJczxFeedResultBaseDto opJczxFeedResultBase) throws Exception;


    public OpJczxFeedResultInitVo getInitInfo(String invbillId, String itemId);


    public int saveCheck(OpJczxFeedResultBaseDto dto);

    public int saveExamine(OpJczxFeedResultBaseDto dto);

    public OpJczxFeedResultVo selectCsfByParentId(String parentId);

    OpJczxFeedResultVo getCsfInfoBySampleList(List<String> sampleList);
    OpJczxFeedResultVo getJhwCsfInfoBySampleList(List<String> sampleList);

    int testSubmit(OpJczxFeedResultBaseDto dto);

    int retest(List<String> opJczxFeedResultInfoId);

    List<OpJczxFeedResultInfo> selectInfoListByBaseId(String id);

    List<OpJczxFeedResultBase> selectJhwListList(OpJczxFeedResultBaseDto dto);

    OpJczxFeedResultVo selectJhwInfoByBaseId(String opJczxFeedResultBaseId);

    void backToSubmit(OpJczxFeedResultBaseDto dto);

    String insertJhwResultBase(OpJczxFeedResultBaseDto dto)throws Exception;

    int updateJhwResultBase(OpJczxFeedResultBaseDto dto)throws Exception;

    int jhwRetest(String sampleId);

    void jhwBackToSubmit(OpJczxFeedResultBaseDto dto);

    OpJczxFeedResultVo getResultBySampleNo(OpJczxFeedResultBaseDto dto);

    OpJczxFeedResultVo getResultBySampleNo2(OpJczxFeedResultBaseDto dto);
    /**
     * 获取当前用户饲料待提交数量
     * @return 待提交数量
     */
    public int countFeedWaitSubmitByCurrentUser();

    /**
     * 获取当前用户饲料待校对数量
     * @return 待校对数量
     */
    public int countFeedWaitCheckByCurrentUser();

}
