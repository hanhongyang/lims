package com.gmlimsqi.business.rmts.entity.vo;

import com.gmlimsqi.business.rmts.entity.pojo.CarInfo;
import com.gmlimsqi.business.rmts.entity.pojo.DriverInfo;
import com.gmlimsqi.business.rmts.entity.pojo.TrailerInfo;
import lombok.Data;

import java.util.List;

/**
 * 车辆信息接口返回Data实体
 */
@Data
public class CarInfoSyncVO {
    private List<CarInfo> carInfo; // 车头信息
    private List<DriverInfo> driverInfo; // 司机信息
    private List<TrailerInfo> trailerInfo; // 挂车信息

}