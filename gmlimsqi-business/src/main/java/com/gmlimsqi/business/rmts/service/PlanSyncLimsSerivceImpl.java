package com.gmlimsqi.business.rmts.service;

import com.alibaba.fastjson2.JSON;
import com.gmlimsqi.business.rmts.entity.dto.*;
import com.gmlimsqi.business.rmts.entity.pojo.ApiResponse;
import com.gmlimsqi.business.rmts.entity.pojo.CarInfo;
import com.gmlimsqi.business.rmts.entity.pojo.DriverInfo;
import com.gmlimsqi.business.rmts.entity.pojo.TrailerInfo;
import com.gmlimsqi.business.rmts.entity.vo.CarInfoSyncVO;
import com.gmlimsqi.business.rmts.entity.vo.PlanSyncVO;
import com.gmlimsqi.business.util.RmtsRanchLimsHttpUtil;
import com.gmlimsqi.business.util.SignatureUtils;
import com.gmlimsqi.common.annotation.FunLog;
import com.gmlimsqi.common.core.domain.R;
import com.gmlimsqi.common.core.domain.entity.SysDept;
import com.gmlimsqi.common.enums.BusinessType;
import com.gmlimsqi.common.enums.FunTypeEnums;
import com.gmlimsqi.common.exception.ServiceException;
import com.gmlimsqi.common.utils.SecurityUtils;
import com.gmlimsqi.system.mapper.SysDeptMapper;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 计划同步服务实现类
 */
@Service
public class PlanSyncLimsSerivceImpl implements RmtsRanchLimsService {

    private final SysDeptMapper sysDeptMapper;

    public PlanSyncLimsSerivceImpl(SysDeptMapper sysDeptMapper) {
        this.sysDeptMapper = sysDeptMapper;
    }

    /**
     * 计划同步
     * @param planSyncDTO
     * @return
     */
    @Override
    @FunLog(title = "生奶追溯系统-牧场LIMS-计划同步接口", systemName = FunTypeEnums.RMTSRANCHLIMS, description = "生奶追溯系统-牧场LIMS-计划同步接口", businessType = BusinessType.OTHER)
    public R planSync(PlanSyncDTO planSyncDTO) {
        if (planSyncDTO.getSchedulingDate() == null) {
            throw new IllegalArgumentException("计划日期不能为空");
        }

        Long deptId = SecurityUtils.getDeptId();
        SysDept sysDept = sysDeptMapper.selectDeptById(deptId);
        if (sysDept == null) {
            throw new ServiceException("获取牧场信息失败");
        }

//        planSyncDTO.setPastureCode(sysDept.getSapName());

        // 日期格式校验
        Date currentDate = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String currentDateStr = sdf.format(currentDate);
        String schedulingDateStr = sdf.format(planSyncDTO.getSchedulingDate());

        // 计划日期不能早于当前日期
        /*if (schedulingDateStr.compareTo(currentDateStr) < 0) {
            throw new IllegalArgumentException("计划日期不能早于当前日期");
        }*/

        String jsonString = JSON.toJSONString(planSyncDTO);

        // 构建请求参数
        ApiResponse responseR = RmtsRanchLimsHttpUtil.syncPlan(jsonString);

        // 先写死返回的数据给前端
        /*ApiResponse<List<PlanSyncVO>> responseR = new ApiResponse<>();
        responseR.setStatus("0000");
        responseR.setMessage("成功");


        PlanSyncVO planSyncVO = new PlanSyncVO();
        planSyncVO.setSchedulingDate(new Date());
        planSyncVO.setOrderNumber("JH20251114917");
        planSyncVO.setCarIndex(1);
        planSyncVO.setPastureCode("8177");
        planSyncVO.setPastureName("金山牧场");
        planSyncVO.setFactoryCode("8177");
        planSyncVO.setFactoryName("金山牧场");
        planSyncVO.setItemsCode("00001");
        planSyncVO.setItemsName("物料01");
        planSyncVO.setMilkQuantityPlan(1000.0);

        PlanSyncVO planSyncVO1 = new PlanSyncVO();
        planSyncVO1.setSchedulingDate(new Date());
        planSyncVO1.setOrderNumber("20250914082444805492");
        planSyncVO1.setCarIndex(13008);
        planSyncVO1.setPastureCode("900147");
        planSyncVO1.setPastureName("澳亚（赤峰分公司）");
        planSyncVO1.setFactoryCode("8031");
        planSyncVO1.setFactoryName("郑州工厂");
        planSyncVO1.setItemsCode("1000046577");
        planSyncVO1.setItemsName("生鲜牛奶");
        planSyncVO1.setMilkQuantityPlan(3000.0);*/

//        responseR.setData(List.of(planSyncVO, planSyncVO1));


        if (!"0000".equals(responseR.getStatus())) {
            return R.fail(responseR.getMessage());
        }

        return R.ok(responseR.getData());
    }

    @Override
    @FunLog(title = "生奶追溯系统-牧场LIMS-车辆、司机等信息接口", systemName = FunTypeEnums.RMTSRANCHLIMS, description = "生奶追溯系统-牧场LIMS-车辆、司机等信息接口", businessType = BusinessType.OTHER)
    public R carInfo(CarInfoSyncDTO carInfoSyncDTO) {
        if (carInfoSyncDTO.getOrderNumber() == null) {
            throw new IllegalArgumentException("订单号不能为空");
        }

        String jsonString = JSON.toJSONString(carInfoSyncDTO);

        // 构建请求参数
        ApiResponse responseR = RmtsRanchLimsHttpUtil.syncCarInfo(jsonString);

        // 先写死返回的数据给前端
        /*ApiResponse<CarInfoSyncVO> responseR = new ApiResponse<>();
        responseR.setStatus("0000");
        responseR.setMessage("成功");

        CarInfoSyncVO carInfoSyncVO = new CarInfoSyncVO();

        CarInfo carInfo = new CarInfo();
        carInfo.setCarId(123456);
        carInfo.setCarNumber("鲁A12345");
        carInfoSyncVO.setCarInfo(List.of(carInfo));

        DriverInfo driverInfo = new DriverInfo();
        driverInfo.setDriverId(654321);
        driverInfo.setDriverName("张三");
        carInfoSyncVO.setDriverInfo(List.of(driverInfo));

        TrailerInfo trailerInfo = new TrailerInfo();
        trailerInfo.setTrailerId(987654);
        trailerInfo.setTrailerNumber("鲁B65432");
        carInfoSyncVO.setTrailerInfo(List.of(trailerInfo));

        responseR.setData(carInfoSyncVO);*/

        if (!"0000".equals(responseR.getStatus())) {
            return R.fail(responseR.getMessage());
        }

        return R.ok(responseR.getData());
    }

    @Override
    @FunLog(title = "生奶追溯系统-牧场LIMS-质检信息同步接口", systemName = FunTypeEnums.RMTSRANCHLIMS, description = "生奶追溯系统-牧场LIMS-质检信息同步接口", businessType = BusinessType.OTHER)
    public R qualityInfo(QualitySyncDTO qualitySyncDTO) {
        String jsonString = JSON.toJSONString(qualitySyncDTO);

        // 构建请求参数
        ApiResponse responseR = RmtsRanchLimsHttpUtil.syncQuality(jsonString);

        if (!"0000".equals(responseR.getStatus())) {
            return R.fail(responseR.getMessage());
        }

        return R.ok(responseR.getData());
    }

    @Override
    @FunLog(title = "生奶追溯系统-牧场LIMS-图片同步接口", systemName = FunTypeEnums.RMTSRANCHLIMS, description = "生奶追溯系统-牧场LIMS-图片同步接口", businessType = BusinessType.OTHER)
    public R photoSync(PhotoSyncDTO photoSyncDTO) {
        String jsonString = JSON.toJSONString(photoSyncDTO);

        // 构建请求参数
        ApiResponse responseR = RmtsRanchLimsHttpUtil.syncPhoto(jsonString);

        if (!"0000".equals(responseR.getStatus())) {
            return R.fail(responseR.getMessage());
        }

        return R.ok(responseR.getData());
    }

    @Override
    @FunLog(title = "生奶追溯系统-牧场LIMS-工厂质检信息同步接口", systemName = FunTypeEnums.RMTSRANCHLIMS, description = "生奶追溯系统-牧场LIMS-工厂质检信息同步接口", businessType = BusinessType.OTHER)
    public R factoryQuality(FactoryQualityDTO factoryQualityDTO) {
        // 日期格式校验
        Date currentDate = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String currentDateStr = sdf.format(currentDate);
        String schedulingDateStr = sdf.format(factoryQualityDTO.getSchedulingDate());
        // 计划日期不能早于当前日期
        /*if (schedulingDateStr.compareTo(currentDateStr) < 0) {
            throw new IllegalArgumentException("计划日期不能早于当前日期");
        }*/

        String jsonString = JSON.toJSONString(factoryQualityDTO);

        // 构建请求参数
        ApiResponse responseR = RmtsRanchLimsHttpUtil.syncFactoryQuality(jsonString);

        if (!"0000".equals(responseR.getStatus())) {
            return R.fail(responseR.getMessage());
        }

        return R.ok(responseR.getData());
    }

    public static void main(String[] args) {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String dateStr = sdf.format(date);
        PlanSyncDTO planSyncDTO = new PlanSyncDTO();
        planSyncDTO.setSchedulingDate(date);
        planSyncDTO.setPastureCode(null);

        String jsonString = JSON.toJSONString(planSyncDTO);

        System.out.println(jsonString);
    }

    /*public static void main(String[] args) {
        String encryptPassword = SecurityUtils.encryptPassword("gmmG48563@Hst");
        System.out.println(encryptPassword);
    }*/

}
