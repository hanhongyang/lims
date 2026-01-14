package com.gmlimsqi.business.labtest.service;

import java.util.List;
import com.gmlimsqi.business.labtest.domain.OpPcrEntrustOrder;
import com.gmlimsqi.business.util.CodeGeneratorUtil;
import com.gmlimsqi.common.exception.BusinessException;

import javax.servlet.http.HttpServletResponse;

/**
 * PCR样品委托单Service接口
 * 
 * @author hhy
 * @date 2025-09-17
 */
public interface IOpPcrEntrustOrderService 
{
    /**
     * 查询PCR样品委托单
     * 
     * @param opPcrEntrustOrderId PCR样品委托单主键
     * @return PCR样品委托单
     */
    public OpPcrEntrustOrder selectOpPcrEntrustOrderByOpPcrEntrustOrderId(String opPcrEntrustOrderId);

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


    String downloadImportModel(HttpServletResponse response);
}
