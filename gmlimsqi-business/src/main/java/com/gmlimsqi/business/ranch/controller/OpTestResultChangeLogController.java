package com.gmlimsqi.business.ranch.controller;

import com.gmlimsqi.business.ranch.dto.changelog.ResultChangeLogQueryDTO;
import com.gmlimsqi.business.ranch.service.IOpTestResultChangeLogService;
import com.gmlimsqi.common.core.controller.BaseController;
import com.gmlimsqi.common.core.domain.AjaxResult;
import com.gmlimsqi.common.core.page.TableDataInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RequiredArgsConstructor
@RestController
@RequestMapping("/ranch/testResultChangeLog")
public class OpTestResultChangeLogController extends BaseController {

    private final IOpTestResultChangeLogService opTestResultChangeLogService;

    @GetMapping("/list")
    public TableDataInfo list(ResultChangeLogQueryDTO dto) {
        startPage();
        return getDataTable(opTestResultChangeLogService.list(dto));
    }

    @GetMapping("/getById")
    public AjaxResult getById(Long id) {
        return AjaxResult.success(opTestResultChangeLogService.getById(id));
    }
}
