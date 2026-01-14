package com.gmlimsqi.business.rmts.controller;

import com.gmlimsqi.business.rmts.entity.dto.*;
import com.gmlimsqi.business.rmts.service.RmtsRanchLimsService;
import com.gmlimsqi.common.core.domain.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/rmts/ranch/lims")
public class RmtsRanchLimsController {

    @Autowired
    private RmtsRanchLimsService rmtsRanchLimsService;

    /**
     * 计划同步
     */
    @GetMapping("/plan/sync")
    public R planSync(PlanSyncDTO planSyncDTO) {
        return rmtsRanchLimsService.planSync(planSyncDTO);
    }

    /**
     * 车辆、司机等信息接口
     */
    @GetMapping("/carInfo")
    public R carInfo(CarInfoSyncDTO carInfoSyncDTO) {
        return rmtsRanchLimsService.carInfo(carInfoSyncDTO);
    }

    /**
     * 质检信息同步接口（推送数据）
     */
    @PostMapping("/quality")
    public R quality(@RequestBody QualitySyncDTO qualitySyncDTO) {
        return rmtsRanchLimsService.qualityInfo(qualitySyncDTO);
    }

     /**
      * 图片同步接口（推送数据）
      */
    @PostMapping("/photo")
    public R photo(@RequestBody PhotoSyncDTO photoSyncDTO) {
        return rmtsRanchLimsService.photoSync(photoSyncDTO);
    }

    /**
     * 质检信息同步接口（下拉数据）
     */
    @GetMapping("/factoryQuality")
    public R factoryQuality(FactoryQualityDTO factoryQualityDTO) {
        return rmtsRanchLimsService.factoryQuality(factoryQualityDTO);
    }

}
