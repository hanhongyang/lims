package com.gmlimsqi.business.rmts.service;

import com.gmlimsqi.business.rmts.entity.dto.*;
import com.gmlimsqi.business.rmts.entity.pojo.ApiResponse;
import com.gmlimsqi.common.core.domain.R;


public interface RmtsRanchLimsService {

    /**
     * 计划同步
     */
    R planSync(PlanSyncDTO planSyncDTO);

    /**
     * 车辆、司机等信息接口
     */
    R carInfo(CarInfoSyncDTO carInfoSyncDTO);

    /**
     * 质检信息同步接口
     */
    R qualityInfo(QualitySyncDTO qualitySyncDTO);

     /**
      * 图片同步接口
      */
    R photoSync(PhotoSyncDTO photoSyncDTO);

     /**
      * 工厂质检信息同步接口
      */
    R factoryQuality(FactoryQualityDTO factoryQualityDTO);
}
