package com.gmlimsqi.business.milktankinspection.mapper;

import com.gmlimsqi.business.milktankinspection.domain.OpMilkTankInspection;
import com.gmlimsqi.common.change.annotation.ChangeLog;
import com.gmlimsqi.common.change.constant.BizType;
import com.gmlimsqi.common.change.constant.ChangeLogConstant;

import java.util.List;

/**
 * 奶罐车质检Mapper接口
 * 
 * @author hhy
 * @date 2025-11-12
 */
public interface OpMilkTankInspectionMapper 
{
    /**
     * 查询奶罐车质检
     * 
     * @param opMilkTankInspectionId 奶罐车质检主键
     * @return 奶罐车质检
     */
    public OpMilkTankInspection selectOpMilkTankInspectionByOpMilkTankInspectionId(String opMilkTankInspectionId);

    /**
     * 查询奶罐车质检列表
     * 
     * @param opMilkTankInspection 奶罐车质检
     * @return 奶罐车质检集合
     */
    public List<OpMilkTankInspection> selectOpMilkTankInspectionList(OpMilkTankInspection opMilkTankInspection);

    /**
     * 新增奶罐车质检
     * 
     * @param opMilkTankInspection 奶罐车质检
     * @return 结果
     */
    public int insertOpMilkTankInspection(OpMilkTankInspection opMilkTankInspection);

    /**
     * 修改奶罐车质检
     * 
     * @param opMilkTankInspection 奶罐车质检
     * @return 结果
     */
    @ChangeLog(
            bizType = BizType.MILK_TANK_INSPECTION,
            opType = ChangeLogConstant.OP_TYPE_UPDATE,
            bizId = "#p0.opMilkTankInspectionId",
            selectById = "selectOpMilkTankInspectionByOpMilkTankInspectionId(#bizId)",
            reason = "#p0.remark"
    )
    public int updateOpMilkTankInspection(OpMilkTankInspection opMilkTankInspection);

    /**
     * 通过奶罐车质检主键更新删除标志
     *
     * @param opMilkTankInspectionId 奶罐车质检ID
     * @return 结果
     */
    public int updateDeleteFlagById(String opMilkTankInspectionId);

    /**
     * 批量通过奶罐车质检主键更新删除标志
     *
     * @param opMilkTankInspectionId 奶罐车质检ID
     * @return 结果
     */
    public int updateDeleteFlagByIds(String[] opMilkTankInspectionIds);



}
