
package com.gmlimsqi.business.ranch.service;

import java.util.List;
import java.util.Map;

import com.gmlimsqi.business.ranch.domain.OpSamplingPlanSample;
import com.gmlimsqi.business.ranch.domain.OpTestResultBase;
import com.gmlimsqi.business.ranch.dto.GetJCKCTestDTO;
import com.gmlimsqi.business.ranch.dto.OpSamplingPlanSamplePushSapDTO;
import com.gmlimsqi.business.ranch.dto.changelog.ResultChangeSaveDTO;
import com.gmlimsqi.business.ranch.vo.JckcTestVO;
import com.gmlimsqi.business.ranch.vo.SampleInfoVO;
import com.gmlimsqi.business.ranch.vo.SampleTaskVo;
import com.gmlimsqi.business.ranch.vo.SamplingPlanReportVO;
import org.apache.ibatis.annotations.Param;

/**
 * 样品化验Service接口
 * * @author hhy
 * @date 2025-11-07
 */
public interface IOpTestResultBaseService
{
    /**
     * 查询样品化验
     */
    public OpTestResultBase selectOpTestResultBaseById(String id);

    /**
     * 查询样品化验列表 (Tab 2, 3, 4)
     */
    public List<OpTestResultBase> selectOpTestResultBaseList(OpTestResultBase opTestResultBase);

    /**
     * 新增样品化验 (用于弹窗)
     */
    public int insertOpTestResultBase(OpTestResultBase opTestResultBase);

    /**
     * 修改样品化验 (用于弹窗 - 保存)
     */
    public int updateOpTestResultBase(OpTestResultBase opTestResultBase);

    /**
     * (新) 查询 "待化验" (Tab 1) 列表
     */
    public List<SampleTaskVo> selectPendingTaskList(SampleTaskVo queryParams);

    /**
     * (修改) 从待办任务开始化验
     * @param opSamplingPlanItemIds 样品-项目ID列表 (来自 SampleTaskVo.opSamplingPlanItemId)
     * @return 结果 (返回新生成的 OpTestResultBase ID)
     */
    public String startTest(@Param("opSamplingPlanItemIds") List<String> opSamplingPlanItemIds) throws Exception;

    /**
     * (新) 提交化验单 (待提交 -> 待审核)
     */
    public int submitTestResult(OpTestResultBase opTestResultBase);

    /**
     * (新) 审核通过 (待审核 -> 已审核)
     */
    public int approveTestResult(String id);

    /**
     * (新) 审核退回 (待审核 -> 待提交)
     */
    public int rejectTestResult(String id, String reason);

    /**
     * (新) 取消审核 (已审核 -> 待审核)
     */
    public int cancelApprove(String id);

    /**
     * (新) 查询所有Tab的角标数量
     */
    public Map<String, Long> getTestResultCounts(SampleTaskVo pendingParams, OpTestResultBase listParams);

    public int retest(List<String> infoId);

    /**
     * 复检单据 (单条 infoId，进入待审核流程)
     */
    public int retestSingle(String infoId);

    /**
     * 查询待审核的复检记录
     */
    public List<OpTestResultBase> selectRetestPendingList(OpTestResultBase opTestResultBase);

    /**
     * 审核通过复检单条记录 (待审核 -> 待化验)
     */
    public int approveRetestSingle(String id);

    /**
     * 查询样品未推送SAP的列表
     */
    List<OpSamplingPlanSample> selectUnPushSapList(OpSamplingPlanSample opSamplingPlanSample);

    /**
     * 推送SAP
     */
    int pushSap(OpSamplingPlanSamplePushSapDTO opSamplingPlanSample);

    /**
     * 合格判定
     * @param opSamplingPlanSample
     * @return
     */
    int judgePass(OpSamplingPlanSample opSamplingPlanSample);

    /**
     * 查询样品检测详情
     */
    SampleInfoVO sampleTestDetail(String opSamplingPlanSampleId);

     /**
      * 自动绑定批次
      * @param opSamplingPlanSampleId
      * @return
      */
    int autoBindBatch(String opSamplingPlanSampleId);

     /**
      * 修改样品信息
      * @param opSamplingPlanSample
      * @return
      */
    int updateSampleInfo(OpSamplingPlanSample opSamplingPlanSample);

    /**
     * 打印报告
     * @param opSamplingPlanSampleId
     * @return
     */
    SamplingPlanReportVO printReport(String opSamplingPlanSampleId);

    /**
     * 调用存储过程获取检测结果
     * @param getJCKCTestDTO
     * @return
     */
    List<JckcTestVO> callProTransGetJCKCTest(GetJCKCTestDTO getJCKCTestDTO);

    /**
     * 检测结果变更
     * @param resultChangeSaveDTO 变更信息
     * @return 影响行数
     */
    int changeResult(ResultChangeSaveDTO resultChangeSaveDTO);

     /**
      * 化验审核通过（手机端使用）
      */
    int approveTest(OpTestResultBase opTestResultBase);

    int jczxAdd(OpSamplingPlanSample opSamplingPlanSample);
}
