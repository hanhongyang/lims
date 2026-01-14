package com.gmlimsqi.business.milkfillingorder.service;

import com.gmlimsqi.business.milkfillingorder.domain.OpMilkFillingOrder;

import java.util.List;

/**
 * 装奶单Service接口
 * 
 * @author hhy
 * @date 2025-11-10
 */
public interface IOpMilkFillingOrderService 
{
    /**
     * 查询装奶单
     * 
     * @param opMilkFillingOrderId 装奶单主键
     * @return 装奶单
     */
    public OpMilkFillingOrder selectOpMilkFillingOrderByOpMilkFillingOrderId(String opMilkFillingOrderId);

    /**
     * 查询装奶单列表
     * 
     * @param opMilkFillingOrder 装奶单
     * @return 装奶单集合
     */
    public List<OpMilkFillingOrder> selectOpMilkFillingOrderList(OpMilkFillingOrder opMilkFillingOrder);

    /**
     * 新增装奶单
     *
     * @param opMilkFillingOrder 装奶单
     * @return 结果
     */
    public String insertOpMilkFillingOrder(OpMilkFillingOrder opMilkFillingOrder);

    /**
     * 修改装奶单
     * 
     * @param opMilkFillingOrder 装奶单
     * @return 结果
     */
    public int updateOpMilkFillingOrder(OpMilkFillingOrder opMilkFillingOrder);

     /**
     * 审核装奶单
     *
     * @param opMilkFillingOrderId 装奶单主键
     * @return 结果
     */
    int audit(String opMilkFillingOrderId);

    /**
     * 根据装奶单主键读取磅单数据并返回
     * @param opMilkFillingOrderId
     * @return
     */
    OpMilkFillingOrder readByDriverCode(String opMilkFillingOrderId);

     /**
     * 查询未关联磅单的装奶单列表
     * @param opMilkFillingOrder
     * @return
     */
    List<OpMilkFillingOrder> selectUnassociatedOpMilkFillingOrderList(OpMilkFillingOrder opMilkFillingOrder);

    /**
     * 装奶单关联磅单
     * @param opMilkFillingOrder
     * @return
     */
    int associate(OpMilkFillingOrder opMilkFillingOrder);

    /**
     * 装奶单手动关联磅单
     * @param opMilkFillingOrder
     * @return
     */
    int manualAssociate(OpMilkFillingOrder opMilkFillingOrder);

    /**
     * 装奶单推送奶源系统
     * @param opMilkFillingOrderId
     * @return
     */
    int pushMilkSource(String opMilkFillingOrderId);

}
