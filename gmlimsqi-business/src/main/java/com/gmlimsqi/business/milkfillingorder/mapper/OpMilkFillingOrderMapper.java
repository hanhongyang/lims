package com.gmlimsqi.business.milkfillingorder.mapper;

import com.gmlimsqi.business.milkfillingorder.domain.OpMilkFillingOrder;

import java.util.List;

/**
 * 装奶单Mapper接口
 * 
 * @author hhy
 * @date 2025-11-10
 */
public interface OpMilkFillingOrderMapper 
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
    public int insertOpMilkFillingOrder(OpMilkFillingOrder opMilkFillingOrder);

    /**
     * 修改装奶单
     * 
     * @param opMilkFillingOrder 装奶单
     * @return 结果
     */
    public int updateOpMilkFillingOrder(OpMilkFillingOrder opMilkFillingOrder);

    /**
     * 通过装奶单主键更新删除标志
     *
     * @param opMilkFillingOrderId 装奶单ID
     * @return 结果
     */
    public int updateDeleteFlagById(String opMilkFillingOrderId);

    /**
     * 批量通过装奶单主键更新删除标志
     *
     * @param opMilkFillingOrderId 装奶单ID
     * @return 结果
     */
    public int updateDeleteFlagByIds(String[] opMilkFillingOrderIds);

    /**
     * 根据奶罐车id查询装奶单
     */
    public OpMilkFillingOrder selectOpMilkFillingOrderByMilkTankersId(String milkTankersId);

     /**
     * 查询未关联磅单的装奶单列表
     * @param opMilkFillingOrder
     * @return
     */
    List<OpMilkFillingOrder> selectUnassociatedOpMilkFillingOrderList(OpMilkFillingOrder opMilkFillingOrder);

    /**
     * 查询未推送奶源的装奶单列表
     * @param opMilkFillingOrder
     * @return
     */
    List<OpMilkFillingOrder> selectUnPushedMilkSourceOpMilkFillingOrderList(OpMilkFillingOrder opMilkFillingOrder);

}
