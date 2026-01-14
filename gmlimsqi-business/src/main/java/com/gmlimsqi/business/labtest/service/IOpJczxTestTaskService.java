package com.gmlimsqi.business.labtest.service;

import com.gmlimsqi.business.labtest.dto.OpJczxBloodResultDto;
import com.gmlimsqi.business.labtest.dto.OpJczxPcrResultDto;
import com.gmlimsqi.business.labtest.dto.OpJczxTestTaskDto;
import com.gmlimsqi.business.labtest.vo.OpJczxTestTaskVo;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 检测中心检测任务Service接口
 * 
 * @author hhy
 * @date 2025-09-22
 */
public interface IOpJczxTestTaskService 
{
    /**
     * 查询检测中心检测任务
     * 
     * @param opJczxTestTaskId 检测中心检测任务主键
     * @return 检测中心检测任务
     */
    public OpJczxTestTaskVo selectOpJczxTestTaskByOpJczxTestTaskId(String opJczxTestTaskId);

    /**
     * 查询检测中心检测任务列表
     * 
     * @param opJczxTestTaskDto 检测中心检测任务
     * @return 检测中心检测任务集合
     */
    public List<OpJczxTestTaskVo> selectOpJczxTestTaskList(OpJczxTestTaskDto opJczxTestTaskDto);

    public void generatePcrTestExcel(HttpServletResponse response, OpJczxTestTaskDto dto );


    void generateBloodTestExcel(HttpServletResponse response, OpJczxTestTaskDto dto);

    public String beginPcrTask(OpJczxTestTaskDto dto);

    public String beginBloodTask(List<String> entrustOrderIdList);

    void savePcrResultInfoList(OpJczxPcrResultDto param);

    void saveBloodResultInfoList(OpJczxBloodResultDto param);

    /**
     * 生成近红外检测Excel
     * @param response HttpServletResponse
     * @param entrustOrderIds 委托单ID列表
     */
    void generateNirTestExcel(HttpServletResponse response, List<String> entrustOrderIds);

    /**
     * 上传近红外报告（按委托单）
     * @param entrustOrderId 委托单ID
     * @param fileId 文件ID
     */
    void uploadNirReportForOrder(String entrustOrderId, String fileId);

    List<OpJczxTestTaskVo> selectJhwTaskList(OpJczxTestTaskDto opJczxTestTaskDto);

    /**
     * 根据当前用户统计饲料检测【待化验 - 化学法】数量
     *
     * @return Feed 待检测项目数
     */
    int countFeedWaitTestChemistryByCurrentUser();

    /**
     * 根据当前用户统计饲料检测【待化验 - 出水分】数量
     *
     * @return 待化验初水分数量
     */
    int countFeedWaitTestCsfByCurrentUser();

    /**
     * 根据当前用户统计PCR检测【待化验】数量
     *
     * @return PCR 待检测项目数
     */
    int countPcrWaitTest();

    /**
     * 统计病害待检测数量
     * @return 数量
     */
    public int countDiseaseWaitTest();

    /**
     * 统计生化待检测数量
     * @return 数量
     */
    public int countBiochemistryWaitTest();

    /**
     * 统计早产待检测数量
     * @return 数量
     */
    public int countEarlyPregnancyWaitTest();

}
