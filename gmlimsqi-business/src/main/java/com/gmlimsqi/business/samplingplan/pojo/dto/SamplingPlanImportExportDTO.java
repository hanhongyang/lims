package com.gmlimsqi.business.samplingplan.pojo.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import lombok.Data;
import java.util.Date;

/**
 * 取样计划导入导出DTO（含计划类型字段）
 */
@Data
public class SamplingPlanImportExportDTO {

    /**
     * 主表-计划时间（Excel表头：计划时间）
     */
    @ExcelProperty(value = "计划时间", index = 0)
    @DateTimeFormat("yyyy-MM-dd")
    private Date planTime;

    /**
     * 主表-计划类型（Excel表头：计划类型（0：成品，1：库存，2：垫料））
     */
    @ExcelProperty(value = "计划类型（0：成品，1：库存，2：垫料）", index = 1)
    private String planType;

    /**
     * 子表-物料编码（Excel表头：物料编码）
     */
    @ExcelProperty(value = "物料编码", index = 2)
    private String materialCode;

    /**
     * 子表-物料名称（Excel表头：物料名称）
     */
    @ExcelProperty(value = "物料名称", index = 3)
    private String materialName;

    /**
     * 子表-生产订单号（Excel表头：生产订单号）
     */
    @ExcelProperty(value = "生产订单号", index = 4)
    private String productionOrderNumber;

    /**
     * 子表-计划生产量（Excel表头：计划生产量）
     */
    @ExcelProperty(value = "计划生产量", index = 5)
    private String plannedProductionVolume;

    /**
     * 子表-计划取样份数（Excel表头：计划取样份数）
     */
    @ExcelProperty(value = "计划取样份数", index = 6)
    private String plannedSampleQuantity;
}