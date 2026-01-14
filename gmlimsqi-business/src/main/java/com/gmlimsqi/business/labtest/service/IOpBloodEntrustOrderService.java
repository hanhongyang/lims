package com.gmlimsqi.business.labtest.service;


import com.gmlimsqi.business.labtest.domain.OpBloodEntrustOrder;
import com.gmlimsqi.business.labtest.dto.OpJczxTestTaskDto;
import com.gmlimsqi.business.labtest.vo.OpBloodEntrustVo;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 血样样品委托单Service接口
 * 
 * @author hhy
 * @date 2025-09-20
 */
public interface IOpBloodEntrustOrderService 
{
    /**
     * 查询血样样品委托单
     * 
     * @param opBloodEntrustOrderId 血样样品委托单主键
     * @return 血样样品委托单
     */
    public OpBloodEntrustVo selectOpBloodEntrustOrderByOpBloodEntrustOrderId(String opBloodEntrustOrderId);

    /**
     * 查询血样样品委托单列表
     * 
     * @param opBloodEntrustOrder 血样样品委托单
     * @return 血样样品委托单集合
     */
    public List<OpBloodEntrustOrder> selectOpBloodEntrustOrderList(OpBloodEntrustOrder opBloodEntrustOrder);

    /**
     * 新增血样样品委托单
     * 
     * @param opBloodEntrustVo 血样样品委托单
     * @return 结果
     */
    public int insertOpBloodEntrustOrder(OpBloodEntrustVo opBloodEntrustVo);

    /**
     * 修改血样样品委托单
     * 
     * @param opBloodEntrustOrder 血样样品委托单
     * @return 结果
     */
    public int updateOpBloodEntrustOrder(OpBloodEntrustVo opBloodEntrustOrder);

    String downloadImportModel(HttpServletResponse response, OpJczxTestTaskDto dto);
}
