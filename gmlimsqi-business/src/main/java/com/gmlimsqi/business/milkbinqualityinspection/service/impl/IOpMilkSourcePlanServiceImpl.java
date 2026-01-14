package com.gmlimsqi.business.milkbinqualityinspection.service.impl;

import com.gmlimsqi.business.inspectionmilktankers.domain.OpInspectionMilkTankers;
import com.gmlimsqi.business.milkbinqualityinspection.domain.OpMilkSourcePlanVO;
import com.gmlimsqi.business.milkbinqualityinspection.mapper.OpMilkSourcePlanMonitorMapper;
import com.gmlimsqi.business.milkbinqualityinspection.service.IOpMilkSourcePlanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
/**
 * 奶源计划Service
 * */
@Service
public class IOpMilkSourcePlanServiceImpl implements IOpMilkSourcePlanService {
    @Autowired
    private OpMilkSourcePlanMonitorMapper opMilkSourcePlanMonitorMapper;

    @Override
    public List<OpMilkSourcePlanVO> selectMilkSourcePlanMonitorList(OpInspectionMilkTankers opInspectionMilkTankers) {
        return opMilkSourcePlanMonitorMapper.selectMilkSourcePlanMonitorList(opInspectionMilkTankers);
    }
}
