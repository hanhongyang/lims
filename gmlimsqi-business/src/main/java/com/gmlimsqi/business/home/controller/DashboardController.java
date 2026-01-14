package com.gmlimsqi.business.home.controller;

import com.gmlimsqi.business.home.service.IDashBoardService;
import com.gmlimsqi.business.home.vo.StatsVo;
import com.gmlimsqi.common.core.domain.AjaxResult;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 首页请求
 * <p>
 * @author yangjw
 * @date 2026-01-05
 */
@RestController
@RequestMapping("/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final IDashBoardService dashboardService;

    @GetMapping("/statistics")
    public AjaxResult statistics() {
        StatsVo stats =  dashboardService.getStatistics();
        return AjaxResult.success(stats);
    }

}
