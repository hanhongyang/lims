package com.gmlimsqi.business.rmts.entity.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.Date;

/**
 * 质检信息同步DTO
 */
@Data
public class QualitySyncDTO {

    /** 订单编号（必填） */
    @NotBlank(message = "订单编号不能为空")
    private String orderNumber;

    /** 车辆id（必填） */
    @NotBlank(message = "车辆id不能为空")
    private Integer carId;

    /** 车牌号（必填） */
    @NotBlank(message = "车牌号不能为空")
    private String carNumber;

    /** 司机id（必填） */
    @NotBlank(message = "司机id不能为空")
    private Integer driverId;

    /** 司机姓名（必填） */
    @NotBlank(message = "司机姓名不能为空")
    private String driverName;

    /** 挂车id（必填） */
    @NotBlank(message = "挂车id不能为空")
    private Integer trailerId;

    /** 挂车车牌（必填） */
    @NotBlank(message = "挂车车牌不能为空")
    private String trailerNumber;

    /** 仓口号（必填，多仓口用逗号分隔） */
    @NotBlank(message = "仓口号不能为空")
    private String openingNumber;

    /** 装车奶量（吨，必填） */
    @NotBlank(message = "装车奶量不能为空")
    private Double milkQuantity;

    /** 第一滴挤奶时间（yyyy-MM-dd HH:mm:ss，必填） */
    @NotBlank(message = "第一滴挤奶时间不能为空")
    private Date milkTime;

    /** 出场温度（必填） */
    @NotBlank(message = "出场温度不能为空")
    private Double temperature;

    /** 铅封号（必填） */
    @NotBlank(message = "铅封号不能为空")
    private String leadSealNum;

    /** 异常类型（必填） */
    @NotBlank(message = "异常类型不能为空")
    private String reasonType;

    /** 异常描述（非必填） */
    private String reason;

    /** 酒精（必填） */
    @NotBlank(message = "酒精不能为空")
    private String alcohol;

    /** 抗生素（必填） */
    @NotBlank(message = "抗生素不能为空")
    private String antibiotic;

    /** 黄曲霉毒素（必填） */
    @NotBlank(message = "黄曲霉毒素不能为空")
    private String aflatoxin;


    /** 掺碱试验（必填） */
    @NotBlank(message = "掺碱试验不能为空")
    private String experiment;

    /** 感官评级（必填） */
    @NotBlank(message = "感官评级不能为空")
    private String tasteAndSmell;

    /** 磷酸盐试验（必填） */
    @NotBlank(message = "磷酸盐试验不能为空")
    private String phosphate;

    /** 状态	Int	是	0，保存 1，提交  暂时数据都为保存状态 */
    private String status;

}