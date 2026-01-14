package com.gmlimsqi.business.milkbinqualityinspection.service;

import com.gmlimsqi.business.inspectionmilktankers.domain.OpInspectionMilkTankers;
import com.gmlimsqi.business.milkbinqualityinspection.domain.OpMilkSourcePlanVO;

import java.util.List;
/** 奶源计划Service*/
public interface IOpMilkSourcePlanService {

    /**
     * 管理员全流程监控：所有奶源计划详情平铺列表
     */
    List<OpMilkSourcePlanVO> selectMilkSourcePlanMonitorList(OpInspectionMilkTankers opInspectionMilkTankers);
}
