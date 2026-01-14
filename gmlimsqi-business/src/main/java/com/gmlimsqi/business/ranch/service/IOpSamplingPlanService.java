package com.gmlimsqi.business.ranch.service;


import com.gmlimsqi.business.ranch.domain.OpSamplingPlan;
import com.gmlimsqi.business.ranch.domain.OpSamplingPlanSample;
import com.gmlimsqi.business.ranch.dto.SignInNotificationDTO;
import com.gmlimsqi.business.ranch.vo.OpSamplingPlanSampleMonitorVO;
import com.gmlimsqi.business.ranch.vo.QRCodeResult;
import com.gmlimsqi.business.ranch.vo.SamplingReceiveListVo;

import java.util.List;

/**
 * 取样计划主Service接口
 *
 * @author hhy
 * @date 2025-11-04
 */
public interface IOpSamplingPlanService
{
    /**
     * 查询取样计划主
     *
     * @param opSamplingPlanId 取样计划主主键
     * @return 取样计划主
     */
    public OpSamplingPlan selectOpSamplingPlanByOpSamplingPlanId(String opSamplingPlanId);

    /**
     * 查询取样计划主列表
     *
     * @param opSamplingPlan 取样计划主
     * @return 取样计划主集合
     */

    public List<OpSamplingPlan> selectOpSamplingPlanList(OpSamplingPlan opSamplingPlan);

    /**
     * 新增取样计划主
     *
     * @param opSamplingPlan 取样计划主
     * @return 结果
     */
    public String insertOpSamplingPlan(OpSamplingPlan opSamplingPlan);

    /**
     * 修改取样计划主
     *
     * @param opSamplingPlan 取样计划主
     * @return 结果
     */
    public int updateOpSamplingPlan(OpSamplingPlan opSamplingPlan);
    /**
     * 处理接收到的司机签到通知
     *
     * @param notifyDTO 来自 egap 系统的通知数据
     */
    void processSignInNotification(SignInNotificationDTO signInInfoDTO, String source);
    /**
     * 批量删除取样计划主
     *
     * @param opSamplingPlanIds 需要删除的取样计划主主键集合
     * @return 结果
     */
    public int deleteOpSamplingPlanByOpSamplingPlanIds(String[] opSamplingPlanIds);

    /**
     * 删除取样计划主信息
     *
     * @param opSamplingPlanId 取样计划主主键
     * @return 结果
     */
    public int deleteOpSamplingPlanByOpSamplingPlanId(String opSamplingPlanId);

    List<SamplingReceiveListVo> selectOpSamplingPlanReceiveList(OpSamplingPlan opSamplingPlan);

    int receiveOpSamplingPlan(List<String> opSamplingPlans);
    /**
     * 获取取样二维码
     */
    List<QRCodeResult> getSampleQRCode(String count);

    int updateStatus(String opSamplingPlanId, String number);

    String isRelease(String signInId);

    /**
     * 取样计划作废
     */
    int cancelOpSamplingPlan(String opSamplingPlanId);

     /**
     * 查询待销毁列表
     */
    List<OpSamplingPlanSample> selectOpSamplingPlanSampleListByPlanId(OpSamplingPlanSample opSamplingPlanSample);

    /**
     * 销毁样品
     * @param opSamplingPlanSampleId 取样计划-样品主键
     * @return
     */
    int destroyOpSamplingPlanSample(String opSamplingPlanSampleId);

    /**
     * 关联取样计划
     * @param opSamplingPlanSample
     * @return
     */
    int linkOpSamplingPlanSample(OpSamplingPlanSample opSamplingPlanSample);

    /**
     * 新增牧场垫料/库存取样计划
     */
    int addKCDL(OpSamplingPlan opSamplingPlan);

    /**
     * (新) 管理员全流程监控：查询样品平铺列表（含检测项目统计进度）
     */
    List<OpSamplingPlanSampleMonitorVO> selectSampleMonitorList(OpSamplingPlanSample opSamplingPlanSample);

    SamplingReceiveListVo selectopSamplingPlanSampleInfoById(String opSamplingPlanSampleId);

    List<SamplingReceiveListVo> selectOpSamplingPlanReceiveList2(OpSamplingPlan opSamplingPlan);

    /**
     * 修改取样物料
     */
    int editOpSamplingPlanSampleMaterial(OpSamplingPlanSample opSamplingPlanSample);

    /**
     * 修改样品检测项目
     */
    int editOpSamplingPlanSampleItem(OpSamplingPlanSample opSamplingPlanSample);
}
