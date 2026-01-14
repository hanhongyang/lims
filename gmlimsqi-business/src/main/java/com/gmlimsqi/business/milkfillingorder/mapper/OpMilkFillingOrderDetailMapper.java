package com.gmlimsqi.business.milkfillingorder.mapper;

import com.gmlimsqi.business.milkfillingorder.domain.OpMilkFillingOrderDetail;

import java.util.List;

/**
 * 装奶单子Mapper接口
 * 
 * @author hhy
 * @date 2025-11-13
 */
public interface OpMilkFillingOrderDetailMapper 
{
    /**
     * 查询装奶单子
     * 
     * @param opMilkFillingOrderDetailId 装奶单子主键
     * @return 装奶单子
     */
    public OpMilkFillingOrderDetail selectOpMilkFillingOrderDetailByOpMilkFillingOrderDetailId(String opMilkFillingOrderDetailId);

    /**
     * 查询装奶单子列表
     * 
     * @param opMilkFillingOrderDetail 装奶单子
     * @return 装奶单子集合
     */
    public List<OpMilkFillingOrderDetail> selectOpMilkFillingOrderDetailList(OpMilkFillingOrderDetail opMilkFillingOrderDetail);

    /**
     * 新增装奶单子
     * 
     * @param opMilkFillingOrderDetail 装奶单子
     * @return 结果
     */
    public int insertOpMilkFillingOrderDetail(OpMilkFillingOrderDetail opMilkFillingOrderDetail);

    /**
     * 修改装奶单子
     * 
     * @param opMilkFillingOrderDetail 装奶单子
     * @return 结果
     */
    public int updateOpMilkFillingOrderDetail(OpMilkFillingOrderDetail opMilkFillingOrderDetail);

    /**
     * 通过装奶单子主键更新删除标志
     *
     * @param opMilkFillingOrderDetailId 装奶单子ID
     * @return 结果
     */
    public int updateDeleteFlagById(String opMilkFillingOrderDetailId);

    /**
     * 批量通过装奶单子主键更新删除标志
     *
     * @param opMilkFillingOrderDetailId 装奶单子ID
     * @return 结果
     */
    public int updateDeleteFlagByIds(String[] opMilkFillingOrderDetailIds);

    /**
     * 删除装奶单子
     *
     * @param opMilkFillingOrderId 装奶单子主键
     * @return 结果
     */
    public int deleteOpMilkFillingOrderDetailByOpMilkFillingOrderId(String opMilkFillingOrderId);

    /**
     * 根据主表id查询子表数据
     *
     * @param opMilkFillingOrderId 主表id
     * @return 子表数据集合
     */
     public List<OpMilkFillingOrderDetail> selectOpMilkFillingOrderDetailListByOpMilkFillingOrderId(String opMilkFillingOrderId);

}
