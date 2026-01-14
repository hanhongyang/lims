package com.gmlimsqi.business.report.controller;

import com.gmlimsqi.common.core.domain.AjaxResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/report")
public class ReportController {

    /**
     * 饲料厂原料检测数据台账
     */
     @RequestMapping("/feedFactoryRawMaterialCheckData")
     public AjaxResult feedFactoryRawMaterialCheckData() {
         return AjaxResult.success();
     }

}
