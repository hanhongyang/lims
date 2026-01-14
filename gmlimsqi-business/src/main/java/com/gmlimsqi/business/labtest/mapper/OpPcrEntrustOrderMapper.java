package com.gmlimsqi.business.labtest.mapper;

import com.gmlimsqi.business.labtest.domain.OpPcrEntrustOrder;
import com.gmlimsqi.business.labtest.dto.OpJczxTestTaskDto;
import com.gmlimsqi.business.labtest.vo.OpPcrReportListVo;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * PCR样品委托单Mapper接口
 * 
 * @author hhy
 * @date 2025-09-17
 */
public interface OpPcrEntrustOrderMapper 
{
    /**
     * 查询PCR样品委托单
     * 
     * @param opPcrEntrustOrderId PCR样品委托单主键
     * @return PCR样品委托单
     */
    public OpPcrEntrustOrder selectOpPcrEntrustOrderByOpPcrEntrustOrderId(String opPcrEntrustOrderId);
    //查询样品委托单
    public OpPcrEntrustOrder selectOrderDetailById(@Param("opPcrEntrustOrderId") String opPcrEntrustOrderId);

    /**
     * 查询PCR样品委托单列表
     * 
     * @param opPcrEntrustOrder PCR样品委托单
     * @return PCR样品委托单集合
     */
    public List<OpPcrEntrustOrder> selectOpPcrEntrustOrderList(OpPcrEntrustOrder opPcrEntrustOrder);

    /**
     * 新增PCR样品委托单
     * 
     * @param opPcrEntrustOrder PCR样品委托单
     * @return 结果
     */
    public int insertOpPcrEntrustOrder(OpPcrEntrustOrder opPcrEntrustOrder);

    /**
     * 修改PCR样品委托单
     * 
     * @param opPcrEntrustOrder PCR样品委托单
     * @return 结果
     */
    public int updateOpPcrEntrustOrder(OpPcrEntrustOrder opPcrEntrustOrder);


    public OpPcrEntrustOrder selectByNo(@Param("entrustOrderNo") String entrustOrderNo);

    public int updateOrderStatusBySampleIdList(@Param("sampleIdList") List<String> entrustOrderSampleIdList);

    void updateOrderStatusByNoList(@Param("entrustOrderNoList") List<String> entrustOrderNoList, @Param("pcrTaskItemType") String pcrTaskItemType);
    /**
     * 更新委托单状态
     */
    int updateStatusById(@Param("opPcrEntrustOrderId") String opPcrEntrustOrderId,
                         @Param("status") String status,
                         @Param("examineUser") String examineUser,
                         @Param("examineUserId") String examineUserId,
                         @Param("examineTime") Date examineTime,
                         @Param("updateBy") String updateBy,
                         @Param("updateTime") Date updateTime
    );

    /**
     * 查询委托单检测项目
     * @param pcrEntrustOrderId
     * @return
     */
    List<String> selectItemTypeByEntrustOrderId(String pcrEntrustOrderId);

    List<OpPcrReportListVo> selectPcrAuditedList(OpJczxTestTaskDto opJczxTestTaskDto);

    void updateSendStatusByIdList(@Param("successId")List<String> successId);
}
