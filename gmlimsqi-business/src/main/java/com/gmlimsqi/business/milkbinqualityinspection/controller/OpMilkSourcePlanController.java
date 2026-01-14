package com.gmlimsqi.business.milkbinqualityinspection.controller;

import com.gmlimsqi.business.inspectionmilktankers.domain.OpInspectionMilkTankers;
import com.gmlimsqi.business.milkbinqualityinspection.domain.OpMilkSourcePlanVO;
import com.gmlimsqi.business.milkbinqualityinspection.service.IOpMilkSourcePlanService;
import com.gmlimsqi.common.core.controller.BaseController;
import com.gmlimsqi.common.core.page.TableDataInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
/**
 *  奶源计划Controller
 * */
@RestController
@RequestMapping("/OpMilkSourcePlan")
public class OpMilkSourcePlanController extends BaseController {

    @Autowired
    IOpMilkSourcePlanService iOpMilkSourcePlanService;

    /**
     * 管理员全流程监控：所有奶源计划详情平铺列表
     */
    @GetMapping("/list")
    public TableDataInfo list(OpInspectionMilkTankers opInspectionMilkTankers) {
        startPage();
        List<OpMilkSourcePlanVO> list = iOpMilkSourcePlanService.selectMilkSourcePlanMonitorList(opInspectionMilkTankers);
        return getDataTable(list);
    }
}
