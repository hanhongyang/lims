package com.gmlimsqi.business.rmts.entity.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 4. 图片同步接口请求VO
 */
@Data
public class PhotoSyncDTO {

    /** 订单编号（必填）*/
    @NotBlank(message = "订单号不能为空")
    private String orderNumber;

    /** 出场温度图片（必填）*/
    @NotBlank(message = "请上传出场温度图片")
    private String temperaturePhoto;

    /** 酒精图片（必填） */
    @NotBlank(message = "请上传酒精图片")
    private String alcoholPhoto;

    /** 抗生素图片（必填）*/
    @NotBlank(message = "请上传抗生素图片")
    private String antibioticPhoto;

    /** 黄曲霉毒素图片（必填）*/
    @NotBlank(message = "请上传黄曲霉毒素图片")
    private String aflatoxinPhoto;

    /** 掺碱试验图片（必填） */
    @NotBlank(message = "请上传掺碱试验图片")
    private String experimentPhoto;

    /** 感官评级图片（必填）*/
    @NotBlank(message = "请上传感官评级图片")
    private String tasteAndSmellPhoto;

    /** 磷酸盐试验图片（必填）*/
    @NotBlank(message = "请上传磷酸盐试验图片")
    private String phosphatePhoto;

    /** 缸盖图片（必填）*/
    @NotBlank(message = "请上传缸盖图片")
    private String cylinderHeadPhoto;

    /** 奶罐喷淋头图片 */
    @NotBlank(message = "请上传奶罐喷淋头图片")
    private String sprinklerHeadPhoto;

    /** 内壁、缸盖垫圈图片（必填）*/
    @NotBlank(message = "请上传内壁、缸盖垫圈图片")
    private String washerPhoto;

    /** 血乳图片 （必填）*/
    @NotBlank(message = "请上传血乳图片")
    private String bloodMilkPhoto;

}