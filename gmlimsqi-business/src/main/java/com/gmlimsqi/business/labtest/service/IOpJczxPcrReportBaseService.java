package com.gmlimsqi.business.labtest.service;


import com.gmlimsqi.business.labtest.domain.OpJczxPcrReportBase;
import com.gmlimsqi.business.labtest.domain.OpPcrEntrustOrder;
import com.gmlimsqi.business.labtest.dto.OpJczxTestTaskDto;
import com.gmlimsqi.business.labtest.dto.PcrReportSendDTO;
import com.gmlimsqi.business.labtest.vo.OpPcrReportItemListVo;
import com.gmlimsqi.business.labtest.vo.OpPcrReportListVo;
import com.gmlimsqi.business.labtest.vo.ReportEmailVo;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * pcr报告主Service接口
 * 
 * @author hhy
 * @date 2025-10-20
 */
public interface IOpJczxPcrReportBaseService 
{
    /**
     * 查询pcr报告主
     *
     * @return pcr报告主
     */
    public  List<OpPcrReportItemListVo>  selectOpJczxPcrReportBase(OpJczxPcrReportBase opJczxPcrReportBase);

    /**
     * 查询pcr报告主列表
     * 
     * @return pcr报告主集合
     */
    public List<OpPcrReportListVo> selectOpJczxPcrReportBaseList(OpJczxTestTaskDto opJczxTestTaskDto);

    /**
     * 新增pcr报告主
     * 
     * @param opJczxPcrReportBase pcr报告主
     * @return 结果
     */
    public int insertOpJczxPcrReportBase(OpJczxPcrReportBase opJczxPcrReportBase);

    /**
     * 修改pcr报告主
     * 
     * @param opJczxPcrReportBase pcr报告主
     * @return 结果
     */
    public int updateOpJczxPcrReportBase(OpJczxPcrReportBase opJczxPcrReportBase);

    /**
     * 批量删除pcr报告主
     * 
     * @param opJczxPcrReportBaseIds 需要删除的pcr报告主主键集合
     * @return 结果
     */
    public int deleteOpJczxPcrReportBaseByOpJczxPcrReportBaseIds(String[] opJczxPcrReportBaseIds);

    /**
     * 删除pcr报告主信息
     * 
     * @param opJczxPcrReportBaseId pcr报告主主键
     * @return 结果
     */
    public int deleteOpJczxPcrReportBaseByOpJczxPcrReportBaseId(String opJczxPcrReportBaseId);

    int verifyOpJczxPcrReportBase(OpJczxPcrReportBase opJczxPcrReportBase);

    int commitOpJczxPcrReportBase(OpJczxPcrReportBase opJczxPcrReportBase);

    void generatePcrReportExcel(HttpServletResponse response, OpPcrEntrustOrder order);

     /**
     * 发送pcr报告
     *
     * @param pcrReportSendDTO pcr报告主
     * @return 结果
     */
    int sendOpPcrReportBase(PcrReportSendDTO pcrReportSendDTO);

    List<ReportEmailVo> selectOpJczxPcrReportBaseEmailList(String opPcrEntrustOrderId);

    /**
     * 查询PCR报告详情（包含排序表头和虚拟对照行）
     */
    Map<String, Object> selectPcrReportDetail(OpPcrEntrustOrder dto);

    /**
     * 批量发送PCR报告
     * @param pcrReportSendDTOList
     * @return
     */
    int batchSendPcrReport(List<PcrReportSendDTO> pcrReportSendDTOList);

    Map<String, Object> selectPcrReportDetail2(OpPcrEntrustOrder dto);

    List<OpPcrReportListVo> queryPcrReport(OpJczxTestTaskDto opJczxTestTaskDto);
}
