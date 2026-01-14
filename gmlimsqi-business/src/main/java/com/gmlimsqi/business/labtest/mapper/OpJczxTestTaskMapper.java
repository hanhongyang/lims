package com.gmlimsqi.business.labtest.mapper;

import java.util.List;

import com.gmlimsqi.business.labtest.dto.OpJczxTestTaskDto;
import com.gmlimsqi.business.labtest.vo.OpJczxTestTaskVo;
import org.apache.ibatis.annotations.Param;

/**
 * 检测中心检测任务Mapper接口
 *
 * @author hhy
 * @date 2025-09-22
 */
public interface OpJczxTestTaskMapper {

    /**
     * 查询检测中心检测任务列表
     *
     * @param opJczxTestTaskDto 检测中心检测任务
     * @return 检测中心检测任务集合
     */
    public List<OpJczxTestTaskVo> selectOpJczxTestTaskList(OpJczxTestTaskDto opJczxTestTaskDto);


    public OpJczxTestTaskVo selectTestTaskByEntrustOrderItemId(String entrustOrderItemId);

    // 根据委托类型查询
    List<OpJczxTestTaskVo> selectFeedTestTaskList(OpJczxTestTaskDto opJczxTestTaskDto);

    // 根据委托类型查询
    List<OpJczxTestTaskVo> selectFeedTestTaskList2(OpJczxTestTaskDto opJczxTestTaskDto);

    List<OpJczxTestTaskVo> selectPcrTestTaskList(OpJczxTestTaskDto opJczxTestTaskDto);

    /**
     * 查询 PCR 待审核列表（状态 2, 3，有未审核项目）
     */
    List<OpJczxTestTaskVo> selectPcrPendingAuditList(OpJczxTestTaskDto opJczxTestTaskDto);

    /**
     * 查询 PCR 已审核列表（状态 3, 4，无未审核项目）
     */
    List<OpJczxTestTaskVo> selectPcrAuditedList(OpJczxTestTaskDto opJczxTestTaskDto);

    List<OpJczxTestTaskVo> selectBloodTestTaskList(OpJczxTestTaskDto opJczxTestTaskDto);

    List<OpJczxTestTaskVo> selectShTestTaskList(OpJczxTestTaskDto opJczxTestTaskDto);

    /**
     * 查询 血样 已审核列表（状态 3, 4，无未审核项目）
     */
    List<OpJczxTestTaskVo> selectBloodAuditedList(OpJczxTestTaskDto opJczxTestTaskDto);

    List<OpJczxTestTaskVo> selectFeedTestTaskListGrouped(OpJczxTestTaskDto opJczxTestTaskDto);

    List<OpJczxTestTaskVo> selectPcrTestTaskListIsTest(OpJczxTestTaskDto opJczxTestTaskDto);

    List<OpJczxTestTaskVo> selectPcrExamine(OpJczxTestTaskDto opJczxTestTaskDto);

    List<OpJczxTestTaskVo> selectBloodExamine(OpJczxTestTaskDto opJczxTestTaskDto);

    List<OpJczxTestTaskVo> selectBloodTestTaskListIsTest(OpJczxTestTaskDto opJczxTestTaskDto);

    List<OpJczxTestTaskVo> selectCsfTestTaskList(OpJczxTestTaskDto opJczxTestTaskDto);

    List<OpJczxTestTaskVo> selectJhwTaskList(OpJczxTestTaskDto opJczxTestTaskDto);

    /**
     * 统计饲料检测【待化验 - 化学法】数量
     *
     * @param userId 用户ID
     * @return Feed 待检测项目数
     */
    int countFeedWaitTestChemistryByUserId(@Param("userId") String userId);

    /**
     * 统计饲料检测【待化验 - 出水分】数量
     *
     * @param userId    当前登录用户ID（权限控制）
     * @param csfItemId 初水分项目ID（来自系统配置 csfItemId）
     * @return 待化验初水分数量
     */
    int countFeedWaitTestCsfByUserId(@Param("userId") String userId,
                             @Param("csfItemId") String csfItemId);

    /**
     * 统计 PCR 待检测数量
     *
     * @return PCR 待检测数量
     */
    int countPcrWaitTest();

    /**
     * 统计生化检测待检测数量
     *
     * @return 生化待检测数量
     */
    int countBiochemistryWaitTest();

    /**
     * 统计疾病检测待检测数量
     *
     * @return 疾病待检测数量
     */
    int countDiseaseWaitTest();

    /**
     * 统计早孕待检测数量
     *
     * @return 早孕待检测数量
     */
    int countEarlyPregnancyWaitTest();

}
