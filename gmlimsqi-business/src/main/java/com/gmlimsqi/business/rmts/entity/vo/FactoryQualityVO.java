package com.gmlimsqi.business.rmts.entity.vo;

import lombok.Data;

/**
 * 工厂质检信息同步VO
 */
@Data
public class FactoryQualityVO {

    /** 订单编号 */
    private String orderNumber;

    /** 工厂编号 */
    private String factoryCode;

    /** 工厂名称 */
    private String factoryName;

    /** 实际奶量 */
    private String actualMilk;

    /** 卸车完成时间 格式 yyyy-MM-dd HH:mm:ss */
    private String unloadTime;

    /** 离开牧场时间  格式 yyyy-MM-dd HH:mm:ss*/
    private String leavePastureTime;

    /** 到达工厂时间  格式 yyyy-MM-dd HH:mm:ss*/
    private String arriveFactoryTime;

    /** 进厂奶温 */
    private String unloadTemperature;

    /** 脂肪 */
    private String testingFat;

    /** 蛋白 */
    private String testingProtein;

    /** 菌落总数 */
    private String testingColonies;

    /** 体细胞 */
    private String testingSomaticCell;

    /** 嗜冷菌 */
    private String testingBacteria;

    /** 需氧芽孢 */
    private String zxyyb;

    /** 耐热需氧芽孢 */
    private String znrxyyb;

    /** 嗜热需氧芽孢 */
    private String zsrxyyb;

    /** 芽孢总数 */
    private String testingSpore;

    /** 黄曲霉毒素M1 */
    private String testingAflatoxin;

    /** 酸度 */
    private String testingAcidity;

    /** 煮沸后酸度差 */
    private String testingAcidDifference;

    /** 冰点 */
    private String testingIce;

    /** 杂志度 */
    private String testingImpurity;

    /** 感官评级 */
    private String testingSensoryRating;

}
