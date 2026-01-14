package com.gmlimsqi.business.milkbinqualityinspection.mapper;

import com.gmlimsqi.business.inspectionmilktankers.domain.OpInspectionMilkTankers;
import com.gmlimsqi.business.milkbinqualityinspection.domain.OpMilkSourcePlanVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
/**
 *  奶源计划Mapper
 * */
@Mapper
public interface OpMilkSourcePlanMonitorMapper {

    /**
     * 管理员全流程监控：所有奶源计划详情平铺列表
     */
    List<OpMilkSourcePlanVO> selectMilkSourcePlanMonitorList(OpInspectionMilkTankers opInspectionMilkTankers);
}
