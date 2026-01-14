package com.gmlimsqi.business.labtest.mapper;

import com.gmlimsqi.business.labtest.domain.OpJczxFeedReportBase;
import com.gmlimsqi.business.labtest.domain.OpJczxFeedReportInfo;
import com.gmlimsqi.business.labtest.dto.OpJczxFeedReportDto;
import com.gmlimsqi.business.labtest.dto.OpJczxFeedResultInfoDto;
import com.gmlimsqi.business.labtest.vo.OpFeedReportListVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 检测中心饲料报告主Mapper接口
 * 
 * @author hhy
 * @date 2025-10-14
 */
public interface OpJczxFeedReportBaseMapper 
{
    /**
     * 查询检测中心饲料报告主
     * 
     * @param opJczxFeedReportBaseId 检测中心饲料报告主主键
     * @return 检测中心饲料报告主
     */
    public OpJczxFeedReportBase selectOpJczxFeedReportBaseByOpJczxFeedReportBaseId(String opJczxFeedReportBaseId);

    /**
     * 查询检测中心饲料报告主列表
     * 
     * @param opJczxFeedReportBase 检测中心饲料报告主
     * @return 检测中心饲料报告主集合
     */
    public List<OpFeedReportListVo> selectOpJczxFeedReportBaseList(OpJczxFeedReportDto opJczxFeedReportBase);

    /**
     * 新增检测中心饲料报告主
     * 
     * @param opJczxFeedReportBase 检测中心饲料报告主
     * @return 结果
     */
    public int insertOpJczxFeedReportBase(OpJczxFeedReportBase opJczxFeedReportBase);

    /**
     * 修改检测中心饲料报告主
     * 
     * @param opJczxFeedReportBase 检测中心饲料报告主
     * @return 结果
     */
    public int updateOpJczxFeedReportBase(OpJczxFeedReportBase opJczxFeedReportBase);

    /**
     * 通过检测中心饲料报告主主键更新删除标志
     *
     * @param opJczxFeedReportBaseId 检测中心饲料报告主ID
     * @return 结果
     */
    public int updateDeleteFlagById(@Param("opJczxFeedReportBaseId") String opJczxFeedReportBaseId,@Param("updateUserId") String updateUserId);

    /**
     * 批量通过检测中心饲料报告主主键更新删除标志
     *
     * @return 结果
     */
    public int updateDeleteFlagByIds(String[] opJczxFeedReportBaseIds);

    /**
     * 批量新增检测中心饲料报告子
     * 
     * @param opJczxFeedReportInfoList 检测中心饲料报告子列表
     * @return 结果
     */
    public int batchOpJczxFeedReportInfo(List<OpJczxFeedReportInfo> opJczxFeedReportInfoList);

    public void updateInfoDeleteByBaseId(@Param("opJczxFeedReportBaseId")String opJczxFeedReportBaseId,@Param("updateUserId") String updateUserId);

    public List<OpFeedReportListVo> selectOpJczxFeedReportBaseListStatus0(OpJczxFeedReportDto opJczxFeedReportBase);

    /**
     * 查询报告制作时的基础信息
     * @param entrustOrderSampleId
     */
    public  List<OpJczxFeedReportBase> selectReportBaseBySampleId(String entrustOrderSampleId);

    public  List<OpJczxFeedReportBase> selectReportBaseBySampleId2(String entrustOrderSampleId);
    /**
     *  查询报告制作时的详情信息
     * @param entrustOrderSampleId
     * @return
     */
    List<OpJczxFeedResultInfoDto> selectReportInfoBySampleId(String entrustOrderSampleId);

    int deleteByOpJczxFeedResultSampleId(String sampleId);

    List<OpJczxFeedReportInfo> selectReportInfoByBaseId(String opJczxFeedReportBaseId);

    /**
     * 已校准列表
     * @param opJczxFeedReportBase
     * @return
     */
    List<OpFeedReportListVo> selectOpJczxFeedReportBaseListStatus4(OpJczxFeedReportDto opJczxFeedReportBase);

    List<OpJczxFeedReportBase> selectReportBaseByOrderId(String feedEntrustOrderId);

    Integer  getStatus0Count(OpJczxFeedReportDto opJczxFeedReportBase);

    List<Map<String, Object>> getStatusCount(OpJczxFeedReportDto opJczxFeedReportBase);

    List<OpJczxFeedResultInfoDto> selectJhwReportInfoBySampleId(String entrustOrderSampleId);

    void updateSampleId(@Param("oldSampleId")String oldSampleId, @Param("newSampleId")String newSampleId);

    void updateStatus6ById(@Param("opJczxFeedReportBaseId")String opJczxFeedReportBaseId, @Param("updateUserId")String updateUserId);
}
