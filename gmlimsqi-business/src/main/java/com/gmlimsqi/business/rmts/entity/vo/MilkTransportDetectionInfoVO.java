package com.gmlimsqi.business.rmts.entity.vo;

import lombok.Data;
import java.io.Serializable;

/**
 * 奶品运输及检测信息实体类
 * 包含订单信息、运输时间、奶量、检测指标等数据
 */
@Data
public class MilkTransportDetectionInfoVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 订单编号
     */
    private String orderNumber;

    /**
     * 工厂编号
     */
    private String factoryCode;

    /**
     * 工厂简称
     */
    private String factoryName;

    /**
     * 实际奶量（单位：吨）
     */
    private Double actualMilk;

    /**
     * 卸车完成时间（格式：yyyy-MM-dd HH:mm:ss）
     */
    private String unloadTime;

    /**
     * 离开牧场时间（格式：yyyy-MM-dd HH:mm:ss）
     */
    private String leavePastureTime;

    /**
     * 到达工厂时间（格式：yyyy-MM-dd HH:mm:ss）
     */
    private String arriveFactoryTime;

    /**
     * 进厂奶温
     */
    private String unloadTemperature;

    /**
     * 脂肪
     */
    private String testingFat;

    /**
     * 蛋白
     */
    private String testingProtein;

    /**
     * 菌落总数
     */
    private String testingColonies;

    /**
     * 体细胞
     */
    private String testingSomaticCell;

    /**
     * 嗜冷菌
     */
    private String testingBacteria;

    /**
     * 需氧芽孢
     */
    private String zxyyb;

    /**
     * 耐热需氧芽孢
     */
    private String znrxyyb;

    /**
     * 嗜热需氧芽孢
     */
    private String zsrxyyb;

    /**
     * 芽孢总数
     */
    private String testingSpore;

    /**
     * 黄曲霉毒素M1
     */
    private String testingAflatoxin;

    /**
     * 酸度
     */
    private String testingAcidity;

    /**
     * 煮沸后酸度差
     */
    private String testingAcidDifference;

    /**
     * 冰点
     */
    private String testingIce;

    /**
     * 杂质度（注：原文"杂志度"应为笔误，修正为"杂质度"）
     */
    private String testingImpurity;

    /**
     * 感官评级
     */
    private String testingSensoryRating;

}